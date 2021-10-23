package com.mx.tsmo.envios.model.dao;

import com.mx.tsmo.envios.model.domain.EnviosGranel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvioGranelDao extends JpaRepository<EnviosGranel, Long> {
}
