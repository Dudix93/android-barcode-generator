package com.example.barcodegenerator;

public interface BarcodeListener {

    void editBarcode(BarcodeModel barcodeModel);

    void deleteBarcode(int id);
}
