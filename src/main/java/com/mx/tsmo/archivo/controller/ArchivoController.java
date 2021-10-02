package com.mx.tsmo.archivo.controller;

import com.mx.tsmo.archivo.model.dto.ArchivoDto;
import com.mx.tsmo.archivo.model.dto.MasivosResponseDto;
import com.mx.tsmo.archivo.service.ArchivoService;
import com.mx.tsmo.cotizacion.service.CotizacionService;
import com.mx.tsmo.envios.model.domain.Envio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@Slf4j
@CrossOrigin("*")
@RequestMapping("archivo")
public class ArchivoController {

    @Autowired
    private ArchivoService archivoService;

    @Autowired
    private CotizacionService cotizacionService;

    @PostMapping("/{cliente}")
    public ResponseEntity<MasivosResponseDto> subirArchivo(@RequestParam(value = "pedidos") MultipartFile pedido, @PathVariable("cliente") String cliente) {
        log.info("Entra a servicio de controlador archivo para cargar excel");
        if (pedido == null || pedido.isEmpty()) {
            log.error("Pedido esta vacio");
            return new ResponseEntity("Archivo esta vacio", HttpStatus.BAD_REQUEST);
        }
        log.info("Archivo trae información");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());
        StringBuilder builder = new StringBuilder();
        builder.append(currentDateTime);
        builder.append("_pedido_excel_cliente_");
        builder.append(cliente);
        builder.append(".xlsx");
        try {
            byte[] fileBytes = pedido.getBytes();
            Path path = Paths.get(builder.toString());
            Files.write(path, fileBytes);

        } catch(IOException ioException) {
            log.error("Error: error al obtener los bytes del archivo");
            log.error(ioException.getMessage());
            ioException.printStackTrace();
            return new ResponseEntity("ERROR: Hubo error en el documento", HttpStatus.BAD_REQUEST);
        }
        //archivoService.leerArchivoExcel(archivoService.file(builder.toString()));
        //ArchivoDto archivoDto = new ArchivoDto();
        List<Envio> envios = archivoService.obtenerEnviosExcel(archivoService.file(builder.toString()));
        if (envios == null || envios.isEmpty()) {
            return new ResponseEntity("ERROR: No se pudo obtener información del Excel", HttpStatus.BAD_REQUEST);
        }
        envios = cotizacionService.cotizaciones(envios);
        return ResponseEntity.ok(archivoService.filtrar(envios));
    }

    @GetMapping
    public ResponseEntity<String> prueba() {
        log.info("Servicio de controlador de prueba");
        return ResponseEntity.ok("Exito");
    }

    /*@PostMapping("/{cliente}")
    public ResponseEntity<String> pruebaSubirArchivo(@RequestParam(value = "pedidos") MultipartFile pedido, @PathVariable("cliente") String cliente) {
        log.info("Entra a servicio de controlador archivo para cargar excel");
        if (pedido == null || pedido.isEmpty()) {
            log.error("Pedido esta vacio");
            return new ResponseEntity("Archivo esta vacio", HttpStatus.BAD_REQUEST);
        }
        log.info("Archivo trae información");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());
        StringBuilder builder = new StringBuilder();
        builder.append(currentDateTime);
        builder.append("_pedido_excel_cliente_");
        builder.append(cliente);
        builder.append(".xlsx");
        try {
            byte[] fileBytes = pedido.getBytes();
            Path path = Paths.get(builder.toString());
            Files.write(path, fileBytes);

        } catch(IOException ioException) {
            log.error("Error: error al obtener los bytes del archivo");
            log.error(ioException.getMessage());
            ioException.printStackTrace();
            return new ResponseEntity("ERROR: Hubo error en el documento", HttpStatus.BAD_REQUEST);
        }
        archivoService.leerArchivoExcel(archivoService.file(builder.toString()));
        //ArchivoDto archivoDto = new ArchivoDto();

        return ResponseEntity.ok("Exito");
    }*/

}
