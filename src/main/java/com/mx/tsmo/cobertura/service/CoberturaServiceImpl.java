package com.mx.tsmo.cobertura.service;

import com.google.gson.Gson;
import com.mx.tsmo.cobertura.model.dto.CoberturaPost;
import com.mx.tsmo.cobertura.model.dto.CoberturaResponse;
import com.mx.tsmo.cotizacion.model.domain.Costo;
import com.mx.tsmo.cotizacion.model.domain.Cotizacion;
import com.mx.tsmo.cotizacion.model.dto.DistanciaEntreCiudades;
import com.mx.tsmo.cotizacion.service.GoogleDistanceHttp;
import com.mx.tsmo.enums.EnviaAuth;
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
public class CoberturaServiceImpl implements CoberturaService{

    private static final String COBERTURA = "Cobertura";

    private static final String URL_GOOGLE = "https://maps.googleapis.com/maps/api/distancematrix/json";
    private static final String KEY = "AIzaSyCSoaSUrUk2c_MDB_kP4bCVwG3WK6zyLTo";
    private static final String MODE = "driving";
    private static final String LANGUAGE = "es-419";


    @Override
    public CoberturaResponse[] coberturaENVIA(CoberturaPost cobertura) {
        log.info("Entra a negocio de envia");
        String res = "";
        try {
            ResteasyClient client = new ResteasyClientBuilder().build();
            //WebTarget target = client.target(EnviaAuth.URL.toString()+COBERTURA);
            WebTarget target = client.target(EnviaAuth.URL_PROD.toString()+COBERTURA);
            log.info("URL: "+EnviaAuth.URL.toString()+COBERTURA);
            String cliente = EnviaAuth.CLIENTE_CARGA_NORMAL.toString();
            String user = EnviaAuth.USER_CARGA_NORMAL.toString();
            String pass = EnviaAuth.PASS_CARGA_NORMAL.toString();

            Invocation.Builder solicitud = target.request();
            // String encodedString = Base64.getEncoder().encodeToString((EnviaAuth.USER.toString()+":"+EnviaAuth.PASS.toString()).getBytes());
            String encodedString = Base64.getEncoder().encodeToString((user+":"+pass).getBytes());
            solicitud.header("Authorization", "Basic "+encodedString);
            solicitud.header("Content-Type", "application/json");
            log.info("AuthorizationBasic "+encodedString);
            Gson gson = new Gson();
            String jsonString = gson.toJson(cobertura);
            log.info("JSON Cotizacion: " + jsonString);
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
                    break;
            }
        } catch (Exception e) {
            log.info("Hubo error");
            res = e.getMessage();
        }
        log.info(res);
        Gson respuestaJson = new Gson();
        CoberturaResponse[] coberturaResponses = respuestaJson.fromJson(res, CoberturaResponse[].class);
        for (CoberturaResponse coberturaResponse : coberturaResponses) {
            log.info(coberturaResponse.toString());
        }
        return coberturaResponses;
    }

    @Override
    public CoberturaResponse coberturaTSMO(CoberturaPost cobertura) {

        return null;
    }

    @Override
    public boolean coberturaCP(String cp) {
        return false;
    }

    public boolean local() {
        return false;
    }

    public String URL_Google(Cotizacion cotizacion) {
        log.info("Entra a negocio para armar url de api distance Google");
        return URL_GOOGLE +
                "?origins=" + cotizacion.getOrigen().getDomicilio().getCiudad().replace(" ", "+") + "," + cotizacion.getOrigen().getDomicilio().getEstado().replace(" ","+") + "," + cotizacion.getOrigen().getDomicilio().getPais().replace(" ","+")+
                "&destinations=" + cotizacion.getDestino().getDomicilio().getCiudad().replace(" ", "+") + "," + cotizacion.getDestino().getDomicilio().getEstado().replace(" ", "+") + "," + cotizacion.getDestino().getDomicilio().getPais().replace(" ", "+")+
                "&mode="+MODE+
                "&language="+LANGUAGE+
                "&key="+KEY;
    }

    public Double getDistancia(String url) {
        //log.info(String.valueOf(contador++));
        log.info("Entra a servicio para calcular distancia");
        log.info(url);
        GoogleDistanceHttp peticion = new GoogleDistanceHttp();
        String response = peticion.GET(url);
        Gson gson = new Gson();
        DistanciaEntreCiudades distanciaEntreCiudades = gson.fromJson(response, DistanciaEntreCiudades.class);
        System.out.println(gson.toString());
        return (distanciaEntreCiudades.getRows().get(0).getElements().get(0).getDistance().getValue())/1000;
    }

}
