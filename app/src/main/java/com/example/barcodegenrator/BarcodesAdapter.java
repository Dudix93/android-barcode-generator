package com.example.barcodegenrator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class BarcodesAdapter extends ArrayAdapter<BarcodeModel>  {
    private ArrayList<BarcodeModel> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView barcode_text;
        ImageView barcode;
    }

    public BarcodesAdapter(ArrayList<BarcodeModel> data, Context context){
        super(context, R.layout.array_list_item_barcode, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final BarcodeModel barcodeModel = getItem(position);
        final ViewHolder viewHolder;
        final View view;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.array_list_item_barcode, parent, false);
            viewHolder.barcode = convertView.findViewById(R.id.barcode_image_view_list_item);
            viewHolder.barcode_text = convertView.findViewById(R.id.barcode_text_text_view_list_item);
            view = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            view = convertView;
        }

            viewHolder.barcode_text.setText(barcodeModel.getText());
//            new BarcodeGenerator().generateBarcode(viewHolder.barcode, barcodeModel.getText());
            viewHolder.barcode.setImageResource(R.drawable.ic_qr_code);



        view.findViewById(R.id.barcode_entry).setOnLongClickListener(new View.OnLongClickListener() {

            private final int barcodeId = barcodeModel.getId();

            @Override
            public boolean onLongClick(View view) {
                if (mContext instanceof BarcodesActivity) {
                    DialogFragment optionsDialogFragment = new EntryOptionsDialogFragment();
                    Bundle args = new Bundle();
                    args.putInt("id", barcodeId);
                    args.putSerializable("barcode", barcodeModel);
                    optionsDialogFragment.setArguments(args);
                    optionsDialogFragment.show(((BarcodesActivity) mContext).getSupportFragmentManager(), "optionsBarcode");
                }
                return true;
            }
        });

        convertView.findViewById(R.id.barcode_entry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent barcodeActivityIntent = new Intent(view.getContext(), GenerateBarcodeActivity.class);
                barcodeActivityIntent.putExtra("barcodeText", barcodeModel.getText());
                mContext.startActivity(barcodeActivityIntent);
            }
        });

        return convertView;
    }
}