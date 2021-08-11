package com.mx.tsmo.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import javax.imageio.ImageIO;

@Slf4j
public class CodigoQR {

    public static void main(String[] args) {
        try {
            log.info("Entra a crear el QR");
            //String content = "http://www.google.com";
            String content = "https://tsmo.com.mx/empleados/tsmo180900";
            String filePath = "";
            String fileType = "png";
            int size = 125;
            UUID uuid = UUID.randomUUID();
            String randomUUIDString = uuid.toString();
            QRCodeWriter qrcode = new QRCodeWriter();
            BitMatrix matrix = qrcode.encode(content, BarcodeFormat.QR_CODE, size, size);
            File qrFile = new File(filePath + "urieltrainingqr." + fileType);
            int matrixWidth = matrix.getWidth();
            BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
            image.createGraphics();

            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, matrixWidth, matrixWidth);
            graphics.setColor(Color.BLACK);

            for (int b = 0; b < matrixWidth; b++) {
                for (int j = 0; j < matrixWidth; j++) {
                    if (matrix.get(b, j)) {
                        graphics.fillRect(b, j, 1, 1);
                    }
                }
            }
            ImageIO.write(image, fileType, qrFile);
            log.info("Se ha creado el QR");
        } catch (WriterException ex) {
            log.info("WriterException: "+ex.getMessage());
            //log.info(QRTest.class.getName()).log(Level.SEVERE, null, ex);
            //Logger.getLogger(QRTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            log.info("IOException ex: "+ex.getMessage());
            //Logger.getLogger(QRTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
