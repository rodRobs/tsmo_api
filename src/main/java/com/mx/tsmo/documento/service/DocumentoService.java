package com.mx.tsmo.documento.service;

import com.mx.tsmo.documento.domain.dto.EnvioDoc;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DocumentoService {

    // private static final String nombreArchivoEnvio = "classpath:Formato_Envio.jrxml";
    // private static final String nombreArchivoGuia = "classpath:Formato_Guia_caja.jrxml";
    private static final String nombreArchivoGuia = "C:/Program Files/Apache Software Foundation/Tomcat 9.0/webapps/crud-0.0.1-SNAPSHOT/WEB-INF/classes/Formato_Guia_caja.jrxml";
    private static final String nombreArchivoEnvio = "C:/Program Files/Apache Software Foundation/Tomcat 9.0/webapps/crud-0.0.1-SNAPSHOT/WEB-INF/classes/Formato_Envio.jrxml";

    // private static final String path = "/Users/Joshue/Desktop/";
    private static final String path = "C:/Users/Administrador/Documents/";
    private static final String nombreFinalEnvio = "formato_envio";

    /*
    * METODO PARA CREAR DOCUMENTO DE ENVIO
    * Este metodo crea el documento que se envia a los cliente cuando se ha dado solicitado un envio
    * @author Rodrigo Robles
    * @param envio EnvioDocumento : El parametro es de tipo EnvioDocumento , ya que contiene todos los datos necesarios para crear el documento del envío
    *
    * */
    public boolean crearDocumentoEnvio(EnvioDoc envio) {
        log.info("Entra a servicio para crear el documento del envio solicitado");
        if (envio != null) {
            List<EnvioDoc> envios = new ArrayList<>();
            envios.add(envio);
            if (this.cargarYCompilar(envios, nombreArchivoEnvio) != null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public byte[] crearDocumentoGuia(EnvioDoc envio) {
        log.info("Entra a servicio para crear el documento del envio solicitado");
        if (envio != null) {
            List<EnvioDoc> envios = new ArrayList<>();
            envios.add(envio);
            log.info("NOMBRE ARCHIVO GUIA: "+nombreArchivoGuia);
            byte [] documento = this.cargarYCompilar(envios, nombreArchivoGuia);
            if (documento != null) {
                return documento;
            }
            return null;
        }
        return null;
    }

    public byte[] cargarYCompilar(List<EnvioDoc> envio, String nombreArchivo) {
    // public byte[] cargarYCompilar(List<EnvioDoc> envio, ClassPathResource nombreArchivo) {
        try {
            File file = ResourceUtils.getFile(nombreArchivo);
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(envio);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("CreatedBy", "Rodrigo Robles");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            byte[] envioPDF = JasperExportManager.exportReportToPdf(jasperPrint);
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + nombreFinalEnvio + "_" + envio.get(0).getGuia() + ".pdf");
            return envioPDF;
        } catch (FileNotFoundException fe) {
            log.error("ERROR FileNotFound: "+fe.getMessage());
            log.error("ERROR: "+fe.getLocalizedMessage());
            log.error("ERROR: "+fe.fillInStackTrace());
            log.error("ERROR: "+fe.getStackTrace());
            fe.printStackTrace();
            return null;
        } catch (JRException jre) {
            log.error("ERRRO: JREException"+jre.getMessage());
            return null;
        }
    }

}
