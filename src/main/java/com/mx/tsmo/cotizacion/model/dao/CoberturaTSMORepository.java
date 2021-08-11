package com.mx.tsmo.cotizacion.model.dao;

import com.mx.tsmo.cotizacion.model.domain.CoberturaTSMO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoberturaTSMORepository extends JpaRepository<CoberturaTSMO, Long> {

    boolean existsByCodigo(String codigo);

}
