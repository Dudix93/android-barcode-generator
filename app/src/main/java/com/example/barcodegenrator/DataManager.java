package com.example.barcodegenrator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static DataManager ourInstance = null;

    public ArrayList<BarcodeModel> barcodesList= new ArrayList<>();

    public Cursor barcodessCursor;

    public static  DataManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new DataManager();
        }
        return ourInstance;
    }
    public static void loadBarcodesFromDatabase(DBHelper dbHelper, Cursor cursor) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        DataManager dm = getInstance();
        final String[] barcodesColumns = {
                BarcodeEntry.COL_BARCODE_ID,
                BarcodeEntry.COL_BARCODE_TEXT
        };
        dm.barcodessCursor = db.query(BarcodeEntry.TABLE_NAME, barcodesColumns, null, null, null, null, null);
        
        int barcodeIdPos = cursor.getColumnIndex(BarcodeEntry.COL_BARCODE_ID);
        int barcodeTextPos = cursor.getColumnIndex(BarcodeEntry.COL_BARCODE_TEXT);

        dm.barcodesList.clear();
        while (cursor.moveToNext()) {
            int barcodeId = cursor.getInt(barcodeIdPos);
            String barcodeText = cursor.getString(barcodeTextPos);
            dm.barcodesList.add(new BarcodeModel(barcodeId, barcodeText));
        }
        cursor.close();
    }
}
