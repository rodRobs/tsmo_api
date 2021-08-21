package com.mx.tsmo.etiqueta.controller;

import com.mx.tsmo.envios.model.domain.Envio;
import com.mx.tsmo.envios.service.EnvioService;
import com.mx.tsmo.etiqueta.domain.dto.EtiquetaPost;
import com.mx.tsmo.etiqueta.service.EtiquetaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("etiquetas")
@CrossOrigin("*")
@Slf4j
public class EtiquetaController {

    @Autowired
    private EtiquetaService etiquetaService;

    @Autowired
    private EnvioService envioService;

    /*
     * SERVICIO REST PARA CREAR DOCUMENTO DE GUIA
     * Método para crear documento de guia para imprimir
     * @author Rodrigo Robles
     * @param guia String, Numero de guia de tipo string que se desea imprimir
     * @return Archivo PDF
     * */
    @PostMapping("/imprimir/proveedor")
    public ResponseEntity<byte[]> imprimirGuia(@RequestBody EtiquetaPost etiquetaPost) {
        log.info("Ingresa a controlador etiqueta para guia Envia: "+etiquetaPost.getGuia());
        if (etiquetaPost.getGuia() == null) {
            String msg = "ERROR: Guia es NULL";
            log.info(msg);
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
        Envio envioBD = envioService.buscarPorGuiaProveedor(etiquetaPost.getGuia());
        if (envioBD == null) {
            String msg = "No hay envio con ese numero de guia";
            log.info(msg);
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
        byte[] guiaPDF = etiquetaService.etiquetaEnvia(etiquetaPost);
        if (guiaPDF == null) {
            String msg = "ERROR: Ocurrio un error en crear el archivo pdf";
            log.error(msg);
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
        String fileName = envioBD.getGuiaTsmo() + "_" + envioBD.getGuiaProveedor() + ".pdf";
        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentLength(guiaPDF.length);
        respHeaders.setContentType(MediaType.APPLICATION_PDF);
        respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        return new ResponseEntity<byte[]>(guiaPDF, respHeaders, HttpStatus.OK);
    }

    @GetMapping("/imprimir/{guia}")
    public ResponseEntity<byte[]> impGuia(@PathVariable("guia") String guiaProveedor) {
        log.info("Ingresa a controlador etiqueta para guia Envia: " + guiaProveedor);
        if (guiaProveedor == null) {
            String msg = "ERROR: Guia es NULL";
            log.info(msg);
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
        Envio envioBD = envioService.buscarPorGuiaProveedor(guiaProveedor);
        if (envioBD == null) {
            String msg = "No hay envio con ese numero de guia";
            log.info(msg);
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
        EtiquetaPost etiquetaPost = new EtiquetaPost(guiaProveedor, 2);
        byte[] guiaPDF = etiquetaService.etiquetaEnvia(etiquetaPost);
        if (guiaPDF == null) {
            String msg = "ERROR: Ocurrio un error en crear el archivo pdf";
            log.error(msg);
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
        String fileName = envioBD.getGuiaTsmo() + "_" + envioBD.getGuiaProveedor() + ".pdf";
        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentLength(guiaPDF.length);
        respHeaders.setContentType(MediaType.APPLICATION_PDF);
        respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        return new ResponseEntity<byte[]>(guiaPDF, respHeaders, HttpStatus.OK);
    }


}
