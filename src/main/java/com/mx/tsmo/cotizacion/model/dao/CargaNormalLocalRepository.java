package com.mx.tsmo.cotizacion.model.dao;

import com.mx.tsmo.cotizacion.model.domain.CargaNormalLocal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CargaNormalLocalRepository extends JpaRepository<CargaNormalLocal, Long> {

    //public CargaNormalLocal findByCarga(Integer carga);
   /*
    @Query("SELECT c FROM CargaNormalLocal c WHERE c:carga BETWEEN c.cargaMin AND c.cargaMax")
    public CargaNormalLocal findCargaLocalByCarga(@Param("carga") double carga);
*/
    List<CargaNormalLocal> findByOrderByIdAsc();
}
