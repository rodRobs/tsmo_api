package com.mx.tsmo.clientes.model.dao;

import com.mx.tsmo.clientes.model.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteDao extends JpaRepository<Cliente, Long> {

    List<Cliente> findByCorreo(String correo);

}
