package com.mx.tsmo.cotizacion.model.dao;

import com.mx.tsmo.cotizacion.model.domain.Cotizacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {
}
