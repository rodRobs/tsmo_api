package com.mx.tsmo.envios.service;

import com.mx.tsmo.envios.model.domain.Envio;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ExportarExcelEnvioService {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Envio> envios;
    private final String nombre = "excel_envios_";

    public ExportarExcelEnvioService(List<Envio> envios) {
        this.envios = envios;
        workbook = new XSSFWorkbook();
    }

    public void writeHeaderLine() {
        sheet = workbook.createSheet(nombre);

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 1, "ID_ENVIO", style);
        createCell(row, 2, "NUMERO_GUIA_TSMO", style);
        createCell(row, 3, "REMITENTE", style);
        createCell(row, 4, "CALLE ORIGEN", style);
        createCell(row, 5, "NUMERO EXTERIOR ORIGEN", style);
        createCell(row, 6, "NUMERO INTERIOR ORIGEN", style);
        createCell(row, 7, "COLONIA ORIGEN", style);
        createCell(row, 8, "CODIGO POSTAL ORIGEN", style);
        createCell(row, 9, "CIUDAD ORIGEN", style);
        createCell(row, 10, "ESTADO ORIGEN", style);
        createCell(row, 11, "PAIS ORIGEN", style);
        createCell(row, 12, "TELEFONO ORIGEN", style);
        createCell(row, 13, "CORREO ORIGEN", style);
        createCell(row, 14, "DESTINATARIO", style);
        createCell(row, 15, "DESTINATARIO 2", style);
        createCell(row, 16, "CALLE DESTINO", style);
        createCell(row, 17, "NUMERO EXTERIOR DESTINO", style);
        createCell(row, 18, "NUMERO INTERIOR DESTINO", style);
        createCell(row, 19, "COLONIA DESTINO", style);
        createCell(row, 20, "CODIGO POSTAL DESTINO", style);
        createCell(row, 21, "CIUDAD DESTINO", style);
        createCell(row, 22, "ESTADO DESTINO", style);
        createCell(row, 23, "PAIS DESTINO", style);
        createCell(row, 24, "TELEFONO DESTINO", style);
        createCell(row, 25, "CORREO DESTINO", style);
        createCell(row, 26, "PAQUETE LARGO", style);
        createCell(row, 27, "PAQUETE ANCHO", style);
        createCell(row, 28, "PAQUETE ALTO", style);
        createCell(row, 29, "PAQUETE PESO", style);
        createCell(row, 30, "TIPO DE ENTREGA", style);
        createCell(row, 31, "TIPO DE ENVIO", style);
        /* createCell(row, 32, "fecha de alta", style);
        createCell(row, 33, "antiguedad", style);
        createCell(row, 34, "tipo de contrato", style);
        createCell(row, 35, "fecha de terminación de obra", style);
        createCell(row, 36, "frecuencia de pago", style);
        createCell(row, 37, "planta depto", style);
        createCell(row, 38, "puesto", style);
        createCell(row, 39, "registro patronal", style);
        createCell(row, 40, "zona economica", style);
        createCell(row, 41, "centro de costo", style);
        createCell(row, 42, "estado imp estatal", style);
        createCell(row, 43, "num credito infonavit", style);
        createCell(row, 44, "credito fonacot", style);
        createCell(row, 45, "tipo descuento credito infonavit", style);
        createCell(row, 46, "sueldo diario", style);
        createCell(row, 47, "sueldo diario integrado", style);
        createCell(row, 48, "sueldo mensual", style);
        createCell(row, 49, "tabulador", style);
        createCell(row, 50, "grado", style);
        createCell(row, 51, "posicion", style);
        createCell(row, 52, "empresa", style);
        createCell(row, 53, "correo", style);
        createCell(row, 54, "edad", style);
        createCell(row, 55, "nacionalidad", style);
        createCell(row, 56, "estatura", style);
        createCell(row, 57, "peso", style);
        createCell(row, 58, "puede viajar", style);
        createCell(row, 59, "residencia", style);
        createCell(row, 60, "casa propia", style);
        createCell(row, 61, "valor aproximado", style);
        createCell(row, 62, "renta", style);
        createCell(row, 63, "monsto mensual", style);
        createCell(row, 64, "automovil propio", style);
        createCell(row, 65, "automovil marca", style);
        createCell(row, 66, "automovil modelo", style);
        createCell(row, 67, "automovil valor aproximado", style);
        createCell(row, 68, "conmutador", style);
        createCell(row, 69, "fax", style);
        createCell(row, 70, "mecanografia", style);
        createCell(row, 71, "taquigrafia", style);
        createCell(row, 72, "copiadora", style);
        createCell(row, 73, "titulo", style);
        createCell(row, 74, "tipo de ingreso", style); */
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Short) {
            cell.setCellValue((Short) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines(Envio envio, int rowCount) {
        // int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        Row row = sheet.createRow(rowCount++);
        int columnCount = 1;

        createCell(row, columnCount++, (envio.getId() == null) ? null : envio.getId(), style);
        createCell(row, columnCount++, (envio.getGuiaTsmo() == null) ? null : envio.getGuiaTsmo(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getOrigen().getRemitente() == null) ? null : envio.getDocumentacion().getCotizacion().getOrigen().getRemitente(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getCalle() == null) ? null : envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getCalle(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getNumeroExt() == null) ? null : envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getNumeroExt(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getNumeroInt() == null) ? null :envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getNumeroInt(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getColonia() == null) ? null : envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getColonia(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getCodigoPostal() == null) ? null : envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getCodigoPostal(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getCiudad() == null) ? null : envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getCiudad(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getEstado() == null) ? null : envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getEstado(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getPais() == null) ? null : envio.getDocumentacion().getCotizacion().getOrigen().getDomicilio().getPais(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getOrigen().getTelefonos().get(0).getNumeroTelefono() == null) ? null : envio.getDocumentacion().getCotizacion().getOrigen().getTelefonos().get(0).getNumeroTelefono() , style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getOrigen().getEmail() == null) ? null : envio.getDocumentacion().getCotizacion().getOrigen().getEmail(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getDestino().getDestinatario() == null) ? null : envio.getDocumentacion().getCotizacion().getDestino().getDestinatario(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getDestino().getDestinatario2() == null) ? null : envio.getDocumentacion().getCotizacion().getDestino().getDestinatario2(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getCalle() == null) ? null : envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getCalle(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getNumeroExt() == null) ? null : envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getNumeroExt(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getNumeroInt() == null) ? null : envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getNumeroInt(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getColonia() == null) ? null : envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getColonia(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getCodigoPostal() == null) ? null : envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getCodigoPostal(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getCiudad() == null) ? null : envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getCiudad(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getEstado() == null) ? null : envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getEstado(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getPais() == null) ? null : envio.getDocumentacion().getCotizacion().getDestino().getDomicilio().getPais(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getDestino().getTelefonos().get(0).getNumeroTelefono() == null) ? null : envio.getDocumentacion().getCotizacion().getDestino().getTelefonos().get(0).getNumeroTelefono(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getDestino().getEmail() == null) ? null : envio.getDocumentacion().getCotizacion().getDestino().getEmail(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getDetalle().get(0).getDimensiones().getLargo() == 0L) ? null : envio.getDocumentacion().getCotizacion().getDetalle().get(0).getDimensiones().getLargo(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getDetalle().get(0).getDimensiones().getAncho() == 0L) ? null : envio.getDocumentacion().getCotizacion().getDetalle().get(0).getDimensiones().getAncho(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getDetalle().get(0).getDimensiones().getAlto() == 0L) ? null : envio.getDocumentacion().getCotizacion().getDetalle().get(0).getDimensiones().getAlto(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getDetalle().get(0).getDimensiones().getPeso() == 0) ? null : envio.getDocumentacion().getCotizacion().getDetalle().get(0).getDimensiones().getPeso(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getOpciones().getTipoEntrega() == null) ? null : envio.getDocumentacion().getCotizacion().getOpciones().getTipoEntrega(), style);
        createCell(row, columnCount++, (envio.getDocumentacion().getCotizacion().getOpciones().getTipoEnvio() == null) ? null : envio.getDocumentacion().getCotizacion().getOpciones().getTipoEnvio(), style);

/*
        createCell(row, columnCount++, (empleado.getIdEmpleado() == null) ? null : empleado.getIdEmpleado(), style);
        createCell(row, columnCount++, (empleado.getNoEmpleado() == null) ? null : empleado.getNoEmpleado(), style);
        createCell(row, columnCount++, (empleado.getNombre() == null) ? null : empleado.getNombre(), style);
        createCell(row, columnCount++, (empleado.getApat() == null) ? null : empleado.getApat(), style);
        createCell(row, columnCount++, (empleado.getAmat() == null) ? null : empleado.getAmat(), style);
        createCell(row, columnCount++, (empleado.getFechaNac() == null) ? null : empleado.getFechaNac().toString(), style);
        createCell(row, columnCount++, (empleado.getEntidadNac() == null) ? null : empleado.getEntidadNac(), style);
        createCell(row, columnCount++, (empleado.getCalle() == null) ? null : empleado.getCalle(), style);
        createCell(row, columnCount++, (empleado.getNumero() == null) ? null : empleado.getNumero(), style);
        createCell(row, columnCount++, (empleado.getColonia() == null) ? null : empleado.getColonia(), style);
        createCell(row, columnCount++, (empleado.getDeleg() == null) ? null : empleado.getDeleg(), style);
        createCell(row, columnCount++, (empleado.getLocalidad() == null) ? null : empleado.getLocalidad(), style);
        createCell(row, columnCount++, (empleado.getEntidadFed() == null) ? null : empleado.getEntidadFed(), style);
        createCell(row, columnCount++, (empleado.getCp() == null) ? null : empleado.getCp(), style);
        createCell(row, columnCount++, (empleado.getTelefono() == null) ? null : empleado.getTelefono(), style);
        createCell(row, columnCount++, (empleado.getCelular() == null) ? null : empleado.getCelular(), style);
        createCell(row, columnCount++, (empleado.getTelefonoNegocio() == null) ? null : empleado.getTelefonoNegocio(), style);
        createCell(row, columnCount++, (empleado.getTelefonoOtro() == null) ? null : empleado.getTelefonoOtro(), style);
        createCell(row, columnCount++, (empleado.getNumImss() == null) ? null : empleado.getNumImss(), style);
        createCell(row, columnCount++, (empleado.getRfc() == null) ? null : empleado.getRfc(), style);
        createCell(row, columnCount++, (empleado.getCurp() == null) ? null : empleado.getCurp(), style);
        createCell(row, columnCount++, (empleado.getFormaPago() == null) ? null : empleado.getFormaPago(), style);
        createCell(row, columnCount++, (empleado.getBanco() == null) ? null : empleado.getBanco(), style);
        createCell(row, columnCount++, (empleado.getCuenta() == null) ? null : empleado.getCuenta(), style);
        createCell(row, columnCount++, (empleado.getClabe() == null) ? null : empleado.getClabe(), style);
        createCell(row, columnCount++, (empleado.getPantalon() == 0) ? null : empleado.getPantalon(), style);
        createCell(row, columnCount++, (empleado.getCamisa() == null) ? null : empleado.getCamisa(), style);
        createCell(row, columnCount++, (empleado.getChamarra() == 0) ? null : empleado.getChamarra(), style);
        createCell(row, columnCount++, (empleado.getZapatos() == 0) ? null : empleado.getZapatos(), style);
        createCell(row, columnCount++, (empleado.getComentarios() == null) ? null : empleado.getComentarios(), style);
        createCell(row, columnCount++, (empleado.getTipoEmpleado() == null) ? null : empleado.getTipoEmpleado(), style);
        createCell(row, columnCount++, (empleado.getFechaRegistro() == null) ? null : empleado.getFechaRegistro().toString(), style);
        createCell(row, columnCount++, (empleado.getFechaAlta() == null) ? null : empleado.getFechaAlta().toString(), style);
        createCell(row, columnCount++, (empleado.getAntiguedad() == null) ? null : empleado.getAntiguedad(), style);
        createCell(row, columnCount++, (empleado.getContrato() == null) ? null : empleado.getContrato(), style);
        createCell(row, columnCount++, (empleado.getTerminacion() == null) ? null : empleado.getTerminacion(), style);
        createCell(row, columnCount++, (empleado.getFrecPago() == null) ? null : empleado.getFrecPago(), style);
        createCell(row, columnCount++, (empleado.getPlantaDpto() == null) ? null : empleado.getPlantaDpto(), style);
        createCell(row, columnCount++, (empleado.getPuesto() == null) ? null : empleado.getPuesto(), style);
        createCell(row, columnCount++, (empleado.getPatronal() == null) ? null : empleado.getPatronal(), style);
        createCell(row, columnCount++, (empleado.getZonaEconomica() == null) ? null : empleado.getZonaEconomica(), style);
        createCell(row, columnCount++, (empleado.getCentroCosto() == null) ? null : empleado.getCentroCosto(), style);
        createCell(row, columnCount++, (empleado.getEstadoEstatal() == null) ? null : empleado.getEstadoEstatal(), style);
        createCell(row, columnCount++, (empleado.getInfonavit() == null) ? null : empleado.getInfonavit(), style);
        createCell(row, columnCount++, (empleado.getFonacot() == null) ? null : empleado.getFonacot(), style);
        createCell(row, columnCount++, (empleado.getTipoDescuentoInfonavit() == null) ? null : empleado.getTipoDescuentoInfonavit(), style);
        createCell(row, columnCount++, (empleado.getSueldoDiario() == null) ? null : empleado.getSueldoDiario(), style);
        createCell(row, columnCount++, (empleado.getSueldoDiarioIntegrado() == null) ? null : empleado.getSueldoDiarioIntegrado(), style);
        createCell(row, columnCount++, (empleado.getSueldoMensual() == null) ? null : empleado.getSueldoMensual(), style);
        createCell(row, columnCount++, (empleado.getTabulador() == null) ? null : empleado.getTabulador(), style);
        createCell(row, columnCount++, (empleado.getGrado() == null) ? null : empleado.getGrado(), style);
        createCell(row, columnCount++, (empleado.getPosicion() == null) ? null : empleado.getPosicion(), style);
        createCell(row, columnCount++, (empleado.getEmpresa() == null) ? null : empleado.getEmpresa(), style);
        createCell(row, columnCount++, (empleado.getCorreo() == null) ? null : empleado.getCorreo(), style);
        createCell(row, columnCount++, (empleado.getEdad() == 0) ? null : empleado.getEdad(), style);
        createCell(row, columnCount++, (empleado.getNacionalidad() == null) ? null : empleado.getNacionalidad(), style);
        createCell(row, columnCount++, (empleado.getEstatura() == null) ? null : empleado.getEstatura(), style);
        createCell(row, columnCount++, (empleado.getPeso() == null) ? null : empleado.getPeso(), style);
        createCell(row, columnCount++, (empleado.getViajar() == null) ? null : empleado.getViajar(), style);
        createCell(row, columnCount++, (empleado.getResidencia() == null) ? null : empleado.getResidencia(), style);
        createCell(row, columnCount++, (empleado.getCasaPropia() == null) ? null : empleado.getCasaPropia(), style);
        createCell(row, columnCount++, (empleado.getValorAproximado() == null) ? null : empleado.getValorAproximado(), style);
        createCell(row, columnCount++, (empleado.getRenta() == null) ? null : empleado.getRenta(), style);
        createCell(row, columnCount++, (empleado.getMontoMensual() == null) ? null : empleado.getMontoMensual(), style);
        createCell(row, columnCount++, (empleado.getAutomovilPropio() == null) ? null : empleado.getAutomovilPropio(), style);
        createCell(row, columnCount++, (empleado.getAutomovilMarca() == null) ? null : empleado.getAutomovilMarca(), style);
        createCell(row, columnCount++, (empleado.getAutomovilModelo() == null) ? null : empleado.getAutomovilModelo(), style);
        createCell(row, columnCount++, (empleado.getAutomovilValorAproximado() == null) ? null : empleado.getAutomovilValorAproximado(), style);
        createCell(row, columnCount++, (empleado.getConmutador() == 0) ? null : empleado.getConmutador(), style);
        createCell(row, columnCount++, (empleado.getFax() == 0) ? null : empleado.getFax(), style);
        createCell(row, columnCount++, (empleado.getMecanografia() == 0) ? null : empleado.getMecanografia(), style);
        createCell(row, columnCount++, (empleado.getTaquigrafia() == 0) ? null : empleado.getTaquigrafia(), style);
        createCell(row, columnCount++, (empleado.getCopiadora() == 0) ? null : empleado.getCopiadora(), style);
        createCell(row, columnCount++, (empleado.getTitulo() == null) ? null : empleado.getTitulo(), style);
        createCell(row, columnCount++, (empleado.getTipoIngreso() == null) ? null : empleado.getTipoIngreso(), style);
        */

    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        int i = 1;
        for (Envio envio : envios) {
            // log.info("Envio: "+envio.getId());
            writeDataLines(envio, i++);
        }
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }



}
