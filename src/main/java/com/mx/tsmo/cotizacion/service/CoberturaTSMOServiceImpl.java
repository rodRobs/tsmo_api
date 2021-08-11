package com.mx.tsmo.cotizacion.service;

import com.mx.tsmo.cotizacion.model.dao.CoberturaTSMORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoberturaTSMOServiceImpl implements CoberturaTSMOService {

    @Autowired
    private CoberturaTSMORepository coberturaTSMORepository;

    @Override
    public boolean existeCobertura(String cp) {
        return coberturaTSMORepository.existsByCodigo(cp);
    }

    @Override
    public boolean local(String cpOrigen, String cpDestino) {
        return this.existeCobertura(cpOrigen) && this.existeCobertura(cpDestino);
    }
}
