// SPDX-License-Identifier: MIT
pragma solidity ^0.8.2;

contract ContractA{
    string variable;
    function writeFunction(string memory param) public{
        variable = param;
    }

    function readFunction() public view returns(string memory){
        return variable;
    }
}