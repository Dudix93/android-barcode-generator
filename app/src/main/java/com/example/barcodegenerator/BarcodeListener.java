package com.example.barcodegenerator;

import com.example.barcodegenerator.model.BarcodeModel;

public interface BarcodeListener {

    void editBarcode(BarcodeModel barcodeModel);

    void deleteBarcode(int id);
}
