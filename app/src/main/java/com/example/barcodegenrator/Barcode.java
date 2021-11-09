package com.example.barcodegenrator;

import android.media.Image;

import java.io.Serializable;

public class Barcode implements Serializable {
    private String barcode_text;
    private Image barcode;

    public Barcode(String barcode_text, Image barcode) {
        this.barcode_text = barcode_text;
        this.barcode = barcode;
    }

    public Image getImage() { return this.barcode; }

    public void setImage(Image barcode) { this.barcode = barcode; }

    public String getText() { return this.barcode_text; }

    public void setText(String barcode_text) { this.barcode_text = barcode_text; }
}
