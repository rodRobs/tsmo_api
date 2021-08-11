package com.mx.tsmo.cotizacion.service;

import com.mx.tsmo.cotizacion.model.dao.CostoRepository;
import com.mx.tsmo.cotizacion.model.domain.Costo;
import com.mx.tsmo.cotizacion.model.domain.Cotizacion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class CostoServiceImpl implements CostoService {

    @Autowired
    private CostoRepository costoRepository;

    @Override
    public Costo guardar(Costo costo) {
        log.info("Entra a servicio para guardar costo");
        costo.setCreateAt(new Date());
        return costoRepository.save(costo);
    }

    @Override
    public Costo listarPorCotizacion(Cotizacion cotizacion) {
        log.info("Entra a servicio para buscar costos por cotizacion");
        return costoRepository.findByCotizacion(cotizacion);
    }

    @Override
    public Costo buscarPorCotizacion(Cotizacion cotizacion) {
        return costoRepository.findByCotizacion(cotizacion);
    }


}
