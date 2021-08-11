package com.mx.tsmo.cotizacion.service;

import com.mx.tsmo.cotizacion.model.domain.CargaNormalLocal;
import com.mx.tsmo.cotizacion.model.dao.CargaNormalLocalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CargaNormalLocalService implements CargaService {

    @Autowired
    private CargaNormalLocalRepository cargaNormalLocalRepository;


    //public CargaNormalLocal getCargaLocalByCarga();
    public List<CargaNormalLocal> listar() {
        log.info("Entra a servicio para listar");
        return cargaNormalLocalRepository.findByOrderByIdAsc();
    }

}
