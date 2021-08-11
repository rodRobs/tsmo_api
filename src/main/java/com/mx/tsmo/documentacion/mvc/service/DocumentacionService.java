package com.mx.tsmo.documentacion.mvc.service;

import com.google.gson.Gson;
import com.mx.tsmo.enums.EnviaAuth;
import com.mx.tsmo.documentacion.model.dto.Documentacion;
import com.mx.tsmo.documentacion.model.dto.Guia;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Base64;

@Service
@Slf4j
public class DocumentacionService {

    private static final String PRE_DOCUMENTACION = "Documentacion/Documentar";

    public Guia preDocumentacion(Documentacion documentacion) {
        log.info("Entra a negocio de envia");
        String res = "";
        try {

            ResteasyClient client = new ResteasyClientBuilder().build();

            WebTarget target = client.target(EnviaAuth.URL.toString()+PRE_DOCUMENTACION);

            Invocation.Builder solicitud = target.request();
            String encodedString = Base64.getEncoder().encodeToString((EnviaAuth.USER.toString()+":"+EnviaAuth.PASS.toString()).getBytes());

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
}
