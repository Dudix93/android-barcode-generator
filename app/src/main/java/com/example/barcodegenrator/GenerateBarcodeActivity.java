package com.example.barcodegenrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class GenerateBarcodeActivity extends AppCompatActivity {
    private EditText barcodeTextEditText;
    private ImageView barcodeImageView;
    private Button generateBarcodeButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        barcodeTextEditText = findViewById(R.id.barcodeTextEditText);
        barcodeImageView = findViewById(R.id.barcodeImageView);
        generateBarcodeButton = findViewById(R.id.generateBarcodeButton);

        barcodeImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_qr_code));

        generateBarcodeButton.setEnabled(barcodeTextEditText.getText().length() == 0 ? false : true);

        barcodeTextEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                generateBarcodeButton.setEnabled(charSequence.length() == 0 ? false : true);
            }

            @Override
            public void afterTextChanged(Editable editable) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                generateBarcodeButton.setEnabled(charSequence.length() == 0 ? false : true);
            }
        });
    }

    public void generateBarcode(View view){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(barcodeTextEditText.getText().toString(), BarcodeFormat.CODE_128, barcodeImageView.getWidth(), barcodeImageView.getHeight());
            Bitmap bitmap = Bitmap.createBitmap(barcodeImageView.getWidth(), barcodeImageView.getHeight(), Bitmap.Config.RGB_565);
            for (int i = 0; i<barcodeImageView.getWidth(); i++){
                for (int j = 0; j<barcodeImageView.getHeight(); j++){
                    bitmap.setPixel(i,j,bitMatrix.get(i,j)? Color.BLACK:Color.WHITE);
                }
            }
            barcodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}