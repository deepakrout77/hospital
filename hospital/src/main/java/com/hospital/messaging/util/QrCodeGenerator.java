package com.hospital.messaging.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class QrCodeGenerator {

    // Base64 QR Code (used for web display)
    public static String generateQrCodeBase64(String content) throws WriterException, IOException {
        BitMatrix matrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, 200, 200);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return "data:image/png;base64," + java.util.Base64.getEncoder().encodeToString(imageBytes);
    }

    // PNG QR Code (used for download)
    public static byte[] generateQrCodeImage(String content) throws WriterException, IOException {
        BitMatrix matrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);
        return outputStream.toByteArray();
    }
}
