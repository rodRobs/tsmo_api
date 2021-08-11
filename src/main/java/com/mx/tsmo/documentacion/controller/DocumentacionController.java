package com.mx.tsmo.documentacion.controller;

import com.mx.tsmo.cotizacion.model.domain.Cotizacion;
import com.mx.tsmo.documentacion.model.dto.Documentacion;
import com.mx.tsmo.documentacion.model.dto.Guia;
import com.mx.tsmo.documentacion.model.dto.Response;
import com.mx.tsmo.documentacion.service.DocumentacionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("documentacion")
@CrossOrigin("*")
@Slf4j
public class DocumentacionController {

    @Autowired
    private DocumentacionService documentacionService;

    @PostMapping("/{paqueteria}/{cotizacion}")
    public ResponseEntity<com.mx.tsmo.documentacion.model.domain.Documentacion> predocumentacion(@RequestBody Documentacion documentacion, @PathVariable("paqueteria") String paqueteria, @PathVariable("cotizacion") Long cotizacion) {
        log.info("Entra a servicio para predocumentacion: ");
        log.info("Doc: "+documentacion.toString());
        com.mx.tsmo.documentacion.model.domain.Documentacion documentacionBD = documentacionService.guardar(this.cambiar(documentacion, cotizacion));
        if (documentacionBD == null) {
            log.error("");
            return new ResponseEntity("ERROR: No se puedo almacenar la documentacion en BD", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(documentacionBD);
        /*
        log.info(documentacion.toString());
        String guia = documentacionService.preDocumentacion(documentacion);
        Response response = Response.builder().idDocumentacion(documentacionBD.getId()).guia(guia).build();
        return ResponseEntity.ok(response);
         */
    }

    @PostMapping("/proveedor")
    public ResponseEntity<String> documentacionConProveedor(@RequestBody Documentacion documentacion) {
        log.info("Entra a servicio para documentacion con proveedor: ENVIA");
        log.info(documentacion.toString());
        Guia guia = documentacionService.preDocumentacion(documentacion);
        if (guia == null) {
            log.error("ERROR");
            return new ResponseEntity("ERROR: No se pudo generar el número de guía con el proveedor", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(guia.getGuia());
    }

    public com.mx.tsmo.documentacion.model.domain.Documentacion cambiar(Documentacion documentacion, Long cotizacion) {
        return com.mx.tsmo.documentacion.model.domain.Documentacion.builder()
                .cotizacion(Cotizacion.builder().id(cotizacion).build())
                .contiene(documentacion.getContiene())
                .createAt(new Date())
                .build();
    }

    @GetMapping("/timer/prueba")
    public String pruebaTimer() {
        log.info("Entra a controlador documentación prueba Timer");
        documentacionService.pruebaTimer("Hola");
        return "Exito";
    }





}
