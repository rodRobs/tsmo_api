package com.mx.tsmo.envios.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ActualizacionEtapaDto {

    public int etapa;
    public String latitud;
    public String longitud;
    public List<String> guias;
    public String descripcion;

}
