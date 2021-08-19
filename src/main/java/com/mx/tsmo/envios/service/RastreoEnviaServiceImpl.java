package com.mx.tsmo.envios.service;

import com.google.gson.Gson;
import com.mx.tsmo.cotizacion.model.domain.Costo;
import com.mx.tsmo.cotizacion.model.domain.Cotizacion;
import com.mx.tsmo.enums.EnviaAuth;
import com.mx.tsmo.envios.model.domain.Envio;
import com.mx.tsmo.envios.model.domain.Rastreo;
import com.mx.tsmo.envios.model.dto.PostRastreo;
import com.mx.tsmo.envios.model.dto.ResponseRastreo;
import com.mx.tsmo.envios.model.enums.DescripcionRastreos;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Base64;
import java.util.List;

@Slf4j
public class RastreoEnviaServiceImpl {

    private static final String RASTREO = "Rastreo";

    public ResponseRastreo[] rastreo(List<PostRastreo> rastreo) {
        log.info("Entra a negocio de envia");
        String res = "";
        try {
            ResteasyClient client = new ResteasyClientBuilder().build();

            //WebTarget target = client.target(EnviaAuth.URL_PROD.toString()+RASTREO);;
            WebTarget target = client.target(EnviaAuth.URL.toString()+RASTREO);

            Invocation.Builder solicitud = target.request();
            String encodedString = Base64.getEncoder().encodeToString((EnviaAuth.USER.toString()+":"+EnviaAuth.PASS.toString()).getBytes());

            solicitud.header("Authorization", "Basic "+encodedString);
            solicitud.header("Content-Type", "application/json");

            Gson gson = new Gson();
            String jsonString = gson.toJson(rastreo);

            log.info("JSON Rastreo: " + jsonString);
            Response post = solicitud.post((Entity.json(jsonString)));

            String responseJson = post.readEntity(String.class);

            res = responseJson;

            log.info("Estatus: " + post.getStatus());
            log.info(responseJson);
            switch (post.getStatus()) {
                case 200:
                    res = responseJson;
                    break;
                default:
                    res = "Error";
                    log.info("");
                    break;
            }

            Gson respuestaJson = new Gson();
            ResponseRastreo[] rastreoRes = respuestaJson.fromJson(res, ResponseRastreo[].class);

            return rastreoRes;
        } catch (Exception e) {
            log.info("Hubo error");
            res = e.getMessage();
        }

        log.info(res);

        return null;

    }



}
