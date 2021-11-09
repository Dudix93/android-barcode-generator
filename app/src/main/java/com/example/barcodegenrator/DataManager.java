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

    public static void loadBarcodesFromDatabase(DBHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        DataManager dm = getInstance();
        final String[] barcodesColumns = {
                BarcodeEntry.COL_BARCODE_ID,
                BarcodeEntry.COL_BARCODE_TEXT
        };
        dm.barcodessCursor = db.query(BarcodeEntry.TABLE_NAME, barcodesColumns, null, null, null, null, null);
        
        int barcodeIdPos = dm.barcodessCursor.getColumnIndex(BarcodeEntry.COL_BARCODE_ID);
        int barcodeTextPos = dm.barcodessCursor.getColumnIndex(BarcodeEntry.COL_BARCODE_TEXT);

        dm.barcodesList.clear();
        while (dm.barcodessCursor.moveToNext()) {
            int barcodeId = dm.barcodessCursor.getInt(barcodeIdPos);
            String barcodeText = dm.barcodessCursor.getString(barcodeTextPos);
            dm.barcodesList.add(new BarcodeModel(barcodeId, barcodeText));
        }
        dm.barcodessCursor.close();
    }
}
