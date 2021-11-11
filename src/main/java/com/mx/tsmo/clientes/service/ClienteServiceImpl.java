package com.mx.tsmo.clientes.service;

import com.mx.tsmo.clientes.model.domain.Cliente;
import com.mx.tsmo.clientes.model.dao.ClienteDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class ClienteServiceImpl implements ClienteService {

   @Autowired
   private ClienteDao clienteDao;


    @Override
    public Cliente guardar(Cliente cliente) {
        return clienteDao.save(cliente);
    }

    @Override
    public List<Cliente> buscarPorCorreo(String correo) {
        return clienteDao.findByCorreo(correo);
    }

    @Override
    public Cliente clienteIguales(List<Cliente> clientesBD, Cliente clientePeticion) {
        for (Cliente clienteBD : clientesBD) {
            if (clienteBD.getNombre().equalsIgnoreCase(clientePeticion.getNombre())) {
                log.info("Nombres son iguales");
                if (clienteBD.getTelefono().equalsIgnoreCase(clientePeticion.getTelefono())) {
                    log.info("Telefonos son iguales");
                    return clienteBD;
                }
                // return null;
            }
        }
        return null;
    }

    @Override
    public List<Cliente> listar() {
        return clienteDao.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        return clienteDao.findById(id).orElse(null);
    }
}
