package com.mx.tsmo.envios.model.dao;

import com.mx.tsmo.envios.model.domain.Rastreo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RastreoDao extends JpaRepository<Rastreo, Long> {
}
