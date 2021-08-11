package com.mx.tsmo.documentacion.model.dao;

import com.mx.tsmo.documentacion.model.domain.Documentacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentacionRepository extends JpaRepository<Documentacion, Long> {
}
