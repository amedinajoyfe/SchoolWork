// SPDX-License-Identifier: MIT
pragma solidity ^0.8.2;

contract MyToken {
    string constant public name = "AlexToken";
    string constant public symbol = "ATK";
    uint8 constant public decimals = 4;
    uint256 constant public supply = 100000;

    mapping(address => uint256) public balance;
    mapping(address => mapping(address => uint256)) public allowance;

    event Transfer(address indexed from, address indexed to, uint256 value);
    event Approval(address indexed owner, address indexed spender, uint256 value);

    constructor() 
    {
        balance[msg.sender] = supply;
        emit Transfer(address(0), msg.sender, supply);
    }

    function transfer(address _to, uint256 _value) external returns (bool success) {
        require(_to != address(0), "ERC20: transfer to the zero address");
        require(balance[msg.sender] >= _value, "ERC20: insufficient balance");

        balance[msg.sender] -= _value;
        balance[_to] += _value;

        emit Transfer(msg.sender, _to, _value);
        return true;
    }

    function approve(address _spender, uint256 _value) external returns (bool success) {
        allowance[msg.sender][_spender] = _value;
        emit Approval(msg.sender, _spender, _value);
        return true;
    }

    function transferFrom(address _from, address _to, uint256 _value) external returns (bool success) {
        require(_to != address(0), "ERC20: transfer to the zero address");
        require(balance[_from] >= _value, "ERC20: insufficient balance");
        require(allowance[_from][msg.sender] >= _value, "ERC20: insufficient allowance");

        balance[_from] -= _value;
        balance[_to] += _value;
        allowance[_from][msg.sender] -= _value;

        emit Transfer(_from, _to, _value);
        return true;
    }

    function totalSupply () public pure returns(uint256){
        return supply;
    }

    function balanceOf (address _account) public view returns(uint256){
        return balance[_account];
    }
}