package com.mx.tsmo.archivo.service;

import com.mx.tsmo.archivo.model.dto.MasivosResponseDto;
import com.mx.tsmo.cotizacion.model.domain.*;
import com.mx.tsmo.documentacion.model.domain.Documentacion;
import com.mx.tsmo.envios.model.domain.Envio;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
public class ArchivoServiceImpl implements ArchivoService {

    @Override
    public void leerArchivoExcel(File file) {
        List cellData = new ArrayList();
        if (file != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
                XSSFSheet hoja = workbook.getSheetAt(0);
                Iterator rowIterator = hoja.rowIterator();
                while (rowIterator.hasNext()) {
                    XSSFRow hssfRow = (XSSFRow) rowIterator.next();
                    Iterator iterator = hssfRow.cellIterator();;
                    List cellTemp = new ArrayList();
                    while (iterator.hasNext()) {
                        XSSFCell hssfCell = (XSSFCell) iterator.next();
                        cellTemp.add(hssfCell);
                    }
                    cellData.add(cellTemp);
                }
            } catch (Exception e) {
                log.error("ERROR: "+e.getMessage());
                e.printStackTrace();
            }
            this.obtener(cellData);
        } else {
            log.error("ERROR: null");
            return;
        }

    }

    @Override
    public List<Envio> obtenerEnviosExcel(File file) {
        List cellData = new ArrayList();
        if (file != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
                XSSFSheet hoja = workbook.getSheetAt(0);
                Iterator rowIterator = hoja.rowIterator();
                while (rowIterator.hasNext()) {
                    XSSFRow hssfRow = (XSSFRow) rowIterator.next();
                    Iterator iterator = hssfRow.cellIterator();;
                    List cellTemp = new ArrayList();
                    while (iterator.hasNext()) {
                        XSSFCell hssfCell = (XSSFCell) iterator.next();
                        cellTemp.add(hssfCell);
                    }
                    cellData.add(cellTemp);
                }
            } catch (Exception e) {
                log.error("ERROR: "+e.getMessage());
                e.printStackTrace();
            }
            return this.obtenerEnvios(cellData);
        } else {
            log.error("ERROR: null");
            return null;
        }
    }

    @Override
    public void obtener(List cellDataList) {
        for (int i = 0; i < cellDataList.size(); i++) {
            log.info("Row: "+i);
            List cellTempList = (List) cellDataList.get(i);

            for (int j = 0; j < cellTempList.size(); j++) {
                XSSFCell hssfCell = (XSSFCell) cellTempList.get(j);
                String stringCellValue = hssfCell.toString();
                log.info(stringCellValue);
            }
        }
    }

    @Override
    public List<Envio> obtenerEnvios(List cellDataList) {
        List<Envio> envios = new ArrayList<>();
        for (int i = 1; i < cellDataList.size(); i++) {
            log.info("Fila: "+i);
            List cellTempList = (List) cellDataList.get(i);
            // Para la inserción de telefonos
            List<Telefono> telOrigen = new ArrayList<>();
            // Double largo = new Double(((XSSFCell) cellTempList.get(25)).toString());

            telOrigen.add(Telefono.builder().numeroTelefono(((XSSFCell) cellTempList.get(11)).toString()).build());
            List<Telefono> telDestino = new ArrayList<>();
            telDestino.add(Telefono.builder().numeroTelefono(((XSSFCell) cellTempList.get(23)).toString()).build());
            List<Detalle> detalle = new ArrayList<>();
            try {
                detalle.add(Detalle.builder().
                        dimensiones(Dimensiones.builder()
                                .largo((((XSSFCell) cellTempList.get(25)).toString().equalsIgnoreCase("") || ((XSSFCell) cellTempList.get(25)).toString() == null) ? null : new Double(((XSSFCell) cellTempList.get(25)).toString()).shortValue())
                                .ancho((((XSSFCell) cellTempList.get(26)).toString().equalsIgnoreCase("") || ((XSSFCell) cellTempList.get(26)).toString() == null) ? null : new Double(((XSSFCell) cellTempList.get(25)).toString()).shortValue())
                                .alto((((XSSFCell) cellTempList.get(27)).toString().equalsIgnoreCase("") || ((XSSFCell) cellTempList.get(27)).toString() == null) ? null : new Double(((XSSFCell) cellTempList.get(25)).toString()).shortValue())
                                .peso((((XSSFCell) cellTempList.get(28)).toString().equalsIgnoreCase("") || ((XSSFCell) cellTempList.get(28)).toString() == null) ? null : new Double(((XSSFCell) cellTempList.get(25)).toString()).shortValue())
                                .build()
                        ).build());
            } catch (NullPointerException e) {
                log.error("Ocurrio error");
                log.error(e.getMessage());
            }
            Envio envio = Envio.builder()
                    .documentacion(
                            Documentacion.builder()
                            .cotizacion(
                                    Cotizacion.builder()
                                    .origen(
                                            Origen.builder()
                                            .remitente(((XSSFCell) cellTempList.get(2)).toString())
                                            .domicilio(
                                                    Domicilio.builder()
                                                            .calle(((XSSFCell) cellTempList.get(3)).toString())
                                                            .numeroExt(((XSSFCell) cellTempList.get(4)).toString())
                                                            .numeroInt(((XSSFCell) cellTempList.get(5)).toString())
                                                            .colonia(((XSSFCell) cellTempList.get(6)).toString())
                                                            .codigoPostal(((XSSFCell) cellTempList.get(7)).toString())
                                                            .ciudad(((XSSFCell) cellTempList.get(8)).toString())
                                                            .estado(((XSSFCell) cellTempList.get(9)).toString())
                                                            .pais(((XSSFCell) cellTempList.get(10)).toString())
                                                            .build()
                                            )
                                            .telefonos(telOrigen)
                                            .email(((XSSFCell) cellTempList.get(12)).toString())
                                            .build()

                                    )
                                    .destino(
                                            Destino.builder()
                                            .destinatario(((XSSFCell) cellTempList.get(13)).toString())
                                            .destinatario2(((XSSFCell) cellTempList.get(14)).toString())
                                            .domicilio(
                                                    Domicilio.builder()
                                                            .calle(((XSSFCell) cellTempList.get(15)).toString())
                                                            .numeroExt(((XSSFCell) cellTempList.get(16)).toString())
                                                            .numeroInt(((XSSFCell) cellTempList.get(17)).toString())
                                                            .colonia(((XSSFCell) cellTempList.get(18)).toString())
                                                            .codigoPostal(((XSSFCell) cellTempList.get(19)).toString())
                                                            .ciudad(((XSSFCell) cellTempList.get(20)).toString())
                                                            .estado(((XSSFCell) cellTempList.get(21)).toString())
                                                            .pais(((XSSFCell) cellTempList.get(22)).toString())
                                                            .build()
                                            )
                                            .telefonos(telDestino)
                                            .email(((XSSFCell) cellTempList.get(24)).toString())
                                            .build()
                                    )
                                    .detalle(detalle)
                                    .opciones(Opciones.builder()
                                            .tipoEntrega(((XSSFCell) cellTempList.get(29)).toString())
                                            .tipoEnvio(((XSSFCell) cellTempList.get(30)).toString())
                                            .build())
                                    .build()

                            ).build()
                    ).build();
            envios.add(envio);
        }
        return envios;
    }

    @Override
    public File file(String fileName) {
        File f = new File(fileName);
        if (f.exists()) {
            log.info("Existe archivo: "+fileName);
            return f;
        }
        return null;
    }

    @Override
    public MasivosResponseDto filtrar(List<Envio> envios) {
        log.info("Entra a filtrar por envios");
        MasivosResponseDto enviosFiltrados = new MasivosResponseDto();
        List<Envio> enviosExitosos = new ArrayList<>();
        List<Envio> enviosSinCobertura = new ArrayList<>();
        List<Envio> enviosError = new ArrayList<>();
        for (Envio envio : envios) {
            if (envio.getDocumentacion().getCotizacion().getDetalle().isEmpty()) {
                enviosError.add(envio);
            } else if (envio.getDocumentacion().getCotizacion().getCosto() == null) {
                enviosSinCobertura.add(envio);
            } else {
                enviosExitosos.add(envio);
            }
        }
        enviosFiltrados.setExito(enviosExitosos);
        enviosFiltrados.setSinCobertura(enviosSinCobertura);
        enviosFiltrados.setError(enviosError);
        return enviosFiltrados;
    }


}
