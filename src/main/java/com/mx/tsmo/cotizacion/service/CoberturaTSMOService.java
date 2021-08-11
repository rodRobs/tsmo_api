package com.mx.tsmo.cotizacion.service;

import com.mx.tsmo.cotizacion.model.domain.CoberturaTSMO;

import java.util.List;

public interface CoberturaTSMOService {

    boolean existeCobertura(String cp);
    boolean local(String cpOrigen, String cpDestino);

}
