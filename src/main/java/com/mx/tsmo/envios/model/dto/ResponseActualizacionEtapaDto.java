package com.mx.tsmo.envios.model.dto;

import com.mx.tsmo.envios.model.domain.Envio;
import com.mx.tsmo.envios.model.domain.Rastreo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ResponseActualizacionEtapaDto {

    private Set<String> enviosEncontrados;
    private Set<String> enviosNoEncontrados;
    private Set<Rastreo> rastreosAlmacenados;
    private Set<Rastreo> rastreosNoAlmacenados;

}
