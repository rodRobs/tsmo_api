package com.mx.tsmo.clientes.controller;

import com.mx.tsmo.clientes.model.domain.Cliente;
import com.mx.tsmo.clientes.service.ClienteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("clientes")
@CrossOrigin("*")
@Slf4j
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> guardar(@RequestBody Cliente cliente) {
        log.info("Entra a servicio para guardar Cliente");
        List<Cliente> clientesBD = clienteService.buscarPorCorreo(cliente.getCorreo());
        Cliente clienteBD;
        if (clientesBD.size() > 0) {
            clienteBD = clienteService.clienteIguales(clientesBD, cliente);
            if (clienteBD != null) {
                log.info("Ya existe cliente con esos datos");
                return ResponseEntity.ok(clienteBD);
            } else {
                clienteBD = clienteService.guardar(cliente);
            }
        } else {
            log.info("Entra a guardar cliente");
            clienteBD = clienteService.guardar(cliente);
            if (clienteBD == null) {
                log.error("ERROR: Al guardar el empleado");
                return new ResponseEntity("ERROR: Al guardar el empleado", HttpStatus.BAD_REQUEST);
            }
        }
        return ResponseEntity.ok(clienteBD);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        log.info("Entra a controlador de clientes para listar todos los clientes");
        return ResponseEntity.ok(clienteService.listar());
    }
}
