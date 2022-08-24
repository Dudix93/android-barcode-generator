package com.example.barcodegenerator.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.barcodegenerator.BarcodeListener;
import com.example.barcodegenerator.database.DBHelper;
import com.example.barcodegenerator.database.DataManager;
import com.example.barcodegenerator.R;
import com.example.barcodegenerator.adapter.BarcodesAdapter;
import com.example.barcodegenerator.model.BarcodeModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;

public class BarcodesActivity extends AppCompatActivity implements BarcodeListener {

    private ArrayList<BarcodeModel> barcodesList;
    private ArrayList<String> barcodesValuesList;
    private ListView listView;
    private DBHelper dbHelper;
    private DataManager dataManager;
    private Intent intent;
    private SQLiteDatabase db;
    private static BarcodesAdapter adapter;
    static final int REQUEST_CODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            loadBarcodesValues();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBarcodesValues();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcodes);
        dbHelper = new DBHelper(this);
        barcodesValuesList = new ArrayList<String>();
        loadBarcodesValues();
        FloatingActionButton fab = findViewById(R.id.add_barcode);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), GenerateBarcodeActivity.class);
                intent.putExtra(getResources().getString(R.string.barcodes_list_variable), barcodesValuesList);
                startActivity(intent);
            }
        });
    }

    public void loadBarcodesValues() {
        listView = (ListView)findViewById(R.id.barcodes_list);
        dataManager = DataManager.getInstance();
        dataManager.loadBarcodesFromDatabase(dbHelper);
        barcodesList = dataManager.barcodesList;
        if (barcodesList.size() > 0) {
            for (BarcodeModel barcode : barcodesList) {
                barcodesValuesList.add(barcode.getText());
            }
        }
        adapter = new BarcodesAdapter(barcodesList, this);
        listView.setAdapter(adapter);
    }

    public void deleteBarcode(int gigId) {
        db = dbHelper.getWritableDatabase();
        dbHelper.deleteBarcode(db, gigId);
        loadBarcodesValues();
        Snackbar.make(this.findViewById(android.R.id.content), R.string.barcode_deleted, 2000).show();
    }

    public void editBarcode(BarcodeModel barcodeModel) {
        intent = new Intent(this, GenerateBarcodeActivity.class);
        intent.putExtra("barcodeModel", barcodeModel);
        intent.putExtra(getResources().getString(R.string.barcodes_list_variable), barcodesValuesList);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

}