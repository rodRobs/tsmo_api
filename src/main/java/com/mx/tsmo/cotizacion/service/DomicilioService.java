package com.mx.tsmo.cotizacion.service;

import com.mx.tsmo.cotizacion.model.domain.Domicilio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DomicilioService {

    List<String> listarCiudades();

}
