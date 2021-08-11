package com.mx.tsmo.cotizacion.controller;

import com.mx.tsmo.cotizacion.model.domain.CPTsmo;
import com.mx.tsmo.cotizacion.model.dto.CPDto;
import com.mx.tsmo.cotizacion.service.CPTsmoService;
import com.mx.tsmo.cotizacion.service.DomicilioService;
import com.mx.tsmo.documentacion.model.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("domicilio")
@CrossOrigin("*")
@Slf4j
public class DomicilioController {

    @Autowired
    private DomicilioService domicilioService;

    @Autowired
    private CPTsmoService cpTsmoService;

    /*
     * METODO PARA BUSCAR TODOS LOS ORIGENES REALIZADOS
     * Servicio REST para listar todos los origenes realizados
     * @author Rodrigo Robles
     * @return origenes List<String>, lista de todas las ciudades en origenes
     * */
    @GetMapping("/ciudades")
    public ResponseEntity<List<String>> origenes() {
        log.info("Entra a servicio para listar todos las ciudades de origen");
        return ResponseEntity.ok(domicilioService.listarCiudades());
    }

    /*
    * METODO PARA BUSCAR LOS ASENTAMIENTOS DE ESE CODIGO POSTAL
    * Servicio REST para listar asentamiento correspondientes a un codigo postal
    * @author Rodrigo Robles
    * @param String cp
    * @return List<CPTsmo> asentamientos
    * */
    @GetMapping("/cp/{cp}")
    public ResponseEntity<CPDto> asentamientos(@PathVariable("cp") String cp) {
        log.info("Entra a servicio para listar asentamientos del codigo postal: "+cp);
        List<CPTsmo> codigos_postales = cpTsmoService.buscarCP(Integer.parseInt(cp));
        log.info("Encontro " + codigos_postales.size() + " asentamientos");
        if (codigos_postales.size() > 0) {
            List<String> asentamientos = new ArrayList<>();
            for (CPTsmo asentamiento : codigos_postales) {
                // log.info("Asentamiento: "+asentamiento.getAsentamiento());
                asentamientos.add(asentamiento.getAsentamiento());
            }
            CPDto cpDto = new CPDto(cp, asentamientos, codigos_postales.get(0).getTipo(), codigos_postales.get(0).getMunicipio(), codigos_postales.get(0).getEstado(), codigos_postales.get(0).getCiudad(), "México");
            return ResponseEntity.ok(cpDto);
        } else {
            return new ResponseEntity("ERROR: No existe ese codigo postal, volver a ingresar codigo postal valido", HttpStatus.BAD_REQUEST);
        }
    }

}
