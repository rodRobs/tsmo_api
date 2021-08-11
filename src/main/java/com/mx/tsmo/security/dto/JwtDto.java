package com.mx.tsmo.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data @AllArgsConstructor @Builder
public class JwtDto {
    private String token;
    private String bearer = "Bearer";
    private String nombreUsuario;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtDto(String token, String nombreUsuario, Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.nombreUsuario = nombreUsuario;
        this.authorities = authorities;
    }
}
