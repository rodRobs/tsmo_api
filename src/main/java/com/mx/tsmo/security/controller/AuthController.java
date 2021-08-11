package com.mx.tsmo.security.controller;

import com.mx.tsmo.clientes.model.domain.Cliente;
import com.mx.tsmo.correos.controller.NotificacionController;
import com.mx.tsmo.dto.Mensaje;
import com.mx.tsmo.security.dto.JwtDto;
import com.mx.tsmo.security.dto.LoginUsuario;
import com.mx.tsmo.security.dto.NuevoUsuario;
import com.mx.tsmo.security.entity.Rol;
import com.mx.tsmo.security.entity.Usuario;
import com.mx.tsmo.security.entity.UsuarioPrincipal;
import com.mx.tsmo.security.enums.RolNombre;
import com.mx.tsmo.security.jwt.JwtProvider;
import com.mx.tsmo.security.service.RolService;
import com.mx.tsmo.security.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@CrossOrigin
@Slf4j
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    RolService rolService;

    @Autowired
    JwtProvider jwtProvider;

    NotificacionController notificacionController = new NotificacionController();

    @PostMapping("/nuevo")
    public ResponseEntity<Usuario> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult){
        log.info("Nombre: " + nuevoUsuario.getNombre());
        log.info("Correo: " + nuevoUsuario.getEmail());
        log.info("Nombre usuario: " + nuevoUsuario.getNombreUsuario());
        log.info("Contrasenia: " + nuevoUsuario.getPassword());
        for (String rol : nuevoUsuario.getRoles()) {
            log.info("Rol: " + rol);
        }
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("campos mal puestos o email inválido"), HttpStatus.BAD_REQUEST);
        if(usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);
        if(usuarioService.existsByEmail(nuevoUsuario.getEmail()))
            return new ResponseEntity(new Mensaje("ese email ya existe"), HttpStatus.BAD_REQUEST);
        Usuario usuario =
                /*Usuario.builder().nombre(nuevoUsuario.getNombre()).nombreUsuario(nuevoUsuario.getNombreUsuario()).email(nuevoUsuario.getEmail()).password(passwordEncoder.encode(nuevoUsuario.getPassword())).build();*/
                new Usuario(nuevoUsuario.getNombre(), nuevoUsuario.getNombreUsuario(), nuevoUsuario.getEmail(),
                        passwordEncoder.encode(nuevoUsuario.getPassword()));
        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get());
        for (String rol : nuevoUsuario.getRoles()) {
            switch (rol) {
                case "Operador":
                    roles.add(rolService.getByRolNombre(RolNombre.ROL_OPERADOR).get());
                    break;
            }
        }
        //roles.add(nuevoUsuario.getRoles())
        if(nuevoUsuario.getRoles().contains("admin"))
            roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
        usuario.setRoles(roles);
        Usuario usuarioReturn = usuarioService.save(usuario);
        usuarioReturn.setPassword(nuevoUsuario.getPassword());
        System.out.println("UsuarioReturn: [Password]" + usuarioReturn.getPassword());
        //return notificacionController.enviarCorreoAltaOperador(usuarioReturn);
        return ResponseEntity.ok(usuarioReturn);
        // return new ResponseEntity(new Mensaje("usuario guardado"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){
        log.info("Entra a servicio de login");
        log.info("Usuario: "+loginUsuario.getNombreUsuario());

        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("campos mal puestos"), HttpStatus.BAD_REQUEST);

        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
        System.out.println("Authentication: [Name]="+authentication.getName()+" [Principal]="+authentication.getPrincipal());
        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        JwtDto jwtDto = new JwtDto(jwt, principal.getNombreUsuario(), userDetails.getAuthorities());
        log.info("JwtDTO: "+jwtDto.getNombreUsuario());
        return new ResponseEntity(jwtDto, HttpStatus.OK);
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<Boolean> existeCorreo(@PathVariable("correo") String correo) {
        log.info("Entra a controlador para verificar si existe correo de usuario: "+correo);
        return ResponseEntity.ok(usuarioService.existsByEmail(correo));
    }

    @GetMapping("/nombreUsuario/{nombreUsuario}")
    public ResponseEntity<Boolean> existeNombreUsuario(@PathVariable("nombreUsuario") String nombreUsuario) {
        log.info("Entra a controlador para verificar si existe nombreusuario de usuario "+nombreUsuario);
        return ResponseEntity.ok(usuarioService.existsByNombreUsuario(nombreUsuario));
    }

    @GetMapping("/contrasena")
    public ResponseEntity<String> contrasena(@Param("contrasena") String contrasena) {
        log.info("Ingresa a controlador Auth para crear contraseña codificada");
        return ResponseEntity.ok(passwordEncoder.encode(contrasena));
    }

    @GetMapping("/{nombreUsuario}")
    public ResponseEntity<Cliente> buscarUsuarioPorNombreUsuario(@PathVariable("nombreUsuario") String nombreUsuario) {
        log.info("Entra a controlador Auth para buscar usuario por nombre usuario");
        Usuario usuario = usuarioService.getByNombreUsuario(nombreUsuario);
        if (usuario == null) {
            log.error("No existe usuario con ese nombreUsuario");
            return new ResponseEntity("ERROR: No existe usuario con ese nombreUsuario", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(usuario.getCliente());
    }

}
