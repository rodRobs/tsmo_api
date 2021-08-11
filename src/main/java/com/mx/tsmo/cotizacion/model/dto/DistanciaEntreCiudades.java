package com.mx.tsmo.cotizacion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor @Builder @AllArgsConstructor
public class DistanciaEntreCiudades {

    private List<String> destination_addresses;
    private List<String> origin_addresses;
    private List<Rows> rows;

}
