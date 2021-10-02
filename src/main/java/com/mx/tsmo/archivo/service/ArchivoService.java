package com.mx.tsmo.archivo.service;

import java.io.File;
import java.util.List;

public interface ArchivoService {

    void leerArchivoExcel(File file);
    void obtener(List cellDataList);
    File file(String fileName);

}
