package com.mx.tsmo.envios.controller;

import com.mx.tsmo.cotizacion.service.EnviaService;
import com.mx.tsmo.envios.model.domain.Cancelacion;
import com.mx.tsmo.envios.model.domain.Envio;
import com.mx.tsmo.envios.model.dto.PostCancelacion;
import com.mx.tsmo.envios.service.CancelacionService;
import com.mx.tsmo.envios.service.EnvioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cancelacion")
@Slf4j
@CrossOrigin("*")
public class CancelacionController {

    @Autowired
    private EnvioService envioService;

    @Autowired
    private EnviaService enviaService;

    @Autowired
    private CancelacionService cancelacionService;

    @PostMapping
    public ResponseEntity<Cancelacion> cancelarEnvio(@RequestBody PostCancelacion postCancelacion) {
        log.info("Entra a controlador para cancelar pedido");
        Envio envio = envioService.buscarPorGuiaTsmo(postCancelacion.getGuia());
        if (envio == null) {
            log.info("No existe envio con ese número de guia");
            return new ResponseEntity("ERROR: No existe envío con ese número de guía, favor de verificar el número de guía", HttpStatus.BAD_REQUEST);
        }
        Cancelacion cancelacionBD = cancelacionService.cancelacion(envio, postCancelacion);
        if (cancelacionBD == null) {
            log.info("ERROR: No se puedo cancelar");
            return new ResponseEntity("ERROR: No se pudo cancelar el envio con ese número de guía", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(cancelacionBD);
    }



}
