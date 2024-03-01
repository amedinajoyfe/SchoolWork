// SPDX-License-Identifier: MIT

pragma solidity ^0.8.20;

// Importando el contrato MiniToken para utilizar sus funciones en el contrato
import "./MiniToken.sol";

/// Importante, en las transacciones llamar al numero de tokens que queremos tal cual, ej: 10, no 10 * 10e18, porque los tokens ya están en las operaciones
/// @title GamesMiniverse
/// @author Alejandro Medina
/// @dev Un contrato que sirve como Backend Web3 para una página web de minijuegos.
contract GamesMiniverse {

    /// Variables del contrato

    /// @dev La dirección del propietario del contrato.
    address public immutable owner;

    /// @dev El contrato MiniToken (el token que utiliza este contrato).
    MiniToken private immutable myToken;
    
    /// @dev El nombre del contrato.
    string private constant contractName = "GamesMiniverse";

    /// @dev Lista de usuarios registrados.
    mapping(address => User) private userList;
    HighScore[] private highScoreList;

    struct HighScore{
        uint id_game;
        uint score;
    }

    // Estructura para Usuarios
    struct User {
        uint[] achievements;
        string username;
        uint[] highScoreReferences;
    }

    /// Constructor

    /// @dev Constructor para inicializar el contrato.
    /// @param _myToken La dirección del contrato del Token.
    constructor(MiniToken _myToken) {
        myToken = _myToken;
        owner = msg.sender;
    }

    /// Función fallback

    /// @dev Función fallback para hacer revert de llamadas erróneas.
    fallback() external {
        revert("Ha ocurrido un error inesperado");
    }

    /// Modifiers

    /// @dev Modificador que sirve para asegurar que el usuario esté en la lista de usuarios.
    modifier requireInList {
        require(findUser(msg.sender), "Usuario no esta en la lista");
        _;
    }

    /// @dev Modificador que hace que solo el usuario pueda hacer uso de la función
    modifier requireOwnership {
        if (msg.sender != owner) {
            revert UserNotOwner(msg.sender);
        }
        _;
    }

    /// Eventos

    /// @dev Este evento se emite cuando un usuario comienza a jugar.
    /// @param gameId La id del juego iniciado.
    /// @param user La dirección del usuario.
    event GameStarted(int gameId, address user);

    event RegisterRequired();

    /// @dev Este evento se emite cuando se registra un nuevo usuario.
    /// @param user La dirección del nuevo usuario.
    /// @param username El nombre del nuevo usuario.
    event UserRegistered(address user, string username);

    /// Errores

    /// @dev Error cuando el usuario no aprueba el gasto.
    /// @param user La dirección del usuario que no ha aprobado el gasto.
    error NotApproved(address user);

    /// @dev Error al no encontrar un usuario en la lista.
    /// @param user La dirección del usuario buscado.
    error UserNotFound(address user);

    /// @dev Error al intentar acceder a una función sin ser el dueño.
    /// @param user El usuario que ha intentado acceder.
    error UserNotOwner(address user);

    /// Funciones públicas

    /// @dev Permite a los usuarios comprar tokens con Matic.
    function buyTokens() public payable requireInList {
        require(msg.value == 0.1 ether, "Valor incorrecto");
        userReceive(10);
    }

    /// @dev Permite a los usuarios iniciar un juego pagando tokens.
    /// @param _id El id del juego iniciado
    /// @param _price El precio del juego.
    function startGame(int _id, uint _price) public requireInList {
        userPay(_price);
        emit GameStarted(_id, msg.sender);
    }

    /// @dev Se activa cuando un usuario gana un juego para recibir tokens según la puntuación.
    /// @param _id El id del juego finalizado
    /// @param _score La puntuación del usuario.
    /// @param _prize El premio del juego.
    function endGame(uint _id, uint _score,  uint _prize) public requireInList {
        require(_score > 1, "La puntuacion debe ser mayor que 1");
        userReceive(_score / _prize);
        setNewScore(_id, _score, msg.sender);
    }


    function loginAttempt(address _user) public view returns(User memory){
        return userList[_user];
    }

    /// @dev Permite a los nuevos usuarios registrarse y a su vez se les envía 10 tokens.
    /// @param _username El nombre de usuario para el registro
    function registerNewUser(string memory _username, address _registeredUser) public {
        require(!findUser(_registeredUser), "Usuario ya registrado");
        approveSpending(10);
        if (myToken.allowance(_registeredUser, address(this)) < 1) revert NotApproved(_registeredUser);
            userReceive(10);
        userList[_registeredUser] = User(new uint[](0), _username, new uint[](0));
        emit UserRegistered(_registeredUser, _username);
    }

    
    function getGameScore(uint _id) public view returns(uint _score){
        HighScore memory foundHighScore;
        for (uint i = 0; i < userList[msg.sender].highScoreReferences.length; i ++) 
        {
            foundHighScore = highScoreList[userList[msg.sender].highScoreReferences[i]];
            if(foundHighScore.id_game == _id)
            {
                return foundHighScore.score;
            }
        }
        return 0;
    }

    function addAchievement(uint _id) public{
        for (uint i = 0; i < userList[msg.sender].achievements.length; i ++) 
        {
            if(userList[msg.sender].achievements[i] == _id)
            {
                return;
            }
        }
        userList[msg.sender].achievements.push(_id);
    }

    /// @dev Permite a un usuario comprobar si está registrado o no.
    /// @param _user La dirección del usuario que se quiere buscar
    function checkInList(address _user) public view returns(bool) {
        return findUser(_user);
    }

    /// @dev Permite ver el nombre del contrato, podría ser una variable pública pero quería incluir una función "pure".
    function viewContractName() public pure returns(string memory) {
        return contractName;
    }

    /// Funciones privadas

    /// @dev Aprueba el gasto de tokens por el contrato en nombre del usuario.
    /// @param _value La cantidad de tokens que se aprueban.
    function approveSpending(uint _value) private {
        myToken.customApprove(msg.sender, address(this), _value * 10 ** myToken.decimals());
    }

    /// @dev Transfiere tokens del usuario al contrato.
    /// @param _value La cantidad de tokens que se transferirán.
    function userPay(uint _value) private {
        myToken.transferFrom(msg.sender, address(this), _value * 10 ** myToken.decimals());
    }

    /// @dev El contrato envía tokens.
    /// @param _value La cantidad de tokens que se envían.
    function userReceive(uint _value) private {
        myToken.transfer(msg.sender, _value * 10 **myToken.decimals());
    }

    function setNewScore(uint _game_id, uint _score, address _user) private{
        bool found = false;
        HighScore memory foundHighScore;
        for (uint i = 0; i < userList[_user].highScoreReferences.length; i ++) 
        {
            foundHighScore = highScoreList[userList[_user].highScoreReferences[i]];
            if(foundHighScore.id_game == _game_id)
            {
                found = true;
                if(_score > foundHighScore.score)
                {
                    highScoreList[userList[_user].highScoreReferences[i]].score = _score;
                }
                break;
            }
        }
        if(!found)
        {
            highScoreList.push(HighScore(_game_id, _score));
            userList[_user].highScoreReferences.push(highScoreList.length);
        }
    }

    /// @dev Encuentra a un usuario en la lista de usuarios.
    /// @param _user La dirección del usuario.
    /// @return Si el usuario se encuentra en la lisya o no.
    function findUser(address _user) public view returns (bool) {
        return bytes(userList[_user].username).length > 0;
    }

    function findUserName(address _user) public view returns (string memory) {
        return userList[_user].username;
    }
}