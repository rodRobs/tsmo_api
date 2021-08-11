package com.mx.tsmo.clientes.service;

import com.mx.tsmo.clientes.model.domain.Cliente;

import java.util.List;

public interface ClienteService {

    Cliente guardar(Cliente cliente);
    List<Cliente> buscarPorCorreo(String correo);
    Cliente clienteIguales(List<Cliente> clientesBD, Cliente clientePeticion);
    List<Cliente> listar();

}
