package com.mx.tsmo.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data @AllArgsConstructor
public class NuevoUsuario {
    @NotBlank
    private String nombre;
    @NotBlank
    private String nombreUsuario;
    @Email
    private String email;
    @NotBlank
    private String password;
    private Set<String> roles = new HashSet<>();

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }


    public String toString() {
        String rolString = "";
        for (String rol: roles) {
            rolString += "Rol: " + rol + "\n";
        };
        return "Nombre: " + nombre +
                "NombreUsuario: " + nombreUsuario +
                "Email: " + email +
                "Password: " + password +
                "Roles: " + rolString;
    }
}
