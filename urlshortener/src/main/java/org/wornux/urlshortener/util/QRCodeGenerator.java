package org.wornux.urlshortener.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BitMatrix;
import com.google.zxing.QRCodeWriter;

public class QRCodeGenerator {
    public static BitMatrix generateQRCode(String url) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            return writer.encode(url, BarcodeFormat.QR_CODE, 200, 200);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
