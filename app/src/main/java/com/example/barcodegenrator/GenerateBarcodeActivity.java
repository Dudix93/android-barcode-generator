package com.example.barcodegenrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.ArrayList;

public class GenerateBarcodeActivity extends AppCompatActivity {
    private EditText barcodeTextEditText;
    private ImageView barcodeImageView;
    private Button generateBarcodeButton;
    private Button saveBarcodeButton;
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private ContentValues values;
    private Bitmap bitmap;
    private BarcodeModel barcodeModel;
    private ArrayList<String> barcodesValuesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_barcode);

        dbHelper = new DBHelper(this);
        values = new ContentValues();

        barcodeTextEditText = findViewById(R.id.barcode_text_edit_text);
        barcodeImageView = findViewById(R.id.barcode_image_view);
        generateBarcodeButton = findViewById(R.id.generate_barcode_button);
        saveBarcodeButton = findViewById(R.id.save_barcode_button);

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getStringArrayList(getResources().getString(R.string.barcodes_list_variable)) != null) {
                barcodesValuesList = getIntent().getExtras().getStringArrayList(getResources().getString(R.string.barcodes_list_variable));
            }
            if (getIntent().getExtras().getSerializable("barcodeModel") != null) {
                barcodeModel = (BarcodeModel) getIntent().getExtras().getSerializable("barcodeModel");
                barcodeTextEditText.setText(barcodeModel.getText());
                saveBarcodeButton.setEnabled(false);
            }
        }

        barcodeImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_qr_code));
        generateBarcodeButton.setEnabled(barcodeTextEditText.getText().length() != 0);
        saveBarcodeButton.setEnabled(barcodeTextEditText.getText().length() != 0);

        barcodeTextEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                generateBarcodeButton.setEnabled(charSequence.length() != 0);
                saveBarcodeButton.setEnabled(charSequence.length() != 0);
            }

            @Override
            public void afterTextChanged(Editable editable) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                generateBarcodeButton.setEnabled(charSequence.length() != 0);
                saveBarcodeButton.setEnabled(charSequence.length() != 0);

                if (barcodesValuesList.size() != 0) {
                    for (String barcode : barcodesValuesList) {
                        if (barcode.equals(charSequence.toString())) {
                            saveBarcodeButton.setEnabled(false);
                            barcodeTextEditText.setTextColor(getResources().getColor(R.color.red));
                            barcodeTextEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0,0,0);
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.barcode_exists), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        else {
                            saveBarcodeButton.setEnabled(true);
                            barcodeTextEditText.setTextColor(getResources().getColor(R.color.black));
                            barcodeTextEditText.setCompoundDrawables(null,null,null,null);
                        }
                    }
                }
            }
        });

        generateBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmap = new BarcodeGenerator().generateBarcode(barcodeImageView, barcodeTextEditText.getText().toString());
                barcodeImageView.setImageBitmap(bitmap);
            }
        });

        saveBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (barcodeTextEditText.getText().length() != 0) {
                    db = dbHelper.getWritableDatabase();
                    values.put(BarcodeEntry.COL_BARCODE_TEXT, barcodeTextEditText.getText().toString());
                    if (barcodeModel != null) {
                        db.update(BarcodeEntry.TABLE_NAME, values, BarcodeEntry.COL_BARCODE_ID + " = "+ barcodeModel.getId(), null);
                        makeToastMessage(R.string.barcode_updated);
                    }
                    else {
                        db.insert(BarcodeEntry.TABLE_NAME, null, values);
                        makeToastMessage(R.string.barcode_saved);
                    }
                    finish();
                }
                else {
                    makeToastMessage(R.string.barcode_empty);                
                }
            }
        });
    }

    public void makeToastMessage(int resource) {
        Toast.makeText(getApplicationContext(), getResources().getString(resource), Toast.LENGTH_SHORT).show();
    }
}