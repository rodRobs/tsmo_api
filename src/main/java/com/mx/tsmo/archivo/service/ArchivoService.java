package com.mx.tsmo.archivo.service;

import com.mx.tsmo.archivo.model.dto.MasivosResponseDto;
import com.mx.tsmo.envios.model.domain.Envio;

import java.io.File;
import java.util.List;

public interface ArchivoService {

    void leerArchivoExcel(File file);
    void obtener(List cellDataList);
    List<Envio> obtenerEnvios(List cellList);
    List<Envio> obtenerEnviosExcel(File file);
    File file(String fileName);
    MasivosResponseDto filtrar(List<Envio> envios);

}
