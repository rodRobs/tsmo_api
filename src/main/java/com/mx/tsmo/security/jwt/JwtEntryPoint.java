package com.mx.tsmo.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtEntryPoint implements AuthenticationEntryPoint {

    private final static Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException, ServletException {
        log.info("Req: "+req);
        log.info("Res: "+res);
        log.info("AuthExcept: "+e.getMessage());
        log.info("Req: "+req.getHeader("Authorization"));
        // log.info("Response: "+res.getHeader());
        logger.error("fail en el método commence");
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario o contraseña incorrectos. Volver a intentar inicio de sesión");
    }
}
