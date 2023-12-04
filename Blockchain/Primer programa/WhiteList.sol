// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract WhiteList{
    address[] allowedUsers;

    function addToWhiteList(address _user) public{
        allowedUsers.push(_user);
    }
    function removeFromWhiteList(address _user) public{
        for (uint i = 0; i < allowedUsers.length; i ++) 
        {
            if(allowedUsers[i] == _user){
                delete allowedUsers[i];
            }
        }
    }

    function checkWhiteList(address _user) public view returns(bool){
        for (uint i = 0; i < allowedUsers.length; i ++) 
        {
            if(allowedUsers[i] == _user){
                return true;
            }
        }
        return false;
    }
}