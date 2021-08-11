package com.mx.tsmo.cotizacion.model.dao;

import com.mx.tsmo.cotizacion.model.domain.Domicilio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DomicilioRepository extends JpaRepository<Domicilio, Long> {

    @Query("SELECT DISTINCT ciudad FROM Domicilio d")
    List<String> findDistinctCiudad();

}
