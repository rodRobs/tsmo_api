package com.mx.tsmo.envios.model.dao;

import com.mx.tsmo.envios.model.domain.Cancelacion;
import com.mx.tsmo.envios.model.domain.Envio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancelacionDao extends JpaRepository<Cancelacion, Long> {

    Cancelacion findByEnvio(Envio envio);
}
