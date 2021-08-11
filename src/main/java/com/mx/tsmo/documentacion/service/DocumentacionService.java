package com.mx.tsmo.documentacion.service;

import com.mx.tsmo.documentacion.model.domain.Documentacion;
import com.mx.tsmo.documentacion.model.dto.Guia;
import com.mx.tsmo.envios.model.dto.Dimensiones;

public interface DocumentacionService {

    Guia preDocumentacion(com.mx.tsmo.documentacion.model.dto.Documentacion documentacion);
    String generarGuia();
    Documentacion guardar(Documentacion documentacion);
    void pruebaTimer(String guia);

}
