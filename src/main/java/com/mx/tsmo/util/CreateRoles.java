package com.mx.tsmo.util;

import com.mx.tsmo.security.entity.Rol;
import com.mx.tsmo.security.enums.RolNombre;
import com.mx.tsmo.security.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 * MUY IMPORTANTE: ESTA CLASE SÓLO SE EJECUTARÁ UNA VEZ PARA CREAR LOS ROLES.
 * UNA VEZ CREADOS SE DEBERÁ ELIMINAR O BIEN COMENTAR EL CÓDIGO
 *
 */

@Component
public class CreateRoles implements CommandLineRunner {

    @Autowired
    RolService rolService;

    @Override
    public void run(String... args) throws Exception {

        /*
        Rol rolAdmin = new Rol(RolNombre.ROLE_ADMIN);
        Rol rolUser = new Rol(RolNombre.ROLE_USER);
        Rol rolOperador = new Rol(RolNombre.ROL_OPERADOR);
        Rol rolCLiente = new Rol(RolNombre.ROL_CLIENTE);
        Rol rolTsmo = new Rol(RolNombre.ROL_TSMO);
        rolService.save(rolAdmin);
        rolService.save(rolUser);
        rolService.save(rolOperador);
        rolService.save(rolCLiente);
         */
    }

}
