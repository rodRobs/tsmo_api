package com.mx.tsmo.cotizacion.service;

import com.mx.tsmo.cotizacion.model.dao.DomicilioRepository;
import com.mx.tsmo.cotizacion.model.domain.Domicilio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DomicilioServiceImpl implements DomicilioService {

    @Autowired
    private DomicilioRepository domicilioRepository;

    @Override
    public List<String> listarCiudades() {
        return domicilioRepository.findDistinctCiudad();
    }
}
