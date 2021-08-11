package com.mx.tsmo.envios.service;

import com.mx.tsmo.enums.EnviaAuth;
import com.mx.tsmo.envios.model.dao.RastreoDao;
import com.mx.tsmo.envios.model.domain.Envio;
import com.mx.tsmo.envios.model.domain.Rastreo;
import com.mx.tsmo.envios.model.dto.Movimientos;
import com.mx.tsmo.envios.model.dto.PostRastreo;
import com.mx.tsmo.envios.model.dto.Referencia;
import com.mx.tsmo.envios.model.dto.ResponseRastreo;
import com.mx.tsmo.envios.model.enums.DescripcionRastreos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class RastreoServiceImpl implements RastreoService {

    private static final String ENVIA = "ENVIA";
    private static final String TSMO = "TSMO";

    @Autowired
    private RastreoDao rastreoDao;

    private RastreoEnviaServiceImpl rastreoEnviaService = new RastreoEnviaServiceImpl();

    @Override
    public Rastreo guardar(Rastreo rastreo) {
        rastreo.setCreateAt(new Date());
        try {
            return rastreoDao.save(rastreo);
        } catch(IllegalStateException ie) {
            log.info("Error IE: " + ie.getMessage());
            return null;
        }
    }

    @Override
    public List<Rastreo> rastrear(Envio envio) {
        log.info("Entra a servicio para regresar rastreos o movimientos");

        if (envio.getDocumentacion().getCotizacion().getRealiza().equalsIgnoreCase(TSMO)) {
            return envio.getRastreos();
        } else if (envio.getDocumentacion().getCotizacion().getRealiza().equalsIgnoreCase(ENVIA)) {
            // PostRastreo postRastreo = PostRastreo.builder().guia(envio.getGuiaProveedor()).referencia(Referencia.builder().cuenta(Integer.parseInt(EnviaAuth.CLIENTE.toString())).referencia1(null).referencia2(null).build()).build();
            return this.listarRastreosEnvia(envio);
        }
        return null;
    }

    public List<Rastreo> listarRastreosEnvia(Envio envio) {
        List<PostRastreo> postRastreos = new ArrayList<>();
        postRastreos.add(PostRastreo.builder().guia(envio.getGuiaProveedor()).referencia(Referencia.builder().cuenta(Integer.parseInt(EnviaAuth.CLIENTE.toString())).referencia1(null).referencia2(null).build()).build());
        ResponseRastreo[] responseRastreos = rastreoEnviaService.rastreo(postRastreos);
        //responseRastreo.getMovimientos();
        List<Rastreo> rastreos = new ArrayList<>();
        for (ResponseRastreo responseRastreo : responseRastreos) {
            for (Movimientos movimiento : responseRastreo.getMovimientos()) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SSZ");
                try {
                    Date fechaMovimiento = formatter.parse(movimiento.getFMovimiento());
                    rastreos.add(Rastreo.builder().nombre(movimiento.getCodigo()).createAt(fechaMovimiento).descripcion(movimiento.getMovimiento()).build());
                } catch (ParseException e) {
                    log.info("ERROR___ ParseException: " + e.getMessage());
                }

            }
        }
        return rastreos;
    }

    @Override
    public Rastreo verificarEtapa(int etapa) {
        Rastreo rastreo = new Rastreo();
        switch(etapa) {
            case 1: // Recolección
                rastreo = Rastreo.builder()
                        .descripcion(DescripcionRastreos.RECOLECCION.toString())
                        .nombre("RECOLECCIÓN")
                        .build();
                break;
            case 2: // Almacén
                rastreo = Rastreo.builder()
                        .descripcion(DescripcionRastreos.ALMACEN.toString())
                        .nombre("ALMACÉN")
                        .build();
                break;
            case 3: // En Tránsito
                rastreo = Rastreo.builder()
                        .descripcion(DescripcionRastreos.TRANSITO.toString())
                        .nombre("EN TRÁNSITO")
                        .build();
                break;
            case 4: // Entregado
                rastreo = Rastreo.builder()
                        .descripcion(DescripcionRastreos.ENTREGADO.toString())
                        .nombre("ENTREGADO")
                        .build();
                break;
            case 5: // 1era Entrega sin éxito
                rastreo = Rastreo.builder()
                        .descripcion(DescripcionRastreos.ENTREGA_PRIMERA_SIN_EXITO.toString())
                        .nombre("1ERA ENTREGA SIN ÉXITO")
                        .build();
                break;
            case 6: // 2da Entrega sin éxito
                rastreo = Rastreo.builder()
                        .descripcion(DescripcionRastreos.ENTREGA_SEGUNDA_SIN_EXITO.toString())
                        .nombre("2DA ENTREGA SIN ÉXITO")
                        .build();
                break;
            case 7: // 3era Entrega sin éxito
                rastreo = Rastreo.builder()
                        .descripcion(DescripcionRastreos.ENTREGA_TERCERA_SIN_EXITO.toString())
                        .nombre("3ERA ENTREGA SIN ÉXITO")
                        .build();
                break;
            case 8: // Devuelto
                rastreo = Rastreo.builder()
                        .descripcion(DescripcionRastreos.DEVUELTO.toString())
                        .nombre("DEVUELTO")
                        .build();
                break;
            case 9: // Cancelado
                rastreo = Rastreo.builder()
                        .descripcion(DescripcionRastreos.CANCELADO.toString())
                        .nombre("CANCELADO")
                        .build();
                break;
        }
        return rastreo;
    }
}
