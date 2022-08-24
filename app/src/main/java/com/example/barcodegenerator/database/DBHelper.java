package com.example.barcodegenerator.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VER = 1;
    private static final String DB_NAME = "barcodesDB";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(BarcodeEntry.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BarcodeEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void deleteBarcode(SQLiteDatabase sqLiteDatabase, int id) {
        sqLiteDatabase.execSQL("DELETE FROM  " + BarcodeEntry.TABLE_NAME + " where "+ BarcodeEntry.COL_BARCODE_ID + " = " + id);
    }

    public boolean isBarcodeListed(SQLiteDatabase sqLiteDatabase, String barcodeText) {
        Cursor res = sqLiteDatabase.rawQuery("SELECT " + BarcodeEntry.COL_BARCODE_ID +
                " FROM  " + BarcodeEntry.TABLE_NAME +
                " where " + BarcodeEntry.COL_BARCODE_TEXT + " = '" + barcodeText + "'", null);
        return res.getCount() > 0;
    }

    public int getBarcodeId(SQLiteDatabase sqLiteDatabase, String barcodeText) {
        Cursor res = sqLiteDatabase.rawQuery("SELECT " +
                BarcodeEntry.TABLE_NAME + "." + BarcodeEntry.COL_BARCODE_ID +
                " FROM  " + BarcodeEntry.TABLE_NAME +
                " where " + BarcodeEntry.TABLE_NAME + "." + BarcodeEntry.COL_BARCODE_TEXT + " = '" + barcodeText + "'" , null);
        res.moveToFirst();
        int bandIdPos = res.getColumnIndex(BarcodeEntry.COL_BARCODE_ID);
        return res.getInt(bandIdPos);
    }
    
}
