package com.mx.tsmo.cotizacion.service;

import com.google.gson.Gson;
import com.mx.tsmo.cotizacion.model.domain.*;
import com.mx.tsmo.cotizacion.model.dto.*;
import com.mx.tsmo.enums.EnviaAuth;
import com.mx.tsmo.enums.TipoServicio;
import com.mx.tsmo.envios.model.dto.PostCancelacion;
import com.mx.tsmo.envios.model.dto.ResponseCancelacion;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
public class EnviaServiceImpl implements EnviaService {

    private static final String COTIZACION = "Cotizador/CotizaCliente";
    private static final String CANCELACION = "Cancelacion/Cancelar";

    @Autowired
    private CoberturaTSMOService coberturaService;

    //public String cotizacion(Cotizacion cotizacion) {
    //public Costo[] cotizacion(Cotizacion cotizacion) {
    public Response cotizacion(Cotizacion cotizacion, int tipoCarga) {
        log.info("Entra a negocio de envia");
        String res = "";
        try {
            Cotizacion cotizacionEnvia = this.cotizacionEnvia(cotizacion);
            String cliente = "";
            String user = "";
            String pass = "";
            switch(tipoCarga) {
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


            cotizacionEnvia.setCuenta(EnviaAuth.CLIENTE.toString());
            //cotizacionEnvia.setCuenta(cliente);
            ResteasyClient client = new ResteasyClientBuilder().build();

            WebTarget target = client.target(EnviaAuth.URL.toString()+COTIZACION);
            // WebTarget target = client.target(EnviaAuth.URL_PROD.toString()+COTIZACION);
            //log.info("URL: "+EnviaAuth.URL.toString()+COTIZACION);
            log.info("URL: "+EnviaAuth.URL_PROD.toString()+COTIZACION);
            Invocation.Builder solicitud = target.request();
            String encodedString = Base64.getEncoder().encodeToString((EnviaAuth.USER.toString()+":"+EnviaAuth.PASS.toString()).getBytes());
            //String encodedString = Base64.getEncoder().encodeToString((user+":"+pass).getBytes());
            solicitud.header("Authorization", "Basic "+encodedString);
            log.info("Authorization: Basic "+encodedString);
            solicitud.header("Content-Type", "application/json");

            Gson gson = new Gson();
            String jsonString = gson.toJson(cotizacionEnvia);

            log.info("JSON Cotizacion: " + jsonString);
            Response post = solicitud.post((Entity.json(jsonString)));
            log.info("Response POST: " + post.getStatus());
            log.info("Response Header: " + post.getHeaders());
            return post;

            /*

            String responseJson = post.readEntity(String.class);

            res = responseJson;

            log.info("Estatus: " + post.getStatus());

            switch (post.getStatus()) {
                case 200:
                    return res = responseJson;
                    //break;
                default:
                    res = "Error";
                    log.info("ResJson: "+responseJson);
                    return res = responseJson;
                    //return null;
                    //break;
            }
        /*
            Gson respuestaJson = new Gson();
            Costo[] cotizacionRes = respuestaJson.fromJson(res, Costo[].class);
            for (Costo cotizacionResponse : cotizacionRes) {
                log.info(cotizacionResponse.toString());
            }
            return cotizacionRes;
        */
        } catch (Exception e) {
            log.info("Hubo error");
            res = e.getMessage();
        }

        log.info(res);

        return null;

    }

    // public Costo calcularEnvia(Cotizacion cotizacion) {
    public Response calcularEnvia(Cotizacion cotizacion, int tipoCarga) {
        log.info("Entra a controlador para calcular Envia");
        List<DetalleDto> detalles = new ArrayList<>();
        detalles.add(DetalleDto.builder().ValorDeclarado(cotizacion.getDetalle().get(0).getValorDeclarado()).Dimensiones(DimensionesDto.builder().Largo((short) cotizacion.getDetalle().get(0).getDimensiones().getLargo()).Alto((short) cotizacion.getDetalle().get(0).getDimensiones().getAlto()).Ancho((short) cotizacion.getDetalle().get(0).getDimensiones().getPeso()).Peso((short) cotizacion.getDetalle().get(0).getDimensiones().getPeso()).build()).build());
        /*
        PostCotizacionDto cotizacionPost = PostCotizacionDto.builder()
                .Cuenta(EnviaAuth.CLIENTE.toString())
                .Opciones(OpcionesDto.builder().TipoEnvio("P").TipoEntrega("D").TipoServicio("").TipoCobro("").build())
                .Origen(OrigenDto.builder().Domicilio(DomicilioDto.builder().CodigoPostal(cotizacion.getCpOrigen()).build()).build())
                .Destino(DestinoDto.builder().Domicilio(DomicilioDto.builder().CodigoPostal(cotizacion.getCpDestino()).build()).build())
                .Detalle(detalles)
                .build();
        */
        return this.cotizacion(cotizacion, tipoCarga);
        /*
        String res = this.cotizacion(cotizacion);
        if (res.length > 0) {
            for (Costo respuesta: res) {
                return respuesta;
            }
        }
        return null;
         */
    }



    public CargaDto crearCargaDTODeResponseCotizacion(Costo costo) {
        double precio = costo.getTotal() * 1.3;
        return CargaDto.builder()
                .precio((float) precio)
                .flete((float) costo.getFlete())
                .cxc(((float) costo.getCombustible()))
                .utilidad(costo.getTotal()*0.3)
                .build();
    }

    public ResponseCancelacion cancelacion(PostCancelacion postCancelacion) {
        log.info("Entra a servicio para cancelar en Envia");
        String res = "";
        try {
            ResteasyClient client = new ResteasyClientBuilder().build();

            WebTarget target = client.target(EnviaAuth.URL.toString()+COTIZACION);

            Invocation.Builder solicitud = target.request();
            String encodedString = Base64.getEncoder().encodeToString((EnviaAuth.USER.toString()+":"+EnviaAuth.PASS.toString()).getBytes());

            solicitud.header("Authorization", "Basic "+encodedString);
            solicitud.header("Content-Type", "application/json");

            Gson gson = new Gson();
            String jsonString = gson.toJson(postCancelacion);

            log.info("JSON Cancelacion: " + jsonString);
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
                    log.info("");
                    break;
            }

            Gson respuestaJson = new Gson();
            ResponseCancelacion cancelacionRes = respuestaJson.fromJson(res, ResponseCancelacion.class);

            return cancelacionRes;
        } catch (Exception e) {
            log.error("ERROR: "+e.getMessage());
        }
        return null;
    }

    @Override
    public Cotizacion cotizacionEnvia(Cotizacion cotizacion) {
        log.info("Entra a armar cotizacion para API Envia");
        return Cotizacion.builder().
                opciones(
                        Opciones.builder()
                                .tipoEnvio(cotizacion.getOpciones().getTipoEnvio())
                                .tipoEntrega(cotizacion.getOpciones().getTipoEntrega())
                        .build()
                ).
                origen(
                        Origen.builder()
                        .domicilio(
                                Domicilio.builder().
                                        codigoPostal(cotizacion.getOrigen().getDomicilio().getCodigoPostal())
                                .build()
                        )
                        .build()
                ).
                destino(
                        Destino.builder()
                        .domicilio(
                                Domicilio.builder().
                                        codigoPostal(cotizacion.getDestino().getDomicilio().getCodigoPostal())
                                .build()
                        ).build()
                ).
                detalle(cotizacion.getDetalle())
                .build();
    }

}
