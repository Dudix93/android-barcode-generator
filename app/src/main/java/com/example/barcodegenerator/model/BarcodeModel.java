package com.example.barcodegenerator.model;

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

    public String getText() { return text; }

    public void setText(String band) { this.text = text; }
}
