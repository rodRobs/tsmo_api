package com.mx.tsmo.envios.service;

import com.mx.tsmo.envios.model.domain.Cancelacion;
import com.mx.tsmo.envios.model.domain.Envio;
import com.mx.tsmo.envios.model.dto.PostCancelacion;

public interface CancelacionService {

    Cancelacion guardar(PostCancelacion postCancelacion, Envio envio, String mensaje);
    Cancelacion cancelacion(Envio envio, PostCancelacion postCancelacion);
    Cancelacion cancelacionTSMO(Envio envio, PostCancelacion postCancelacion);
    Cancelacion cancelacionENVIA(Envio envio, PostCancelacion postCancelacion);
    Cancelacion buscarPorEnvio(Envio envio);

}
