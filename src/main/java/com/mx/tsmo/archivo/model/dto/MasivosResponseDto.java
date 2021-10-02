package com.mx.tsmo.archivo.model.dto;

import com.mx.tsmo.envios.model.domain.Envio;
import lombok.*;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class MasivosResponseDto {

    List<Envio> exito;
    List<Envio> sinCobertura;
    List<Envio> error;

}
