package com.mx.tsmo.notificacion.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

@Slf4j
public class NotificacionService {

    private JavaMailSender javaMailSender;

    // private static final String path = "/Users/Joshue/Documents/";
    private static final String path = "C:/Users/Administrador/Documents/img/logo-black.png";

    @Autowired
    public NotificacionService(JavaMailSender javaMailSender) { this.javaMailSender = javaMailSender; }

    // public void enviarNotificacionEnvio
}
