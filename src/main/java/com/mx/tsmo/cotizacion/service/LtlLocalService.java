package com.mx.tsmo.cotizacion.service;

import com.mx.tsmo.cotizacion.model.domain.LtlLocal;
import com.mx.tsmo.cotizacion.model.dao.LtlLocalRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LtlLocalService implements CargaService {

    @Autowired
    private LtlLocalRespository ltlLocalRespository;

    public List<LtlLocal> listar() {
        return ltlLocalRespository.findByOrderByIdAsc();
    }

}
