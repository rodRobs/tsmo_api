package com.mx.tsmo.documentacion.mvc.controller;

import com.mx.tsmo.documentacion.model.dto.Documentacion;
import com.mx.tsmo.documentacion.model.dto.Guia;
import com.mx.tsmo.documentacion.mvc.service.DocumentacionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("documentacionOld")
@CrossOrigin("*")
@Slf4j
public class DocumentacionOldController {

    @Autowired
    private DocumentacionService documentacionService;

    @PostMapping
    public ResponseEntity<Guia> predocumentacion(@RequestBody Documentacion documentacion) {
        log.info("Entra a servicio para predocumentacion: ");
        if (documentacion.getDetalle() == null) {
            log.info("Detalle es vacio");
            return null;
        }
        log.info(documentacion.toString());
        return ResponseEntity.ok(documentacionService.preDocumentacion(documentacion));
    }
}
