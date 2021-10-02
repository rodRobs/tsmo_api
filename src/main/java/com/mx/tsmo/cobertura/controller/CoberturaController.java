package com.mx.tsmo.cobertura.controller;

import com.google.gson.Gson;
import com.mx.tsmo.cobertura.model.dto.CoberturaPost;
import com.mx.tsmo.cobertura.model.dto.CoberturaResponse;
import com.mx.tsmo.cobertura.service.CoberturaService;
import com.mx.tsmo.cotizacion.model.domain.CoberturaTSMO;
import com.mx.tsmo.cotizacion.model.domain.Costo;
import com.mx.tsmo.cotizacion.service.CoberturaTSMOService;
import com.mx.tsmo.enums.EnviaAuth;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("cobertura")
@CrossOrigin("*")
@Slf4j
public class CoberturaController {

    @Autowired
    private CoberturaService coberturaService;

    @Autowired
    private CoberturaTSMOService coberturaTSMOService;

    @PostMapping
    public ResponseEntity<CoberturaResponse[]> cobertura(@RequestBody CoberturaPost cobertura) {
        log.info("Entra a contoller cobertura");
        if (coberturaTSMOService.local(cobertura.getCpOrigen(), cobertura.getCpDestino())) {
            CoberturaResponse[] responses = {this.armarCoberturaResponseLocal()};
            return ResponseEntity.ok(responses);
        } else {
            return new ResponseEntity("TSMO no tiene cobertura en los códigos postales ingresados.", HttpStatus.BAD_REQUEST);
            // return ResponseEntity.ok(coberturaService.coberturaENVIA(cobertura));
        }
    }

    public CoberturaResponse armarCoberturaResponseLocal() {
        return CoberturaResponse.builder()
                .isDomicilio(true)
                .isOcurre(true)
                .tipoServicio("1 a 4 días hábiles")
                .build();
    }

    @PostMapping("cp")
    public ResponseEntity<Boolean> coberturaCP(@RequestBody String codigoPostal) {
        log.info("Entra a controlador para validar cobertura de un codigo postal");
        if (coberturaTSMOService.existeCobertura(codigoPostal)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }
}
