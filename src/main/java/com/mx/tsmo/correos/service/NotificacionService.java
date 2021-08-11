package com.mx.tsmo.correos.service;

import com.mx.tsmo.cotizacion.service.CostoService;
import com.mx.tsmo.documento.domain.dto.EnvioDoc;
import com.mx.tsmo.documento.service.DocumentoService;
import com.mx.tsmo.enums.TipoEnvioCorreo;
import com.mx.tsmo.envios.model.domain.Envio;
import com.mx.tsmo.security.dto.NuevoUsuario;
import com.mx.tsmo.security.entity.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class NotificacionService {

    private JavaMailSender javaMailSender;

    @Autowired
    private CostoService costoService;

    @Autowired
    private DocumentoService documentoService;

    private static final String ASUNTO_ENVIO = "TSMO: No Guía  de Envío";
    private static final String ASUNTO_ALTA_OPERADOR = "TSMO: Datos de acceso a sistema de TSMO";

    private static final String path = "/Users/Joshue/Desktop/";
    private static final String nombreFinalEnvio = "formato_envio";

    @Autowired
    public NotificacionService(JavaMailSender javaMailSender) { this.javaMailSender = javaMailSender; }

    @Autowired
    PasswordEncoder passwordEncoder;

    public void enviarNotificacion (String email, String mensaje, int tipoCorreo) {
    // log.info(mensaje);
        log.info("Entra a serivio para enviar notificacion");
        log.info(email);
        /*
        log.info("Entra a enviar la notificacion");
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom("avisos@tsmo.com.mx");
        mail.setSubject(asunto);
        mail.setText(mensaje);
        mail.setCc("rodrigo.roblesortiz@gmail.com");
        javaMailSender.send(mail);
         */
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mm)  {
                try {

                    // mm.setFrom(new InternetAddress("avisos@tsmo.com.mx"));

                    log.info("Entra al prepare");
                    mm.setFrom(new InternetAddress("avisos@tsmo.com.mx"));
                    mm.setSubject(ASUNTO_ENVIO);
                    mm.setText(mensaje);

                    MimeMessageHelper helper = new MimeMessageHelper(mm, "utf-8");
                    //mm.setText(mensaje, );
                    helper.setTo(email);
                    helper.setText(mensaje, true);
                    helper.setSubject((tipoCorreo == TipoEnvioCorreo.ENVIO.getValue()) ? ASUNTO_ENVIO : ASUNTO_ALTA_OPERADOR);
                    // helper.setSubject(ASUNTO_ENVIO);
                    helper.setFrom(new InternetAddress("avisos@tsmo.com.mx"));
                    javaMailSender.send(mm);
                } catch (Exception e) {

                    log.error("ERROR: "+e.getMessage());
                }
            }
        };

        try {
            log.info("Ingresa a enviar el correo");
            javaMailSender.send(preparator);
        } catch (MailException ex) {
            log.error("Ha ocurrido un error; No se puede enviar el correo");
            System.err.println(ex.getMessage());
        }

    }

    public String crearMensajeDeEnvioGuia (Envio envio) {
        log.info("Entra a crear mensaje de envio guia");
        log.info("Telefono: "+envio.getDocumentacion().getCotizacion().getOrigen().getTelefonos().get(0));
        return "<html lang=\"en\">\n" +
                "            <head>\n" +
                "            <meta charset=\"UTF-8\">\n" +
                "            <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "            <title>Correo de promoción</title>\n" +
                "            </head>\n" +
                "            <body style=\"font-family: Geneva, Verdana, sans-serif;\">\n" +
                "            <header style=\"background-color: #F7FFF7; padding: 30px 0;\">\n" +
                "                <div style=\"margin: 0 auto; width: 90%; max-width: 1000px;\">\n" +
                "                    <div class=\"logo\">\n" +
                "                        <a href=\"https://tsmo.com.mx/\">\n" +
                "                            <img src=\"https://tsmo.com.mx/img/logo-black.png\" alt=\"\" title=\"Visita nuestro sitio web\">\n" +
                "                        </a>\n" +
                "                    </div>\n" +
                "                    <div class=\"telefono\" style=\"border: 3px solid #B22525; padding: 20px\">\n" +
                "                        (55)-5714-3905\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </header>\n" +
                "            <main>\n" +
                "                <section style=\"background-color: #F7FFF7; padding: 60px 0;\">\n" +
                "                <div style=\"margin: 0 auto; width: 90%; max-width: 1000px; font-size: 18px; color: #1A535C;\">\n" +
                "                        <p style=\"text-align: justify;\">\n" +
                "                            Hola <strong style=\"text-transform: uppercase;\"> " + envio.getCliente().getNombre() + " </strong>.\n" +
                "                        </p>\n" +
                "                        <br>\n" +
                "                        <p style=\"text-align: justify;\">\n" +
                "                            Has realizado con éxito la contratación para tu envío registrado con el siguiente número de guía para su rastreo.\n" +
                "                        </p>\n" +
                "                        <center>\n" +
                "                           <strong style=\"text-align: center;\">\n" +
                "                               <a href=\"http://localhost:4200/rastrear?guia="+ envio.getGuiaTsmo() + "\">\n" +
                "                               " + envio.getGuiaTsmo() +
                "                               </a>" +
                "                           </strong>\n" +
                "                        </center>\n" +
                "                        <br><br>\n" +
                "                        \n" +
                "                        <strong>" +
                "                            ORIGEN\n\n" +
                "                        </strong>" +
                "                            <div style=\"width: 100%\">\n" +
                "                               <label for=\"remitente\" style=\"width: 30%\">Remitente:</label>\n" +
                "                               <label for=\"remitente\" class=\"valor\"> " + envio.getDocumentacion().getCotizacion().getOrigen().getRemitente() + " </label>\n" +
                "                            </div>\n" +
                "                            <div style=\"width: 100%\">\n" +
                "                               <label for=\"calle\" style=\"width: 30%\">Calle:</label>\n" +
                "                               <label for=\"calle\" class=\"valor\"> " + envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getCalle() + " </label>\n" +
                "                            </div>\n" +
                "                            <div style=\"width: 100%\">\n" +
                "                               <label for=\"numeroExt\" style=\"width: 30%\">Número Exterior: </label>\n" +
                "                               <label for=\"numeroExt\" class=\"valor\"> " + envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getNumeroExt() + " </label>\n" +
                "                           </div>\n" +
                "                           <div style=\"width: 100%\">\n" +
                "                               <label for=\"numeroInt\" style=\"width: 30%\">Número Interior:</label>\n" +
                "                               <label for=\"numeroInt\" class=\"valor\"> " + ((envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getNumeroInt() != null) ? envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getNumeroInt() : "") + " </label>\n" +
                "                           </div>\n" +
                "                           <div style=\"width: 100%\">\n" +
                "                               <label for=\"colonia\" style=\"width: 30%\">Colonia:</label>\n" +
                "                               <label for=\"colonia\" class=\"valor\"> " + envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getColonia() + " </label>\n" +
                "                           </div>\n" +
                "                           <div style=\"width: 100%\">\n" +
                "                               <label for=\"cp\" style=\"width: 30%\">Código Postal</label>\n" +
                "                               <label for=\"cp\" class=\"valor\"> " + envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getCodigoPostal() + " </label>\n" +
                "                           </div>\n" +
                "                           <div style=\"width: 100%\">\n" +
                "                               <label for=\"ciudad\" style=\"width: 30%\">Ciudad:</label>\n" +
                "                               <label for=\"ciudad\" class=\"valor\"> " + envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getCiudad() + " </label>\n" +
                "                           </div>\n" +
                "                           <div style=\"width: 100%\">\n" +
                "                               <label for=\"estado\" style=\"width: 30%\">Estado:</label>\n" +
                "                               <label for=\"estado\" class=\"valor\"> " + envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getEstado() + " </label>\n" +
                "                           </div>\n" +
                "                           <div style=\"width: 100%\">\n" +
                "                               <label for=\"pais\" style=\"width: 30%\">Pais:</label>\n" +
                "                               <label for=\"pais\" class=\"valor\"> " + envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getPais() + " </label>\n" +
                "                           </div>\n" +
                "                           <div style=\"width: 100%\">\n" +
                "                               <label for=\"telefono\" style=\"width: 30%\">Teléfono:</label>\n" +
                "                               <label for=\"telefono\" class=\"valor\"> " + envio.getDocumentacion().getCotizacion().getOrigen().getTelefonos().get(0).getNumeroTelefono() + " </label>\n" +
                "                           </div>\n" +
                "                           <div style=\"width: 100%\">\n" +
                "                               <label for=\"correo\" style=\"width: 30%\">Correo:</label>\n" +
                "                               <label for=\"correo\" class=\"valor\"> " + envio.getDocumentacion().getCotizacion().getOrigen().getEmail() + " </label>\n" +
                "                           </div>\n" +
                "                        <strong>" +
                "                            DESTINO\n\n" +
                "                        </strong>" +
                "                        <div style=\"width: 100%\">\n" +
                "                           <label for=\"destinatario\" style=\"width: 30%\">Destinatario:</label>\n" +
                "                           <label for=\"destinatario\" class=\"valor\"> " + envio.getDocumentacion().getCotizacion().getDestino().getDestinatario() + " </label>\n" +
                "                       </div>\n" +
                "                       <div style=\"width: 100%\">\n" +
                "                           <label for=\"destinatario2\" style=\"width: 30%\">Destinatario 2:</label>\n" +
                "                           <label for=\"destinatario2\" class=\"valor\"> " + envio.getDocumentacion().getCotizacion().getDestino().getDestinatario2() + " </label>\n" +
                "                       </div>\n" +
                "                       <div style=\"width: 100%\">\n" +
                "                           <label for=\"calle\" style=\"width: 30%\">Calle:</label>\n" +
                "                           <label for=\"calle\" class=\"valor\"> " + envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getCalle() + " </label>\n" +
                "                       </div>\n" +
                "                       <div style=\"width: 100%\">\n" +
                "                           <label for=\"numeroExt\" style=\"width: 30%\">Número Exterior:</label>\n" +
                "                           <label for=\"numeroExt\" class=\"valor\"> " + envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getNumeroExt() + " </label>\n" +
                "                       </div>\n" +
                "                       <div style=\"width: 100%\">\n" +
                "                           <label for=\"numeroInt\" style=\"width: 30%\">Número Interior:</label>\n" +
                "                           <label for=\"numeroInt\" class=\"valor\"> " + ((envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getNumeroInt() != null) ? envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getNumeroInt() : "") + " </label>\n" +
                "                       </div>\n" +
                "                       <div style=\"width: 100%\">\n" +
                "                           <label for=\"colonia\" style=\"width: 30%\">Colonia:</label>\n" +
                "                           <label for=\"colonia\" class=\"valor\"> " + envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getColonia() + " </label>\n" +
                "                       </div>\n" +
                "                       <div style=\"width: 100%\">\n" +
                "                           <label for=\"cp\" style=\"width: 30%\"> Código Postal:</label>\n" +
                "                           <label for=\"cp\" class=\"valor\"> " + envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getCodigoPostal() + " </label>\n" +
                "                       </div>\n" +
                "                       <div style=\"width: 100%\">\n" +
                "                           <label for=\"ciudad\" style=\"width: 30%\">Ciudad:</label>\n" +
                "                           <label for=\"ciudad\" class=\"valor\"> " + envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getCodigoPostal() + " </label>\n" +
                "                       </div>\n" +
                "                       <div style=\"width: 100%\">\n" +
                "                           <label for=\"estado\" style=\"width: 30%\">Estado:</label>\n" +
                "                           <label for=\"estado\" class=\"valor\"> " + envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getEstado() + " </label>\n" +
                "                       </div>\n" +
                "                       <div style=\"width: 100%\">\n" +
                "                           <label for=\"pais\" style=\"width: 30%\">País:</label>\n" +
                "                           <label for=\"pais\" class=\"valor\"> " + envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getPais() + " </label>\n" +
                "                       </div>\n" +
                "                       <div style=\"width: 100%\">\n" +
                "                           <label for=\"telefono\" style=\"width: 30%\">Teléfono:</label>\n" +
                "                           <label for=\"telefono\" class=\"valor\"> " + envio.getDocumentacion().getCotizacion().getDestino().getTelefonos().get(0).getNumeroTelefono() + " </label>\n" +
                "                       </div>\n" +
                "                       <div style=\"width: 100%\">\n" +
                "                           <label for=\"correo\" style=\"width: 30%\">Correo:</label>\n" +
                "                           <label for=\"correo\" class=\"valor\"> " + envio.getDocumentacion().getCotizacion().getDestino().getEmail() + " </label>\n" +
                "                       </div>\n" +
                "                        <p style=\"\">\n" +
                "                            Atentamente:\n" +
                "                        </p>\n" +
                "                        <br>\n" +
                "                        <p>\n" +
                "                            Transportes San Miguel de Oaxaca\n" +
                "                        </p>\n" +
                "                    </div> \n" +
                "                </section>\n" +
                "            </main>\n" +
                "            <footer style=\"background-color: #040E10; padding: 40px 0;\">\n" +
                "                <div style=\"margin: 0 auto; width: 90%; max-width: 1000px;\">\n" +
                "                    <p style=\"color: #F7FFF7; font-size: 16px; text-align: center;\">\n" +
                "                        Derechos Reservados &copy; Transportes San Miguel de Oaxaca \n" +
                "                    </p>\n" +
                "                </div>\n" +
                "            </footer>\n" +
                "            </body>\n" +
                "            </html>";
    }

    public String mensajeAltaUsuario(Usuario usuario) {
        log.info("Entra a servicio para enviar correo con alta de usuario");
        return "<html lang=\"en\">\n" +
                "            <head>\n" +
                "            <meta charset=\"UTF-8\">\n" +
                "            <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "            <title>Correo de promoción</title>\n" +
                "            </head>\n" +
                "            <body style=\"font-family: Geneva, Verdana, sans-serif;\">\n" +
                "            <header style=\"background-color: #F7FFF7; padding: 30px 0;\">\n" +
                "                <div style=\"margin: 0 auto; width: 90%; max-width: 1000px;\">\n" +
                "                    <div class=\"logo\">\n" +
                "                        <a href=\"https://tsmo.com.mx/\">\n" +
                "                            <img src=\"https://tsmo.com.mx/img/logo-black.png\" alt=\"\" title=\"Visita nuestro sitio web\">\n" +
                "                        </a>\n" +
                "                    </div>\n" +
                "                    <div class=\"telefono\" style=\"border: 3px solid #B22525; padding: 20px\">\n" +
                "                        (55)-5714-3905\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </header>\n" +
                "            <main>\n" +
                "                <section style=\"background-color: #F7FFF7; padding: 60px 0;\">\n" +
                "                <div style=\"margin: 0 auto; width: 90%; max-width: 1000px; font-size: 18px; color: #1A535C;\">\n" +
                "                        <p style=\"text-align: justify;\">\n" +
                "                            Hola <strong style=\"text-transform: uppercase;\"> " + usuario.getNombre() + " </strong>.\n" +
                "                        </p>\n" +
                "                        <br>\n" +
                "                        <p style=\"text-align: justify;\">\n" +
                "                            Has sido dado de alta para el sistema web de TSMO, con los siguientes datos de acceso.\n" +
                "                        </p>\n" +
                "                        <center>\n" +
                "                           <strong style=\"text-align: center;\">\n" +
                "                               Usuario: " + usuario.getNombreUsuario() +
                "                           </strong>\n" +

                "                           <br>\n" +
                "                           <strong style=\"text-align: center;\">\n" +
                "                               Contraseña: " + usuario.getPassword() +
                "                           </strong>\n" +

                "                        </center>\n" +

                "                        <p style=\"\">\n" +
                "                            Atentamente:\n" +
                "                        </p>\n" +
                "                        <br>\n" +
                "                        <p>\n" +
                "                            Transportes San Miguel de Oaxaca\n" +
                "                        </p>\n" +
                "                    </div> \n" +
                "                </section>\n" +
                "            </main>\n" +
                "            <footer style=\"background-color: #040E10; padding: 40px 0;\">\n" +
                "                <div style=\"margin: 0 auto; width: 90%; max-width: 1000px;\">\n" +
                "                    <p style=\"color: #F7FFF7; font-size: 16px; text-align: center;\">\n" +
                "                        Derechos Reservados &copy; Transportes San Miguel de Oaxaca \n" +
                "                    </p>\n" +
                "                </div>\n" +
                "            </footer>\n" +
                "            </body>\n" +
                "            </html>";

    }

    public boolean envioFormatoEnvio(String[] email, String asunto, String mensaje, String guia) {
        log.info("Ingresa a enviar el formato de envio en pdf");
        MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mm) throws Exception {
                log.info("Entra a prepare");
                mm.setFrom(new InternetAddress("avisos@tsmo.com.mx"));
                log.info("Ha colocado setFrom");
                mm.setSubject(asunto);
                log.info("Ha colocado asunto");
                mm.setText(mensaje);
                log.info("Ha colocado el mensaje");

                log.info("Entra a buscar archivo en: " + path + nombreFinalEnvio + "_" + guia + ".pdf");
                FileSystemResource file = new FileSystemResource((new File(path + nombreFinalEnvio + "_" + guia + ".pdf")));
                log.info("Ha recuperado el File");
                log.info("Existe file: "+file.exists());
                MimeMessageHelper helper = new MimeMessageHelper(mm, true);
                // MimeMessageHelper helper = new MimeMessageHelper(mm, "utf-8");
                log.info("Ha creado el helper");
                helper.addAttachment(path + nombreFinalEnvio + "_" + guia + ".pdf", file);
                log.info("Se ha agregado el adjunto");
                helper.setTo(email);
                helper.setText(mensaje, true);

            }
        };

        try {
            log.info("Ingresa a enviar el correo");
            javaMailSender.send(mimeMessagePreparator);
        } catch (MailException me) {
            log.error("ERROR: MailException "+me.getMessage());
            return false;
        }
        return true;
    }

    /*

     */
    public boolean envioCorreoFormatoSolicitud(Envio envio) {
        log.info("Entra para enviar correo con pdf");
        String[] email = new String[] {"rodrigo.roblesortiz@gmail.com"/*, "credito.cobranza@tsmo.com.mx"*/, envio.getCliente().getCorreo()};
        List<EnvioDoc> envios = new ArrayList<>();
        if (documentoService.crearDocumentoEnvio(this.crearEnvioDoc(envio))) {
            log.info("Ha creado correctamente el formato");
            try {
                log.info("Ingresa a enviar formato de envio");
                this.envioFormatoEnvio(email, "NOTIFICACIÓN: TSMO Solicitud Envio", crearMensajeDeEnvioGuia(envio), envio.getGuiaTsmo());
            } catch (MailException me) {
                log.info("Ha ocurrido un error en la parte de formato");
                log.error("ERROR: "+me.getMessage());
                return false;
            }
            return true;
        }
        return false;
    }

    public EnvioDoc crearEnvioDoc(Envio envio) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        log.info("Crea Envio Doc: ");
        log.info("Fecha: "+dateFormat.format(envio.getCreateAt()));
        log.info("Documentacion: "+envio.getDocumentacion());
        log.info("Cotizacion: "+envio.getDocumentacion().getCotizacion());
        log.info("Costo: "+String.valueOf(costoService.buscarPorCotizacion(envio.getDocumentacion().getCotizacion()).getCostoTotal()));
        EnvioDoc envioDoc;
        try {
             envioDoc = EnvioDoc.builder()
                    .alto(String.valueOf(envio.getDocumentacion().getCotizacion().getDetalle().get(0).getDimensiones().getAlto()))
                    .calle_destino(envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getCalle())
                    .calle_origen(envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getCalle())
                    .ciudad_destino(envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getCiudad())
                    .ciudad_origen(envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getCiudad())
                    .ancho(String.valueOf(envio.getDocumentacion().getCotizacion().getDetalle().get(0).getDimensiones().getAncho()))
                    .codigo_postal_destino(envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getCodigoPostal())
                    .codigo_postal_origen(envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getCodigoPostal())
                    .colonia_destino(envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getColonia())
                    .colonia_origen(envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getColonia())
                    .costo_total(String.valueOf(costoService.buscarPorCotizacion(envio.getDocumentacion().getCotizacion()).getCostoTotal()))
                    .destinatario_destino(envio.getDocumentacion().getCotizacion().getDestino().getDestinatario())
                    .destinatario_destino2(envio.getDocumentacion().getCotizacion().getDestino().getDestinatario2())
                    .email_destino(envio.getDocumentacion().getCotizacion().getDestino().getEmail())
                    .email_origen(envio.getDocumentacion().getCotizacion().getOrigen().getEmail())
                    .estado_destino(envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getEstado())
                    .estado_origen(envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getEstado())
                    .guia(envio.getGuiaTsmo())
                    .largo(String.valueOf(envio.getDocumentacion().getCotizacion().getDetalle().get(0).getDimensiones().getLargo()))
                    .nombre(envio.getCliente().getNombre())
                    .numero_exterior_destino(envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getNumeroExt())
                    .numero_exterior_origen(envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getNumeroExt())
                    .numero_interior_destino(envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getNumeroInt())
                    .numero_interior_origen(envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getNumeroExt())
                    .telefono_destino(envio.getDocumentacion().getCotizacion().getDestino().getTelefonos().get(0).getNumeroTelefono())
                    .numero_telefono_origen(envio.getDocumentacion().getCotizacion().getOrigen().getTelefonos().get(0).getNumeroTelefono())
                    .pais_destino(envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getPais())
                    .pais_origen(envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getPais())
                    .peso(String.valueOf(envio.getDocumentacion().getCotizacion().getDetalle().get(0).getDimensiones().getPeso()))
                    .tipo_envio((envio.getDocumentacion().getCotizacion().getOpciones().getTipoEnvio().equalsIgnoreCase("P")) ? "Paquete" : (envio.getDocumentacion().getCotizacion().getOpciones().getTipoEnvio().equalsIgnoreCase("V")) ? "Valija" : "Sobre")
                    .remitente(envio.getDocumentacion().getCotizacion().getOrigen().getRemitente())
                    .tipo_entrega((envio.getDocumentacion().getCotizacion().getOpciones().getTipoEntrega().equalsIgnoreCase("D")) ? "Domicilio" : "Ocurre")
                    .fecha(dateFormat.format(envio.getCreateAt()))
                    .build();
        } catch (NullPointerException ne) {
            log.error("ERROR: NullPointerException: "+ne.getMessage());
            log.error("ERROR: GetLocalizedMessage: "+ne.getLocalizedMessage());
            log.error("ERROR: Cause: "+ne.getCause());
            log.error("ERROR: StackTrace: "+ne.fillInStackTrace());
            log.error("ERROR: Suppressed"+ne.getSuppressed());
            ne.printStackTrace();
            return null;
        }
        System.out.println("Alto: "+String.valueOf(envio.getDocumentacion().getCotizacion().getDetalle().get(0).getDimensiones().getAlto()));

        return envioDoc;
    }
}
