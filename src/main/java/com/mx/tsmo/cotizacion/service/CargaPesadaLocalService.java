package com.mx.tsmo.cotizacion.service;

import com.mx.tsmo.cotizacion.model.domain.CargaPesadaLocal;
import com.mx.tsmo.cotizacion.model.dao.CargaPesadaLocalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CargaPesadaLocalService implements CargaService {

    @Autowired
    private CargaPesadaLocalRepository cargaPesadaLocalRepository;

    public List<CargaPesadaLocal> listar() {
        log.info("Entra a servicio para listar");
        return cargaPesadaLocalRepository.findByOrderByIdAsc();
    }

}
