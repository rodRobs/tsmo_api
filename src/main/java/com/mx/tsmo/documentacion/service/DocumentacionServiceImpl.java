package com.mx.tsmo.documentacion.service;

import com.google.gson.Gson;
import com.mx.tsmo.cotizacion.model.domain.Detalle;
import com.mx.tsmo.cotizacion.model.domain.Dimensiones;
import com.mx.tsmo.cotizacion.model.dto.DetalleDto;
import com.mx.tsmo.cotizacion.model.dto.DimensionesDto;
import com.mx.tsmo.cotizacion.service.CotizacionService;
import com.mx.tsmo.documentacion.model.dao.DocumentacionRepository;
import com.mx.tsmo.documentacion.model.dto.Documentacion;
import com.mx.tsmo.documentacion.model.dto.Guia;
import com.mx.tsmo.documentacion.timertask.RastreoEnviaTask;
import com.mx.tsmo.enums.EnviaAuth;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.*;

@Service
@Transactional
@Slf4j
public class DocumentacionServiceImpl implements DocumentacionService {

    private static final String PRE_DOCUMENTACION = "Documentacion/Documentar";

    @Autowired
    private DocumentacionRepository documentacionRepository;

    @Autowired
    private CotizacionService cotizacionService;

    @Override
    public Guia preDocumentacion(Documentacion documentacion) {
        log.info("Entra a negocio de envia");
        String res = "";
        try {

            ResteasyClient client = new ResteasyClientBuilder().build();

            WebTarget target = client.target(EnviaAuth.URL.toString()+PRE_DOCUMENTACION);
            // WebTarget target = client.target(EnviaAuth.URL_PROD.toString()+PRE_DOCUMENTACION);

            String cliente = "";
            String user = "";
            String pass = "";

            double peso = cotizacionService.getPeso(Detalle.builder()
                    .dimensiones(Dimensiones.builder()
                            .alto(documentacion.getDetalle().get(0).getDimensiones().getAlto())
                            .ancho(documentacion.getDetalle().get(0).getDimensiones().getAncho())
                            .largo(documentacion.getDetalle().get(0).getDimensiones().getLargo())
                            .peso(documentacion.getDetalle().get(0).getDimensiones().getPeso())
                            .build())
                    .build());

            switch(cotizacionService.getTipoCarga(peso)) {
                case 1:
                    cliente = EnviaAuth.CLIENTE_CARGA_NORMAL.toString();
                    user = EnviaAuth.USER_CARGA_NORMAL.toString();
                    pass = EnviaAuth.PASS_CARGA_NORMAL.toString();
                    break;
                case 2:
                    cliente = EnviaAuth.CLIENTE_CARGA_PESADA.toString();
                    user = EnviaAuth.USER_CARGA_PESADA.toString();
                    pass = EnviaAuth.PASS_CARGA_PESADA.toString();
                    break;
                case 3:
                    cliente = EnviaAuth.CLIENTE_LTL.toString();
                    user = EnviaAuth.USER_LTL.toString();
                    pass = EnviaAuth.PASS_LTL.toString();
                    break;
            }

            Invocation.Builder solicitud = target.request();
            String encodedString = Base64.getEncoder().encodeToString((EnviaAuth.USER.toString()+":"+EnviaAuth.PASS.toString()).getBytes());
            // String encodedString = Base64.getEncoder().encodeToString((user+":"+pass).getBytes());
            documentacion.setCuenta(cliente);
            solicitud.header("Authorization", "Basic "+encodedString);
            solicitud.header("Content-Type", "application/json");

            Gson gson = new Gson();
            String jsonString = gson.toJson(documentacion);

            log.info("JSON Pre-Documentacion: " + jsonString);
            Response post = solicitud.post((Entity.json(jsonString)));

            String responseJson = post.readEntity(String.class);

            res = responseJson;

            log.info("Estatus: " + post.getStatus());

            switch (post.getStatus()) {
                case 200:
                    res = responseJson;
                    break;
                default:
                    res = "Error";
                    log.info(responseJson);
                    break;
            }
        } catch (Exception e) {
            log.info("Hubo error");
            log.info(e.getMessage());
            res = e.getMessage();
        }

        log.info(res);


        Gson respuestaJson = new Gson();
        Guia guiaRes = respuestaJson.fromJson(res, Guia.class);

        return guiaRes;
    }

    public String generarGuia() {
        Date fecha = new Date();
        int mes = fecha.getMonth();
        int anio = fecha.getYear();
        int segundo = fecha.getSeconds();

        return "";
    }

    @Override
    public com.mx.tsmo.documentacion.model.domain.Documentacion guardar(com.mx.tsmo.documentacion.model.domain.Documentacion documentacion) {
        log.info("Entra a servicio para guardar documentacion");
        return documentacionRepository.save(documentacion);
    }

    @Override
    public void pruebaTimer(String guia) {
        Timer timer = new Timer();
        Date fechaRegistro = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(fechaRegistro);
        c.add(Calendar.MINUTE, 1);
        fechaRegistro = c.getTime();

        TimerTask task = new RastreoEnviaTask(guia);
        timer.schedule(task, fechaRegistro, 21600000);

        // timer.schedule(task, 0, 5000);
        /*
        TimerTask tarea = new TimerTask() {

            private int contador = 0;

            @Override
            public void run() {
                contador++;
                log.info("Peticion para rastreo");
                log.info("Se ejecuto en: " + new Date());
                if (contador == 5) {
                    log.info("Entra a cancelar tarea");
                    this.cancel();
                }
            }
        };

        timer.schedule(tarea, 0, 1000);
        */
    }


}
