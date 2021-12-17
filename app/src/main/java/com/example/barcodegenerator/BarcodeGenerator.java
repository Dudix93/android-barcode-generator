package com.example.barcodegenerator;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class BarcodeGenerator {

    private Bitmap bitmap;

    public Bitmap generateBarcode(ImageView barcodeImageView, String barcodeText){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(barcodeText, BarcodeFormat.CODE_128, barcodeImageView.getWidth(), barcodeImageView.getHeight());
            bitmap = Bitmap.createBitmap(barcodeImageView.getWidth(), barcodeImageView.getHeight(), Bitmap.Config.RGB_565);
            for (int i = 0; i<barcodeImageView.getWidth(); i++){
                for (int j = 0; j<barcodeImageView.getHeight(); j++){
                    bitmap.setPixel(i,j,bitMatrix.get(i,j)? Color.BLACK:Color.WHITE);
                }
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
