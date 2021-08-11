package com.mx.tsmo.cotizacion.model.dao;

import com.mx.tsmo.cotizacion.model.domain.CargaPesadaLocal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CargaPesadaLocalRepository extends JpaRepository<CargaPesadaLocal, Long> {

    List<CargaPesadaLocal> findByOrderByIdAsc();
}
