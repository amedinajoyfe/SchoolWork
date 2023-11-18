// SPDX-License-Identifier: MIT
pragma solidity ^0.8.2;
interface IContractA
{
    function writeFunction(string memory param) external;
    function readFunction() external view returns(string memory);
}

contract ContractB
{
    IContractA contractA;
    constructor(address contractAAddress)
    {
        contractA = IContractA(contractAAddress); //0xd8b934580fcE35a11B58C6D73aDeE468a2833fa8
    }

    function writeInteroperability(string memory param) public
    {
        contractA.writeFunction(param);
    }

    function readInteroperability() public view returns(string memory)
    {
        return contractA.readFunction();
    }
}