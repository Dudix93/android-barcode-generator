package com.example.barcodegenerator;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;


public class WidgetActivity extends AppWidgetProvider {

    private ArrayList<String> barcodesValuesList;
    private ArrayList<BarcodeModel> barcodesList;
    private DBHelper dbHelper;
    private DataManager dataManager;
    private Bitmap bitmap;
    private ImageView barcodeImageView;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        dbHelper = new DBHelper(context.getApplicationContext());
        barcodesValuesList = new ArrayList<String>();
        barcodeImageView = new ImageView(context.getApplicationContext(), null);
        barcodeImageView.setMaxWidth(150);
        barcodeImageView.setMaxWidth(90);
        loadBarcodesValues();

        for (int i=0; i<appWidgetIds.length; i++) {
            int currentWidgetId = appWidgetIds[i];
            Bundle awo = appWidgetManager.getAppWidgetOptions(currentWidgetId);
            int widgetHeight = awo.getInt("appWidgetMaxHeight");
            int widgetWidth = awo.getInt("appWidgetMaxWidth");
            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.activity_widget);
            bitmap = new BarcodeGenerator().generateBarcode(widgetHeight, widgetWidth, barcodesValuesList.get(0));
            views.setImageViewBitmap(R.id.barcode_image_view, bitmap);
            appWidgetManager.updateAppWidget(currentWidgetId, views);
            Toast.makeText(context, "widget added", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadBarcodesValues() {
        dataManager = DataManager.getInstance();
        dataManager.loadBarcodesFromDatabase(dbHelper);
        barcodesList = dataManager.barcodesList;
        if (barcodesList.size() > 0) {
            for (BarcodeModel barcode : barcodesList) {
                barcodesValuesList.add(barcode.getText());
            }
        }
    }

}
