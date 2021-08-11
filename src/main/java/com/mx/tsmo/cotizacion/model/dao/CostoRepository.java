package com.mx.tsmo.cotizacion.model.dao;

import com.mx.tsmo.cotizacion.model.domain.Costo;
import com.mx.tsmo.cotizacion.model.domain.Cotizacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CostoRepository extends JpaRepository<Costo, Long> {

    Costo findByCotizacion(Cotizacion cotizacion);
    // Costo findById(Long cotizacion);
}
