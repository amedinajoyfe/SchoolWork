// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

// Contratos de OpenZeppelin para funcionalidades de tokens
import "@openzeppelin/contracts/token/ERC20/ERC20.sol";
import "@openzeppelin/contracts/token/ERC20/extensions/ERC20Permit.sol";

/// @title MiniToken
/// @author Alejandro Medina
/// @dev Un token ERC20 para el proyecto de GamesMiniverse.
contract MiniToken is ERC20, ERC20Permit { 
    /// Variables del contrato

    /// @dev La dirección del propietario del contrato.
    address public owner;

    /// Constructor

    /// @dev Constructor del contrato MiniToken.
    constructor() ERC20("MyToken", "MTK") ERC20Permit("MyToken") {
        owner = msg.sender;
    }

    /// Modifiers

    /// @dev Modificador que hace que solo el usuario pueda hacer uso de la función
    modifier requireOwnership {
        if (msg.sender != owner) {
            revert UserNotOwner(msg.sender);
        }
        _;
    }

    /// Eventos

    /// @dev Evento emitido cuando se aprueban tokens para un usuario.
    event TokensApproved(address _user, uint _amount);

    /// @dev Evento emitido cuando se crean nuevos tokens y se asignan a un destinatario.
    event TokensMinted(address _recipient, uint _amount);

    /// Errores

    /// @dev Error al intentar acceder a una función sin ser el dueño.
    /// @param user El usuario que ha intentado acceder.
    error UserNotOwner(address user);

    /// Funciones públicas

    /// @dev Función para crear nuevos tokens.
    /// @param _to La dirección del contrato que recibe los tokens.
    /// @param _amount La cantidad de tokens que se van a crear.
    function mint(address _to, uint256 _amount) public requireOwnership {
        _mint(_to, _amount);
        emit TokensMinted(_to, _amount);
    }

    /// @dev Función para aprobar una cantidad personalizada de tokens ya que approve no funciona.
    /// @param _owner La dirección del propietario.
    /// @param _spender La dirección del contrato autorizado a gatsar tokens.
    /// @param _value La cantidad de tokens que se aprueban.
    function customApprove(address _owner, address _spender, uint _value) public {
        _approve(_owner, _spender, _value);
        emit TokensApproved(_owner, _value);
    }

    /// @dev Función para cambiar de propietario.
    /// @param _newOwner La dirección del nuevo propietario.
    function customTransferOwnership(address _newOwner) public requireOwnership {
        owner = _newOwner;
    }
}