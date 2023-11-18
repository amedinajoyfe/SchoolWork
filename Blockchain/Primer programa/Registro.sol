// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;
import "./Usuario.sol";
contract Register{

    mapping(address => Usuario) dataToUser;
    address[] activeUsers;
    address immutable creator;

    constructor() {
        creator = msg.sender;
        
        Usuario user = new Usuario();
        user.setEdad(99);
        user.setNombre("Admin");

        dataToUser[msg.sender] = user;
        activeUsers.push(msg.sender);
    }

    error tooMuchRetrieved(uint balance, uint maxWithdraw);

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

    modifier noEmptyAddress{
        require(msg.sender != address(0));
        _;
    }

    receive() external payable {
        require(msg.value == 0.025 ether, "No ha introducido suficiente cantidad de ether");
        require(dataToUser[msg.sender].getDiscount() > 0, "Ya ha recibido el descuento");
        dataToUser[msg.sender].applyDiscount();
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
    function createUser() public{
        require(!findInUsers(msg.sender));

        Usuario user = new Usuario();
        dataToUser[msg.sender] = user;
        activeUsers.push(msg.sender);
    }
    function registrarNombre(string memory _nombre) public payable ownerOrUser noEmptyName(_nombre){
        require(msg.value == 0.05 ether || (msg.value == 0.025 ether && dataToUser[msg.sender].getDiscount() > 0), "El valor de la transaccion no es correcto");
        dataToUser[msg.sender].setNombre(_nombre);
        emit ActualizarNombre(msg.sender, _nombre);
    }
    function registrarEdad(uint16 _edad) public ownerOrUser noNegativeAge(_edad){
        dataToUser[msg.sender].setEdad(_edad);
        emit ActualizarEdad(msg.sender, _edad);
    }

    function verNombre() public ownerOrUser view returns(string memory){
        return dataToUser[msg.sender].getNombre();
    }
    function verEdad() public ownerOrUser view returns(uint){
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