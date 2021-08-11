package com.mx.tsmo.envios.model.dao;

import com.mx.tsmo.clientes.model.domain.Cliente;
import com.mx.tsmo.envios.model.domain.Envio;
import com.mx.tsmo.security.entity.Usuario;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


public interface EnvioCustomDao {

    List<Envio> findByEstadoEnvioAndEstadoPago(String estadoEnvio, String estadoPago);
    List<Envio> findByParams(Map<String, String> allParams) throws ParseException;
    List<Envio> findByParamsCliente(Map<String, String> allParams, Cliente cliente) throws ParseException;
    List<Envio> findByParamsUsuario(Map<String, String> allParams, Usuario usuario) throws ParseException;

}
