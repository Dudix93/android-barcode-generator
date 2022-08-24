package com.example.barcodegenerator.activity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.example.barcodegenerator.BarcodeGenerator;
import com.example.barcodegenerator.R;
import com.example.barcodegenerator.database.DBHelper;
import com.example.barcodegenerator.database.DataManager;
import com.example.barcodegenerator.model.BarcodeModel;

import java.util.ArrayList;


public class WidgetActivity extends AppWidgetProvider {

    private ArrayList<String> barcodesValuesList;
    private ArrayList<BarcodeModel> barcodesList;
    private DBHelper dbHelper;
    private DataManager dataManager;
    private Bitmap bitmap;
    private ImageView barcodeImageView;
    static int barcodeId = 0;
    private static final String OnNextButtonClick = "onNextButtonClickTag";
    private static final String OnPreviousButtonClick = "onPreviousButtonClickTag";

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        loadBarcodesValues(context);
        updateBarcodeWidget(context, appWidgetManager, appWidgetIds);
    }

    public void loadBarcodesValues(Context context) {
        dbHelper = new DBHelper(context.getApplicationContext());
        barcodesValuesList = new ArrayList<String>();
        dataManager = DataManager.getInstance();
        dataManager.loadBarcodesFromDatabase(dbHelper);
        barcodesList = dataManager.barcodesList;
        if (barcodesList.size() > 0) {
            for (BarcodeModel barcode : barcodesList) {
                barcodesValuesList.add(barcode.getText());
            }
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        loadBarcodesValues(context);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), WidgetActivity.class.getName());

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

        if (OnNextButtonClick.equals(intent.getAction())){
            if (barcodeId < barcodesValuesList.size()-1) barcodeId++;
            else if (barcodeId == barcodesValuesList.size() - 1) barcodeId = 0;
            updateBarcodeWidget(context, appWidgetManager, appWidgetIds);
        }

        if (OnPreviousButtonClick.equals(intent.getAction())) {
            if (barcodeId > 0) barcodeId--;
            else if (barcodeId == 0) barcodeId = barcodesValuesList.size() - 1;
            updateBarcodeWidget(context, appWidgetManager, appWidgetIds);
        }

    };


    public void updateBarcodeWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        barcodeImageView = new ImageView(context.getApplicationContext(), null);
        barcodeImageView.setMaxWidth(150);
        barcodeImageView.setMaxWidth(90);
        for (int i=0; i<appWidgetIds.length; i++) {
            int currentWidgetId = appWidgetIds[i];
            Bundle awo = appWidgetManager.getAppWidgetOptions(currentWidgetId);
            int widgetHeight = awo.getInt("appWidgetMaxHeight");
            int widgetWidth = awo.getInt("appWidgetMaxWidth");
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_widget);
            views.setOnClickPendingIntent(R.id.widget_next_barcode, getPendingSelfIntent(context, OnNextButtonClick));
            views.setOnClickPendingIntent(R.id.widget_previous_barcode, getPendingSelfIntent(context, OnPreviousButtonClick));
            bitmap = new BarcodeGenerator().generateBarcode(widgetHeight, widgetWidth, barcodesValuesList.get(barcodeId));
            views.setImageViewBitmap(R.id.barcode_image_view, bitmap);
            views.setTextViewText(R.id.barcode_text_view, barcodesValuesList.get(barcodeId));
            appWidgetManager.updateAppWidget(currentWidgetId, views);
        }
    }
}
