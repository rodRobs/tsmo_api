package com.mx.tsmo.envios.model.dao;

import com.mx.tsmo.clientes.model.domain.Cliente;
import com.mx.tsmo.clientes.service.ClienteService;
import com.mx.tsmo.cotizacion.model.domain.Cotizacion;
import com.mx.tsmo.documentacion.model.domain.Documentacion;
import com.mx.tsmo.envios.model.domain.Envio;
import com.mx.tsmo.security.entity.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class EnvioCustomDaoImpl implements EnvioCustomDao {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ClienteService clienteService;

    @Override
    public List<Envio> findByEstadoEnvioAndEstadoPago(String estadoEnvio, String estadoPago) {
        log.info("Entra a Envio Custom Dao Impl");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();

        Root<Envio> envio = cq.from(Envio.class);

        List<Predicate> predicates = new ArrayList<>();


        if (estadoEnvio != null && estadoEnvio != "")
            predicates.add(cb.equal(envio.get("estadoEnvio"), estadoEnvio));

        if (estadoPago != null && estadoPago != "")
            predicates.add(cb.equal(envio.get("estadoPago"), estadoPago));

        // Predicate estadoEnvioPredicate = cb.equal(envio.get("estadoEnvio"), estadoEnvio);
        // Predicate estadoPagoPredicate = cb.equal(envio.get("estadoPago"), estadoPago);
        //cq.where(estadoEnvioPredicate, estadoPagoPredicate);
        cq.where(predicates.toArray(new Predicate[0]));
        cq.select(envio);
        TypedQuery<Envio> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<Envio> findByParams(Map<String, String> allParams) throws ParseException {
        log.info("Entra a dao para buscar por parametros");
        Iterator<Map.Entry<String, String>> it = allParams.entrySet().iterator();


        // log.info("periodoInicial: "+periodoInicial);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();

        Root<Envio> fromEnvio = cq.from(Envio.class);

        List<Predicate> predicates = new ArrayList<>();

        Predicate date;

        Date periodoInicial = null;
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            log.info(entry.getKey());
            switch (entry.getKey()) {
                case "periodoInicial":
                    log.info(entry.getKey());
                    periodoInicial = new SimpleDateFormat("yyyy-MM-dd").parse(entry.getValue());
                    log.info(""+periodoInicial);
                    break;
                case "periodoFinal":
                    Date periodoFinal = new SimpleDateFormat("yyyy-MM-dd").parse(entry.getValue());
                    Calendar c = Calendar.getInstance();
                    c.setTime(periodoFinal);
                    c.add(Calendar.DATE, 1);
                    predicates.add(cb.between(fromEnvio.get("createAt"), periodoInicial, c.getTime()));
                    log.info(""+c.getTime());
                    break;
                case "estadoEnvio":
                    predicates.add(cb.equal(fromEnvio.get("estadoEnvio"), entry.getValue()));
                    break;
                case "estadoPago":
                    predicates.add(cb.equal(fromEnvio.get("estadoPago"), entry.getValue()));
                    break;
                case "cliente":
                    Cliente cliente = Cliente.builder().id(Long.parseLong(entry.getValue())).build();
                    predicates.add(cb.equal(fromEnvio.get("cliente"), cliente));
                    break;
                case "proveedor":
                    predicates.add(cb.like(fromEnvio.get("guiaTsmo"), entry.getValue() + "%"));
                    // Documentacion doc = Documentacion.builder().cotizacion(Cotizacion.builder().realiza(entry.getValue()).build()).build();
                    // predicates.add(cb.equal(fromEnvio.get("documentacion"), doc));
                    break;
                case "estadoDestino":
                    predicates.add(cb.equal(fromEnvio.get("estadoDestino"), entry.getValue()));
                    break;
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.select(fromEnvio);
        TypedQuery<Envio> query = entityManager.createQuery(cq);
        return query.getResultList();

    }

    @Override
    public List<Envio> findByParamsCliente(Map<String, String> allParams, Cliente clienteUno) throws ParseException {
        log.info("Entra a dao para buscar por parametros para cliente");
        Iterator<Map.Entry<String, String>> it = allParams.entrySet().iterator();


        // log.info("periodoInicial: "+periodoInicial);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();

        Root<Envio> fromEnvio = cq.from(Envio.class);

        List<Predicate> predicates = new ArrayList<>();

        Predicate date;

        Date periodoInicial = null;
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            log.info(entry.getKey());
            switch (entry.getKey()) {
                case "periodoInicial":
                    log.info(entry.getKey());
                    periodoInicial = new SimpleDateFormat("yyyy-MM-dd").parse(entry.getValue());
                    log.info(""+periodoInicial);
                    break;
                case "periodoFinal":
                    Date periodoFinal = new SimpleDateFormat("yyyy-MM-dd").parse(entry.getValue());
                    Calendar c = Calendar.getInstance();
                    c.setTime(periodoFinal);
                    c.add(Calendar.DATE, 1);
                    predicates.add(cb.between(fromEnvio.get("createAt"), periodoInicial, c.getTime()));
                    log.info(""+c.getTime());
                    break;
                case "estadoEnvio":
                    predicates.add(cb.equal(fromEnvio.get("estadoEnvio"), entry.getValue()));
                    break;
                case "estadoPago":
                    predicates.add(cb.equal(fromEnvio.get("estadoPago"), entry.getValue()));
                    break;
                case "cliente":
                    Cliente cliente = Cliente.builder().id(Long.parseLong(entry.getValue())).build();
                    predicates.add(cb.equal(fromEnvio.get("cliente"), cliente));
                    break;
                case "proveedor":
                    predicates.add(cb.like(fromEnvio.get("guiaTsmo"), entry.getValue() + "%"));
                    // Documentacion doc = Documentacion.builder().cotizacion(Cotizacion.builder().realiza(entry.getValue()).build()).build();
                    // predicates.add(cb.equal(fromEnvio.get("documentacion"), doc));
                    break;
                case "estadoDestino":
                    predicates.add(cb.equal(fromEnvio.get("estadoDestino"), entry.getValue()));
                    break;
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.select(fromEnvio);
        TypedQuery<Envio> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<Envio> findByParamsUsuario(Map<String, String> allParams, Usuario usuario) throws ParseException {
        return null;
    }


}
