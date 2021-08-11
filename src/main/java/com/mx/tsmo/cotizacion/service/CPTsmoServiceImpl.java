package com.mx.tsmo.cotizacion.service;

import com.mx.tsmo.cotizacion.model.dao.CPTsmoRepository;
import com.mx.tsmo.cotizacion.model.domain.CPTsmo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CPTsmoServiceImpl implements CPTsmoService {

    @Autowired
    private CPTsmoRepository cpTsmoRepository;

    @Override
    public boolean buscarCPTSMO(int cp) {
        return cpTsmoRepository.existsByCodigo(cp);
    }

    @Override
    public boolean local(int cpOrigen, int cpDestino) {
        return this.buscarCPTSMO(cpOrigen) && this.buscarCPTSMO(cpDestino);
    }

    @Override
    public List<CPTsmo> buscarCP(int cp) {
        return cpTsmoRepository.findByCodigo(cp);
    }
}
