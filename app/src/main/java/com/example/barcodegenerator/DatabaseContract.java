package com.example.barcodegenerator;

class BarcodeEntry {
    public static final String TABLE_NAME = "Barcode";
    public static final String COL_BARCODE_ID = "Id";
    public static final String COL_BARCODE_TEXT = "Text";

    public static String createTable() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                COL_BARCODE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_BARCODE_TEXT + " TEXT )";
    }
}