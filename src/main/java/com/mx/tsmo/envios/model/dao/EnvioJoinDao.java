package com.mx.tsmo.envios.model.dao;

import com.mx.tsmo.envios.model.domain.Envio;
import com.mx.tsmo.envios.model.dto.EnvioJoinDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnvioJoinDao extends JpaRepository<Envio, Long> {


}
