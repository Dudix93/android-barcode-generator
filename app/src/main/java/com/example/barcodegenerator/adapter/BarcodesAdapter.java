package com.example.barcodegenerator.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;

import com.example.barcodegenerator.BarcodeGenerator;
import com.example.barcodegenerator.activity.BarcodesActivity;
import com.example.barcodegenerator.fragment.DeleteDialogFragment;
import com.example.barcodegenerator.R;
import com.example.barcodegenerator.model.BarcodeModel;

import java.util.ArrayList;

public class BarcodesAdapter extends ArrayAdapter<BarcodeModel>  {
    private Bitmap bitmap;
    private TextView barcodeValueTextView;
    private PopupWindow popupWindow;
    private View popupView;
    Context mContext;

    private static class ViewHolder {
        TextView barcode_text;
    }

    public BarcodesAdapter(ArrayList<BarcodeModel> data, Context context){
        super(context, R.layout.array_list_item_barcode, data);
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
            viewHolder.barcode_text = convertView.findViewById(R.id.barcode_text_text_view_list_item);
            view = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            view = convertView;
        }

            viewHolder.barcode_text.setText(barcodeModel.getText());

        convertView.findViewById(R.id.barcode_entry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBarcodePopup(view, barcodeModel);
            }
        });

        return convertView;
    }

    public void showBarcodePopup(View view, BarcodeModel barcodeModel) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        popupView = inflater.inflate(R.layout.popup_barcode, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        popupWindow = new PopupWindow(popupView, width, height, true);

        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        ImageView barcode = popupView.findViewById(R.id.barcode_image_view);

        barcode.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                    bitmap = new BarcodeGenerator().generateBarcode(barcode.getHeight(), barcode.getWidth(), barcodeModel.getText());
                    barcode.setImageBitmap(bitmap);
                    barcodeValueTextView = popupView.findViewById(R.id.popup_barcode_value);
                    barcodeValueTextView.setText(barcodeModel.getText());
            }
        });

        dimBehind(popupWindow);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });


        Button editBarcodeButton = popupWindow.getContentView().findViewById(R.id.edit_barcode_button);
        Button deleteBarcodeButton = popupWindow.getContentView().findViewById(R.id.delete_barcode_button);

        editBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof BarcodesActivity) {
                    ((BarcodesActivity) mContext).editBarcode(barcodeModel);
                    popupWindow.dismiss();
                }
            }
        });

        deleteBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof BarcodesActivity) {
                    DialogFragment deleteGigFragment = new DeleteDialogFragment();
                    Bundle args = new Bundle();
                    args.putInt("id", barcodeModel.getId());
                    if (mContext instanceof BarcodesActivity) {
                        args.putInt("msg", R.string.barcode_delete);
                        deleteGigFragment.setArguments(args);
                        deleteGigFragment.show(((BarcodesActivity) getContext()).getSupportFragmentManager(), "deleteGig");
                        popupWindow.dismiss();
                    }

                }
            }
        });
    }

    public static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.3f;
        wm.updateViewLayout(container, p);
    }

}