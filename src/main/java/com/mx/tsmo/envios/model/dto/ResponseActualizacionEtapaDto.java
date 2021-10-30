package com.mx.tsmo.envios.model.dto;

import com.mx.tsmo.envios.model.domain.Envio;
import com.mx.tsmo.envios.model.domain.Rastreo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ResponseActualizacionEtapaDto {

    private List<Envio> enviosEncontrados;
    private List<String> enviosNoEncontrados;
    private List<Rastreo> rastreosAlmacenados;
    private List<Rastreo> rastreosNoAlmacenados;

}
