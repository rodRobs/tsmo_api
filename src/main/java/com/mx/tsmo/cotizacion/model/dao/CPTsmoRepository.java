package com.mx.tsmo.cotizacion.model.dao;


import com.mx.tsmo.cotizacion.model.domain.CPTsmo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CPTsmoRepository extends JpaRepository<CPTsmo, Long> {

    Boolean existsByCodigo(int codigo);
    List<CPTsmo> findByCodigo(int codigo);

}
