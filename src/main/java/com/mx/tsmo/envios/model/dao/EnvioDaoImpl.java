package com.mx.tsmo.envios.model.dao;

import com.mx.tsmo.documentacion.model.dto.Documentacion;
import com.mx.tsmo.envios.model.domain.Envio;
import com.mx.tsmo.envios.service.EnvioService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateOperations;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

@Slf4j
@Repository
public class EnvioDaoImpl {

    @PersistenceContext
    private EntityManager em;

    /*
    public List<Envio> findEnvioByEstadoEnvio(String estadoEnvio) {

        log.info("entra a repository findEnvioByEstadoEnvio");
        log.info("EntityM: " + em);
        //EntityManager em =
        try {
            log.info("Antes de crear el criteria builder");
            CriteriaBuilder cb = em.getCriteriaBuilder();
            log.info("Despues de crear el criteria builder");
            CriteriaQuery<Envio> cq = cb.createQuery(Envio.class);

            Root<Envio> envio = cq.from(Envio.class);
            Predicate estadoEnvioPredicate = cb.equal(envio.get("estadoEnvio"), estadoEnvio);

            cq.where(estadoEnvioPredicate);

            TypedQuery<Envio> query = em.createQuery(cq);
            return query.getResultList();
        } catch (NullPointerException e) {
            log.info("ERROR");
            log.info(e.getMessage());
        }
        return null;
    }*/

    public List<Envio> findByCreateAtAndEstadoEnvioAndEstadoPagoAndClienteAndDocumentacion(Date createAt, String estadoEnvio, String estadoPago, String cliente, Documentacion documentacion) {
        log.info("Ingrea a DAO para criteria");
        Envio envio = null;
        List<Envio> envios = null;
        //CriteriaBuilder cb = em.getCriteriaBuilder();
        try {
            log.info("Antes de crear criteria builder");
            CriteriaBuilder cb = em.getCriteriaBuilder();
            log.info("Despues de crear criteria builder");
            CriteriaQuery<Envio> criteriaQuery = cb.createQuery(Envio.class);

            Root<Envio> fromEnvio = null;
            TypedQuery<Envio> query = null;



            // Query utilizando
            // 1. Consulta todos los envios

            // Paso 1: El objeto EntityManager crea una instancia CriteriaBuilder
            //cb = em.getCriteriaBuilder();

            // Paso 2: Se crea un objeto de CriteriaQuery
            //criteriaQuery = cb.createQuery(Envio.class);

            // Paso 3: Creamos el objeto raíz de query
            fromEnvio = criteriaQuery.from(Envio.class);

            // Paso 4: Seleccionamos lo necesario del from
            criteriaQuery.select(fromEnvio);

            // Paso 5: Creamos el query typeSafe
            query = em.createQuery(criteriaQuery);

            // Paso 6: Ejecutamos la consulta
            envios = query.getResultList();

            mostrarEnvios(envios);

        } catch (NullPointerException e) {
            log.error(""+e);
            log.error(e.getLocalizedMessage());
            log.error("ERROR: "+e.getMessage());
        }
        return envios;
    }

    private static void mostrarEnvios(List<Envio> envios) {
        for (Envio envio : envios) {
            log.debug(""+envio);
        }
    }

}
