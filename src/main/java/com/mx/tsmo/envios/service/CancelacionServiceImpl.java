package com.mx.tsmo.envios.service;

import com.mx.tsmo.cotizacion.service.EnviaService;
import com.mx.tsmo.envios.model.dao.CancelacionDao;
import com.mx.tsmo.envios.model.domain.Cancelacion;
import com.mx.tsmo.envios.model.domain.Envio;
import com.mx.tsmo.envios.model.dto.PostCancelacion;
import com.mx.tsmo.envios.model.dto.ResponseCancelacion;
import com.mx.tsmo.envios.model.enums.EstadoEnvio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@Slf4j
public class CancelacionServiceImpl implements CancelacionService {

    private static final String EXITO = "Cancelación exitosa";

    @Autowired
    private CancelacionDao cancelacionDao;

    @Autowired
    private EnviaService enviaService;

    @Autowired
    private EnvioService envioService;


    @Override
    public Cancelacion guardar(PostCancelacion postCancelacion, Envio envio, String mensaje){
        log.info("Entra a servicio para guardar canelacion");
        Cancelacion cancelacion = Cancelacion.builder().envio(envio).comentario(postCancelacion.getComentario()).mensaje(mensaje).createAt(new Date()).build();
        return cancelacionDao.save(cancelacion);
    }

    @Override
    public Cancelacion cancelacion(Envio envio, PostCancelacion postCancelacion) {
        log.info("Entra a servicio para cancelar envio");
        Cancelacion cancelacion = new Cancelacion();
        if (envio.getDocumentacion().getCotizacion().getRealiza().equalsIgnoreCase("TSMO")) {
            log.info("Cancela para TSMO");
            cancelacion = this.cancelacionTSMO(envio, postCancelacion);
        } else if (envio.getDocumentacion().getCotizacion().getRealiza().equalsIgnoreCase("ENVIA")) {
            log.info("Cancela para ENVIA");
            cancelacion = this.cancelacionENVIA(envio, postCancelacion);
        }
        return cancelacion;
    }

    @Override
    public Cancelacion cancelacionTSMO(Envio envio, PostCancelacion postCancelacion) {
        Cancelacion cancelacion = Cancelacion.builder().comentario(postCancelacion.getComentario()).envio(envio).mensaje(EXITO).createAt(new Date()).build();
        Cancelacion cancelacionBD = cancelacionDao.save(cancelacion);
        if (cancelacionBD == null) {
            log.info("ERROR: No se pudo cancelar, cancelacion");
            return null;
        }
        envio.setEstadoEnvio(EstadoEnvio.CANCELADO.toString());
        Envio envioBD = envioService.actualizarEstadoEnvio(envio);
        if (envioBD == null) {
            log.info("ERROR: No se pudo modificar el estado del envio");
            return null;
        }
        return cancelacionBD;
    }

    @Override
    public Cancelacion cancelacionENVIA(Envio envio, PostCancelacion postCancelacion) {
        ResponseCancelacion responseCancelacion = enviaService.cancelacion(postCancelacion);
        if (responseCancelacion == null) {
            log.error("Error al cancela desde envia");
            return null;
        }
        Cancelacion cancelacion = Cancelacion.builder().mensaje(responseCancelacion.getMsg()).comentario(postCancelacion.getComentario()).envio(envio).build();
        return cancelacionDao.save(cancelacion);
    }

    @Override
    public Cancelacion buscarPorEnvio(Envio envio) {
        Cancelacion cancelacionBD = cancelacionDao.findByEnvio(envio);
        if (cancelacionBD == null) {
            log.error("Cancelacion NULL");
            return null;
        }
        return cancelacionBD;
    }


}
