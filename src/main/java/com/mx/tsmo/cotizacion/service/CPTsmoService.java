package com.mx.tsmo.cotizacion.service;

import com.mx.tsmo.cotizacion.model.domain.CPTsmo;

import java.util.List;

public interface CPTsmoService {

    boolean buscarCPTSMO(int cp);
    boolean local(int cpOrigen, int cpDestino);
    List<CPTsmo> buscarCP(int cp);

}
