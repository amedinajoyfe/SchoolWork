// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

contract Usuario{
    uint edad;
    string nombre;

    function setEdad (uint _edad) public {
        edad = _edad;
    }
    function getEdad () public view returns (uint){
        return edad;
    }

    function setNombre (string memory _nombre) public {
        nombre = _nombre;
    }
    function getNombre () public view returns (string memory){
        return nombre;
    }

    constructor () {
        edad = 0;
        nombre = "";
    }
}