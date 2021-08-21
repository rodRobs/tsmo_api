package com.mx.tsmo.correos.controller;

import com.mx.tsmo.correos.service.NotificacionService;
import com.mx.tsmo.enums.TipoEnvioCorreo;
import com.mx.tsmo.envios.model.domain.Envio;
import com.mx.tsmo.envios.service.EnvioService;
import com.mx.tsmo.security.dto.NuevoUsuario;
import com.mx.tsmo.security.entity.Usuario;
import com.mx.tsmo.security.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("notificacion")
@CrossOrigin("*")
@Slf4j
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private EnvioService envioService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/envio")
    public ResponseEntity<String> enviarCorreoDeEnvio(@RequestBody Envio envio) {
        log.info("Entra a controlador para enviar correo con numero de guia del envio: "+envio.getId());
        Envio envioBD = envioService.buscarPorId(envio.getId());
        if (envioBD == null) {
            return new ResponseEntity("ERROR: No se encontro envio con ese id para enviar Correo", HttpStatus.BAD_REQUEST);
        }
        try {
            notificacionService.envioCorreoFormatoSolicitud(envio);
        } catch (MailException me) {
            log.error("ERROR: ");
            return new ResponseEntity("ERROR: No se pudo enviar el correo, comunicarse con soporte técnico para el reenvio del correo con los datos del ", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Se ha enviado correctamente el correo");
        /*
        try {
            notificacionService.enviarNotificacion(envioBD.getCliente().getCorreo(), notificacionService.crearMensajeDeEnvioGuia(envioBD), TipoEnvioCorreo.ENVIO.getValue());
            return new ResponseEntity("Envio de correo exitoso", HttpStatus.OK);
        } catch (Exception e) {
            log.error("ERROR: "+e.getMessage());
            return new ResponseEntity("ERROR: Ocurrio un error al enviar correo", HttpStatus.BAD_REQUEST);
        }
        */
    }

    @PostMapping("/alta/tsmo")
    public ResponseEntity<String> enviarCorreoDeAlta(@RequestBody Usuario usuario) {
        log.info("Entra a controlador para enviar correo con datos de acceso de usuario");
        Usuario usuarioBD = usuarioService.buscarPorId(usuario.getId());
        if ( usuarioBD == null ) {
            return new ResponseEntity("ERROR: No existe usuario para envio de correo", HttpStatus.BAD_REQUEST);
        }
        try {
            notificacionService.enviarNotificacion(usuarioBD.getEmail(), notificacionService.mensajeAltaUsuario(usuarioBD), TipoEnvioCorreo.ALTA_OPERADOR.getValue());
            return new ResponseEntity("Envio de correo exitoso", HttpStatus.OK);
        } catch (Exception e) {
            log.error("ERROR: "+e.getMessage());
            return new ResponseEntity("ERROR: Ocurrio un error al enviar correo", HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * METODO DE PRUEBA PARA ENVIO DE CORREO CON FORMATO PDF
     * Servicio REST para enviar correo con el formato de envio
     * @author Rodrigo Robles
     * @param envio Long: El parametro envio es de tipo Long, a traves del id buscamos
     * */
    @GetMapping("/envio/formato/{envio}")
    public String envioFormatoPrueba(@PathVariable("envio") Long id) {
        log.info("Entra a servicio para enviar formato por correo de prueba");
        log.info("Envio id:"+id);
        Envio envio = envioService.buscarPorId(id);
        if (envio == null) {
            return "No se encontro envio";
        }
        try {
            notificacionService.envioCorreoFormatoSolicitud(envio);
        } catch (MailException me) {
            log.error("ERROR: ");
            return "ERROR: No se pudo enviar";
        }
        return "Se ha enviado el correo con el PDF exitosamente";
    }

}
