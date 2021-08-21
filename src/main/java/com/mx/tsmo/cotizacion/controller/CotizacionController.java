package com.mx.tsmo.cotizacion.controller;

import com.mx.tsmo.cotizacion.model.domain.Costo;
import com.mx.tsmo.cotizacion.model.domain.Cotizacion;
import com.mx.tsmo.cotizacion.service.*;
import com.mx.tsmo.enums.TipoTraslado;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import com.google.gson.Gson;

@Controller
@RequestMapping("cotizacion")
@CrossOrigin("*")
@Slf4j
public class CotizacionController {

    @Autowired
    private CotizacionService cotizacionService;

    @Autowired
    private CostoService costoService;

    @Autowired
    private EnviaService enviaService;

    @Autowired
    private CPTsmoService cpTsmoService;

    @Autowired
    private CoberturaTSMOService coberturaTSMOService;

    @PostMapping
    public ResponseEntity<Costo> cotizar(@RequestBody Cotizacion cotizacion) {
        log.info("Entra a controlador para cotizar envio Regresa Costo");
        // Costo costo = cotizacionService.cotizacion(cotizacion);

        return this.realizarCotizacion(cotizacion);
    }

    @PostMapping("clientes")
    public ResponseEntity<Costo> cotizarCliente(@RequestBody Cotizacion cotizacion) {
        log.info("Entra a servicio de controlador cotizar para cliente: bueno");
        //cotizacion.setTipoServicio("S");
        //log.info("Costo: "+cotizacion.getOrigen().getTelefonos().get(0).toString());
        /*
        Costo costo = cotizacionService.cotizacion(cotizacion);
        if (costo == null) {
            return new ResponseEntity("Error al realizar la cotizacion", HttpStatus.BAD_REQUEST);
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        cotizacion.setRealiza(costo.getRealiza());
        log.info("Cotizacion: "+cotizacion.toString());
        Cotizacion cotizacionBD = cotizacionService.guardar(cotizacion);
        costo.setCotizacion(cotizacionBD);
        Costo costoBD = costoService.guardar(costo);
        return ResponseEntity.ok(costoBD);
    }
         */
        return this.realizarCotizacion(cotizacion);
    }

    @PostMapping("envio")
    public ResponseEntity<Costo> cotizarParaEnvio(@RequestBody Cotizacion cotizacion) {
        log.info("Entra a servicio de controlador cotizar para envios");
        log.info(cotizacion.getOrigen().getDomicilio().getCodigoPostal());
        if (cotizacion.getOrigen().getDomicilio().getCodigoPostal() == null || cotizacion.getOrigen().getDomicilio().getCodigoPostal().equalsIgnoreCase("")) {
            log.info("Es null");
            return new ResponseEntity("ERROR: No hay información ingresada", HttpStatus.BAD_REQUEST);
        }
        /*
        Costo costo = cotizacionService.cotizacion(cotizacion);
        if (costo == null) {
            return new ResponseEntity("ERROR: Al cotizar precio de envío", HttpStatus.BAD_REQUEST);
        }
        cotizacion.setRealiza(costo.getRealiza());
        log.info("Cotizacion: "+cotizacion.toString());
        Cotizacion cotizacionBD = cotizacionService.guardar(cotizacion);
        costo.setCotizacion(cotizacionBD);
        Costo costoBD = costoService.guardar(costo);
        return ResponseEntity.ok(costoBD);
    }
        */
        return this.realizarCotizacion(cotizacion);
    }

    @GetMapping("buscar/{cotizacion}")
    public ResponseEntity<Cotizacion> buscarCotizacion(@PathVariable("cotizacion") Long cotizacion) {
        log.info("Entra a controlador para buscar cotizacion por id de prueba");
        return ResponseEntity.ok(cotizacionService.buscarPorId(cotizacion));
    }

    @GetMapping("buscar/costos/{cotizacion}")
    public ResponseEntity<Costo> buscarCostos(@PathVariable("cotizacion") Long cotizacion) {
        log.info("Entra a controlador para buscar costo por id de cotizacion");
        return ResponseEntity.ok(costoService.listarPorCotizacion(Cotizacion.builder().id(cotizacion).build()));
    }

    /*
    * Funcion para realizar cotizacion local o foranea
    * Almacena Costo en BD y regresa costo en caso de ser exitoso la cotizacion
    * Regresa mensaje de Error caso contrario
    * */
    private ResponseEntity<Costo> realizarCotizacion(Cotizacion cotizacion) {
        Costo costo = new Costo();
        // double distancia = cotizacionService.getDistancia(cotizacionService.URL_Google_Cotizacion(cotizacion));
        //int tipoDistancia = cotizacionService.getTipoDistancia(distancia, cotizacion);

        double peso = cotizacionService.getPeso(cotizacion.getDetalle().get(0));
        int tipoCarga = cotizacionService.getTipoCarga(peso);
        if (coberturaTSMOService.local(cotizacion.getOrigen().getDomicilio().getCodigoPostal(), cotizacion.getDestino().getDomicilio().getCodigoPostal())) {
            log.info("Cotizacion Local");
            costo = cotizacionService.seleccionarServicioCosto(tipoCarga,(int) peso);
            costo.setTipoServicio("ESTÁNDAR NACIONAL");
            costo.setFCompromisoEntrega("1 a 4 días hábiles");
            costo.setRealiza("TSMO");
        } else {
            log.info("Cotizacion Foranea");
            Response post = enviaService.calcularEnvia(cotizacion, tipoCarga);
            String responseJson = post.readEntity(String.class);
            log.info("Response Json: "+responseJson);
            log.info("Estatus: " + post.getStatus());
            switch (post.getStatus()) {
                case 200:
                    log.info("Status: 200");
                    Gson respuestaJson = new Gson();
                    Costo[] cotizacionRes = respuestaJson.fromJson(responseJson, Costo[].class);
                    for (Costo cotizacionResponse : cotizacionRes) {
                        log.info(cotizacionResponse.toString());
                        costo = cotizacionResponse;
                        costo.setCostoTotal(cotizacionService.calculoCostoFinal(cotizacion, costo.getTotal()));
                        costo.setRealiza("ENVIA");
                        // costo.setFCompromisoEntrega(cotizacionResponse.getCompromisoEntrega());
                    }
                    break;
                case 500:
                    log.info("ResJson: " + responseJson);
                    return new ResponseEntity(responseJson, HttpStatus.BAD_REQUEST);
            }
        }
        if (costo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        cotizacion.setRealiza(costo.getRealiza());
        log.info("Cotizacion: "+cotizacion.toString());
        Cotizacion cotizacionBD = cotizacionService.guardar(cotizacion);
        costo.setCotizacion(cotizacionBD);
        Costo costoBD = costoService.guardar(costo);

        return ResponseEntity.ok(costo);


        /*
        double distancia = cotizacionService.getDistancia(cotizacionService.URL_Google_Cotizacion(cotizacion));
        int tipoDistancia = cotizacionService.getTipoDistancia(distancia, cotizacion);
        if (tipoDistancia == TipoTraslado.LOCAL.getValue()) {
            log.info("Cotizacion Local");
            double peso = cotizacionService.getPeso(cotizacion.getDetalle().get(0));
            int tipoCarga = cotizacionService.getTipoCarga(peso);
            costo = cotizacionService.seleccionarServicioCosto(tipoCarga, tipoDistancia,(int) peso,(int) distancia);
            costo.setTipoServicio("1 a 4 días hábiles");
            costo.setRealiza("TSMO");
            //return ResponseEntity.ok(costo);
        } else {
            log.info("Cotizacion Foranea");
            Response post = enviaService.calcularEnvia(cotizacion);
            String responseJson = post.readEntity(String.class);
            log.info("Estatus: " + post.getStatus());
            switch (post.getStatus()) {
                case 200:
                    log.info("Status: 200");
                    Gson respuestaJson = new Gson();
                    Costo[] cotizacionRes = respuestaJson.fromJson(responseJson, Costo[].class);
                    for (Costo cotizacionResponse : cotizacionRes) {
                        log.info(cotizacionResponse.toString());
                        costo = cotizacionResponse;
                        costo.setCostoTotal(costo.getTotal()*1.3);
                        costo.setRealiza("ENVIA");
                    }
                    break;
                case 500:
                    log.info("ResJson: " + responseJson);
                    return new ResponseEntity(responseJson, HttpStatus.BAD_REQUEST);
            }
        }
        if (costo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        cotizacion.setRealiza(costo.getRealiza());
        log.info("Cotizacion: "+cotizacion.toString());
        Cotizacion cotizacionBD = cotizacionService.guardar(cotizacion);
        costo.setCotizacion(cotizacionBD);
        Costo costoBD = costoService.guardar(costo);
        return ResponseEntity.ok(costo);*/
    }

    /*
    * Servicio REST para determinar si el envio es local
    * Prueba para determinar si el servicio es local
    * @author Rodrigo Robles
    * @param int cpOrigen
    * @param int cpDestino
    * @return String tipoEnvio
    * */
    @GetMapping("prueba/local/{cpOrigen}/{cpDestino}")
    public ResponseEntity<String> pruebaLocal(@PathVariable("cpOrigen") String cpOrigen, @PathVariable("cpDestino") String cpDestino) {
        log.info("Entra a servicio de prueba para determinar si el envio es local");
        log.info("Origen: "+cpOrigen);
        log.info("Destino: "+cpDestino);
        String tipoEnvio = "";

        if (coberturaTSMOService.local(cpOrigen, cpDestino)) {
            tipoEnvio = "LOCAL";
        } else {
            tipoEnvio = "FORANEO";
        }
        return ResponseEntity.ok(tipoEnvio);
    }

}
