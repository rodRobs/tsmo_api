package com.mx.tsmo.documentacion.timertask;

import com.mx.tsmo.envios.model.domain.Rastreo;
import com.mx.tsmo.envios.service.RastreoService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

@Data
@Slf4j
public class RastreoEnviaTask extends TimerTask {

    private String guia;
    private int contador;

    @Autowired
    private RastreoService rastreoService;


    @Override
    public void run() {
        log.info("Entra a peticion para solicitar rastreo de la guia: "+guia);
        contador++;

        log.info("",new Date());

        if (contador == 5) {
            log.info("Entra a cancelar tarea");
            this.cancel();
        }
    }

    public RastreoEnviaTask(String guia) {
        this.guia = guia;
    }

    public List<Rastreo> listarRastreos() {
        return null;
    }

}
