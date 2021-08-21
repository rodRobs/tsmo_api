package com.mx.tsmo.etiqueta.service;

import com.google.gson.Gson;
import com.mx.tsmo.cobertura.model.dto.CoberturaResponse;
import com.mx.tsmo.enums.EnviaAuth;
import com.mx.tsmo.envios.model.domain.Envio;
import com.mx.tsmo.etiqueta.domain.dto.EtiquetaPost;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
@Slf4j
public class EtiquetaServiceImpl implements EtiquetaService {

    private final static String ETIQUETA = "Documentacion/EtiquetaEnvio";

    @Override
    public byte[] etiquetaEnvia(EtiquetaPost etiquetaPost) {
        log.info("Entra a negocio de envia");
        String res = "";
        Response post = null;
        byte[] responseBytes = null;
        try {
            ResteasyClient client = new ResteasyClientBuilder().build();
            WebTarget target = client.target(EnviaAuth.URL.toString()+ETIQUETA);
            //WebTarget target = client.target(EnviaAuth.URL_PROD.toString()+COBERTURA);
            log.info("URL: "+EnviaAuth.URL.toString()+ETIQUETA);
            /*String cliente = EnviaAuth.CLIENTE_CARGA_NORMAL.toString();
            String user = EnviaAuth.USER_CARGA_NORMAL.toString();
            String pass = EnviaAuth.PASS_CARGA_NORMAL.toString();*/

            Invocation.Builder solicitud = target.request();
            String encodedString = Base64.getEncoder().encodeToString((EnviaAuth.USER.toString()+":"+EnviaAuth.PASS.toString()).getBytes());
            //String encodedString = Base64.getEncoder().encodeToString((user+":"+pass).getBytes());
            solicitud.header("Authorization", "Basic "+encodedString);
            solicitud.header("Content-Type", "application/json");
            log.info("AuthorizationBasic "+encodedString);
            Gson gson = new Gson();
            String jsonString = gson.toJson(etiquetaPost);
            log.info("JSON PETICION: " + jsonString);
            /*Response */post = solicitud.post((Entity.json(jsonString)));
            responseBytes = post.readEntity(byte[].class);
            // String responseJson = post.readEntity(String.class);
            //res = responseJson;
            log.info("Estatus: " + post.getStatus());
            switch (post.getStatus()) {
                case 200:
                    log.info("EXITO");
                    break;
                default:
                    log.info("ERROR");
                    break;
            }
        } catch (Exception e) {
            log.info("Hubo error");
            res = e.getMessage();
        }
        log.info(res);

        return responseBytes;
    }

}
