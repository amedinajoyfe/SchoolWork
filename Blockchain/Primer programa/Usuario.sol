// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

contract Usuario{
    uint edad;
    string nombre;
    uint oneTimeDiscount;

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

    function applyDiscount () public {
        oneTimeDiscount += 1;
    }
    function getDiscount () public view returns (uint){
        return oneTimeDiscount;
    }

    constructor () {
        edad = 0;
        nombre = "";
        oneTimeDiscount = 0;
    }
}