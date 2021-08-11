package com.mx.tsmo.cotizacion.model.dao;

import com.mx.tsmo.cotizacion.model.domain.LtlLocal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LtlLocalRespository extends JpaRepository<LtlLocal, Long> {

    public List<LtlLocal> findByOrderByIdAsc();

}
