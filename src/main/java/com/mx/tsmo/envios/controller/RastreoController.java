package com.mx.tsmo.envios.controller;

import com.mx.tsmo.envios.model.domain.Cancelacion;
import com.mx.tsmo.envios.model.domain.Envio;
import com.mx.tsmo.envios.model.domain.Rastreo;
import com.mx.tsmo.envios.model.dto.RastreoDto;
import com.mx.tsmo.envios.model.enums.EstadoEnvio;
import com.mx.tsmo.envios.model.enums.EstadoPago;
import com.mx.tsmo.envios.service.CancelacionService;
import com.mx.tsmo.envios.service.EnvioService;
import com.mx.tsmo.envios.service.RastreoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("rastreo")
@Slf4j
@CrossOrigin("*")
public class RastreoController {

    @Autowired
    private EnvioService envioService;

    @Autowired
    private RastreoService rastreoService;

    @Autowired
    private CancelacionService cancelacionService;

    @GetMapping("/{guia}")
    public ResponseEntity<Envio> rastrearEnvio(@PathVariable("guia") String guia, @RequestParam(value = "cte", required = false) Long cliente) {
        log.info("Entra a controlador para rastrear numero de guía");
        log.info("Cliente: "+cliente);
        Envio envio = envioService.buscarPorGuiaTsmo(guia);
        if (envio == null) {
            log.error("Envio null");
            return new ResponseEntity("ERROR: No existe envío con ese número de guía, favor de verificar su número de guía", HttpStatus.BAD_REQUEST);
        }
        log.info("Envio se ha encontrado con id: "+envio.getId());
        if (envio.getEstadoEnvio() != null) {
            if (envio.getEstadoEnvio().equalsIgnoreCase(EstadoEnvio.CANCELADO.toString()) && envio.getEstadoPago().equalsIgnoreCase(EstadoPago.RECHAZADO.toString())) {
                log.info("El envio fue cancelado desde antes de realizar el pago");
                return  new ResponseEntity("ERROR: No se pudo completar la realización de la documentación del envío con éxito", HttpStatus.BAD_REQUEST);
            }
        }
        log.info("EL envio no ha sido cancelado");
        if (envio.getEstadoEnvio() != null) {
            if (envio.getEstadoEnvio().equalsIgnoreCase(EstadoEnvio.CANCELADO.toString())) {
                log.info("El envío con ese número de guía ha sido cancelado");
                Cancelacion cancelacionBD = cancelacionService.buscarPorEnvio(envio);
                return new ResponseEntity("ERROR: El envio fue cancelado con fecha y hora siguientes: " + cancelacionBD.getCreateAt() + " y comentario de cancelación siguiente: " + cancelacionBD.getComentario(), HttpStatus.BAD_REQUEST);
            }
        }
        log.info("Regresamos envio");
        if (cliente != null) {
            String msg = "ERROR: No se puede mostrar información del envío";
            log.info("Busca por cliente: "+cliente);
            if (envio.getUsuario() != null) {
                log.info("Usuario en envio es diferente a cero");
                if (envio.getCliente().getId() != cliente) {
                    log.info("");
                    return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
            }
        }
        return ResponseEntity.ok(envio);
    }

    @PostMapping("/actualizar/{envio}")
    public ResponseEntity<Rastreo> actualizarRastreo(@RequestBody Rastreo rastreo, @PathVariable("envio") Long envio) {
        log.info("Entra a controlador para actualizar rastreo");
        Envio envioBD = envioService.actualizarEtapa(Envio.builder().id(envio).etapa(rastreo.getEtapa()).build());
        if (envioBD == null) {
            log.error("Envio null");
            return new ResponseEntity("ERROR: No existe envio con esa guía", HttpStatus.BAD_REQUEST);
        }
        rastreo.setEnvio(envioBD);
        Rastreo rastreoBD = rastreoService.guardar(rastreo);
        if (rastreoBD == null) {
            return new ResponseEntity("ERROR: No se pudo almacenar el rastreo del envio", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(rastreoBD);
    }

    /*
    * SERVICIO REST PARA ACTUALIZAR RASTREO DE LOS ENVIOS
    * Servicio para actualizar los estados de rastreo
    * @param rastreoDto RastreoDto, variable contiene toda la información correspondiente
    * */
    @PostMapping("/actualizar/rastreo")
    public ResponseEntity<String> actualizarRastreoEnvios(@RequestBody RastreoDto rastreoDto) {
        log.info("Entra a servicio para actualizar usuario");
        List<String> guiasNoEncontradas = new ArrayList<>();
        boolean banderaGuiasNoEncontradas = false;
        for (String guia : rastreoDto.getGuias()) {
            Envio envioBD = envioService.buscarPorGuiaTsmo(guia);
            if (envioBD == null) {
                log.error("Envio null guia: "+guia);
                guiasNoEncontradas.add(guia);
                //return new ResponseEntity("ERROR: No existe envio con esa guía", HttpStatus.BAD_REQUEST);
            } else {
                Rastreo rastreo = rastreoService.verificarEtapa(rastreoDto.getEtapa());
                rastreo.setEtapa(rastreoDto.getEtapa());
                rastreo.setEstado(rastreoDto.getEstado());
                rastreo.setMunicipio(rastreoDto.getMunicipio());
                rastreo.setPais(rastreoDto.getPais());
                rastreo.setUbicacion(rastreoDto.getMunicipio() + ", " + rastreoDto.getEstado() + ", " + rastreoDto.getPais());
                rastreo.setEnvio(envioBD);
                rastreo.setLongitud(rastreoDto.getLongitud());
                rastreo.setLatitud(rastreoDto.getLatitud());
                Rastreo rastreoBD = rastreoService.guardar(rastreo);
                if (rastreoBD == null) {
                    // return new ResponseEntity("ERROR: No se pudo almacenar el rastreo del envio", HttpStatus.BAD_REQUEST);
                }
            }
        }
        if (banderaGuiasNoEncontradas) {
            String listaGuias = "";
            for (String guia : guiasNoEncontradas) {
                listaGuias = listaGuias + "[ " + guia + " ]";
            }
            return new ResponseEntity("ERROR: No se encontraron los siguientes números de guías para actualizar el estado del envio: " + listaGuias, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Se han actualizado los estados de los envios con éxito");
    }
}
