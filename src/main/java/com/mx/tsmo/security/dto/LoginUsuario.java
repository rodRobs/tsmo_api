package com.mx.tsmo.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data @AllArgsConstructor @Builder
public class LoginUsuario {
    @NotBlank
    private String nombreUsuario;
    @NotBlank
    private String password;


}
