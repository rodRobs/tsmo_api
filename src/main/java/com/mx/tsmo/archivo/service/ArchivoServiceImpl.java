package com.mx.tsmo.archivo.service;

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
    public void obtener(List cellDataList) {
        for (int i = 0; i < cellDataList.size(); i++) {
            List cellTempList = (List) cellDataList.get(i);

            for (int j = 0; j < cellTempList.size(); j++) {
                XSSFCell hssfCell = (XSSFCell) cellTempList.get(j);
                String stringCellValue = hssfCell.toString();
                log.info(stringCellValue);
            }
        }
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


}
