package com.example.barcodegenrator;

import java.io.Serializable;

public class BarcodeModel implements Serializable {
    private int id;
    private String text;

    public BarcodeModel(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String gettext() { return text; }

    public void settext(String band) { this.text = text; }
}
