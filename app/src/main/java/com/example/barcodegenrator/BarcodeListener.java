package com.example.barcodegenrator;

public interface BarcodeListener {

    void editBarcode(BarcodeModel barcodeModel);

    void deleteBarcode(int id);
}
