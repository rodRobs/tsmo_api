package com.mx.tsmo.documentacion.model.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class Response {

    private String guia;
    private Long idDocumentacion;

}
