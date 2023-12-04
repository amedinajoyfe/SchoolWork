// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;
import "./User.sol";
import "./WhiteList.sol";
import "https://github.com/OpenZeppelin/openzeppelin-contracts/blob/master/contracts/token/ERC20/IERC20.sol";

contract Register{
    WhiteList myWhiteList = new WhiteList();
    IERC20 immutable myToken;

    mapping(address => Usuario) dataToUser;
    address[] activeUsers;
    address immutable creator;
    uint constant decimals = 18;

    constructor(IERC20 _myToken) {
        creator = msg.sender;
        
        Usuario user = new Usuario();
        user.setEdad(99);
        user.setNombre("Admin");

        dataToUser[msg.sender] = user;
        activeUsers.push(msg.sender);

        myToken = _myToken;
    }

    error tooMuchRetrieved(uint balance, uint maxWithdraw);

    event CrearUsuario(address _sender);
    event ActualizarEdad(address _sender, uint _age);
    event ActualizarNombre(address _sender, string _name);
    event RetirarSaldoEspecifico(address _receiver, uint _amount);

    modifier onlyOwner{
        require(msg.sender == creator);
        _;
    }
    modifier ownerOrUser{
        require(msg.sender == creator || findInUsers(msg.sender));
        _;
    }

    modifier noEmptyName(string memory _nombre){
        require(bytes(_nombre).length > 0);
        _;
    }
    modifier noNegativeAge(uint _edad){
        require(_edad >= 0);
        _;
    }

    modifier requirePayment{
        require(myToken.transferFrom(msg.sender, address(this), 5 * 10 ** (decimals - 1)));
        _;
    }
    modifier requireWhiteListed(){
        require(myWhiteList.checkWhiteList(msg.sender));
        _;
    }
    modifier checkBalanceOnFinish(){
        _;
        if(myToken.balanceOf(msg.sender) == 0){
            myWhiteList.removeFromWhiteList(msg.sender);
        }
    }

    receive() external payable {
        myToken.transfer(msg.sender, 10 * 10 ** decimals);
        myWhiteList.addToWhiteList(msg.sender);
    }

    function addToWhiteListTest() public{
        myWhiteList.addToWhiteList(msg.sender); //Testing functions
    }function removeFromWhiteListTest() public{
        myWhiteList.removeFromWhiteList(msg.sender);
    }

    function findInUsers(address _address) public view returns(bool) {
        for (uint i = 0; i < activeUsers.length; i ++) 
        {
            if(activeUsers[i] == _address){
                return true;
            }
        }
        return false;
    }
    function createUser() public requirePayment requireWhiteListed checkBalanceOnFinish{
        require(!findInUsers(msg.sender));

        Usuario user = new Usuario();
        dataToUser[msg.sender] = user;
        activeUsers.push(msg.sender);
        emit CrearUsuario(msg.sender);
    }
    function registrarNombre(string memory _nombre) public ownerOrUser noEmptyName(_nombre) requirePayment requireWhiteListed checkBalanceOnFinish{
        dataToUser[msg.sender].setNombre(_nombre);
        emit ActualizarNombre(msg.sender, _nombre);
    }
    function registrarEdad(uint16 _edad) public ownerOrUser noNegativeAge(_edad) requirePayment requireWhiteListed checkBalanceOnFinish{
        dataToUser[msg.sender].setEdad(_edad);
        emit ActualizarEdad(msg.sender, _edad);
    }

    function verNombre() public ownerOrUser view requireWhiteListed returns(string memory) {
        return dataToUser[msg.sender].getNombre();
    }
    function verEdad() public ownerOrUser view requireWhiteListed returns(uint) {
        return dataToUser[msg.sender].getEdad();
    }
    function verAddress() public view returns(address){
        return msg.sender;
    }

    function changeUserNombre(address _address, string memory _nombre) public onlyOwner{
        dataToUser[_address].setNombre(_nombre);
    }
    function changeUserEdad(address _address, uint16 _edad) public onlyOwner{
        dataToUser[_address].setEdad(_edad);
    }

    function retrieveMoney(address payable _address, uint _amount) public onlyOwner{
        if(_amount >= (address(this).balance/3))
        {
            revert tooMuchRetrieved(_amount, (address(this).balance/3));
        }
        _address.transfer(_amount);
        (bool sent, bytes memory data) = _address.call{value: _amount}("");
        emit RetirarSaldoEspecifico(_address, _amount);
    }
    function seeBalance() public view returns(uint balance){
        return address(this).balance;
    }
}