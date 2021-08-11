package com.mx.tsmo.envios.service;

import com.mx.tsmo.envios.model.domain.Envio;
import com.mx.tsmo.envios.model.domain.Rastreo;

import java.util.List;

public interface RastreoService {

    Rastreo guardar(Rastreo rastreo);
    List<Rastreo> rastrear(Envio envio);
    Rastreo verificarEtapa(int etapa);

}
