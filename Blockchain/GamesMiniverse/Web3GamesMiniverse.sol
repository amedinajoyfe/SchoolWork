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
    address[] private userList;

    /// @dev Lista de juegos disponibles.
    Game[] private gameList;

    // Estructura para Game
    struct Game {
        uint id;
        string name;
        uint price;
        uint prize;
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
    /// @param name El nombre del juego iniciado.
    /// @param user La dirección del usuario.
    event GameStarted(string name, address user);

    /// @dev Este evento se emite cuando se registra un nuevo usuario.
    /// @param user La dirección del nuevo usuario.
    event UserRegistered(address user);

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

    /// @dev Error al no encontrar un juego en la lista.
    /// @param gameId El ID del juego buscado.
    error GameNotFound(uint gameId);

    /// Funciones públicas

    /// @dev Permite a los usuarios comprar tokens con Matic.
    function buyTokens() public payable requireInList {
        require(msg.value == 0.1 ether, "Valor incorrecto");
        userReceive(10);
    }

    /// @dev Se activa cuando un usuario gana un juego para recibir tokens según la puntuación.
    /// @param _score La puntuación del usuario.
    /// @param _gameId La id del juego ganado.
    function winGame(uint _score, uint _gameId) public requireInList {
        require(_score > 1, "La puntuacion debe ser mayor que 10");
        Game memory playedGame = findGame(_gameId);
        userReceive(_score / playedGame.prize);
    }

    /// @dev Permite a los usuarios iniciar un juego pagando tokens.
    /// @param _id La ID del juego que se va a iniciar.
    function startGame(uint _id) public requireInList {
        Game memory playedGame = findGame(_id);
        userPay(playedGame.price);
        emit GameStarted(playedGame.name, msg.sender);
    }

    /// @dev Permite a los nuevos usuarios registrarse y a su vez se les envía 10 tokens.
    function registerNewUser() public {
        require(!findUser(msg.sender), "Usuario ya registrado");
        approveSpending(10);
        if (myToken.allowance(msg.sender, address(this)) < 1) revert NotApproved(msg.sender);
            userReceive(10);
        userList.push(msg.sender);
        emit UserRegistered(msg.sender);
    }

    /// @dev Permite a un usuario comprobar si está registrado o no.
    function checkInList() public view returns(bool) {
        return findUser(msg.sender);
    }

    /// @dev Permite ver el nombre del contrato, podría ser una variable pública pero quería incluir una función "pure".
    function viewGameName() public pure returns(string memory) {
        return contractName;
    }

    /// @dev Permite al propietario del contrato agregar un nuevo juego a la lista.
    /// @param _name El nombre del juego.
    /// @param _price El coste de jugar el juego.
    function addGame(string memory _name, uint _price, uint _prize) public requireOwnership {
        Game memory myGame;
        myGame.name = _name;
        myGame.price = _price;
        myGame.prize = _prize;
        myGame.id = gameList.length;
        gameList.push(myGame);
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

    /// @dev Encuentra a un usuario en la lista de usuarios.
    /// @param _user La dirección del usuario.
    /// @return Si el usuario se encuentra en la lisya o no.
    function findUser(address _user) private view returns (bool) {
        for (uint i = 0; i < userList.length; i++) {
            if (userList[i] == _user) 
                return true;
        }
        return false;
    }

    /// @dev Encuentra un juego en la lista de juegos por su ID.
    /// @param _gameId El ID del juego a encontrar.
    /// @return El juego encontrado.
    function findGame(uint _gameId) private view returns (Game memory) {
        for (uint i = 0; i < gameList.length; i++) {
            if (gameList[i].id == _gameId) 
                return gameList[i];
        }
        revert GameNotFound(_gameId);
    }
}