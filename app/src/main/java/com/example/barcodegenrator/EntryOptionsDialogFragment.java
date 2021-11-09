package com.example.barcodegenrator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EntryOptionsDialogFragment extends DialogFragment {
    private int id;
    private String msg;
    private BarcodeModel barcode;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        id = getArguments().getInt("id");
        barcode = (BarcodeModel) getArguments().getSerializable("barcode");
        dbHelper = new DBHelper(this.getContext());
        db = dbHelper.getWritableDatabase();

        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DialogFragment deleteGigFragment = new DeleteDialogFragment();
                Bundle args = new Bundle();
                args.putInt("id", id);
                if (getActivity() instanceof BarcodesActivity) {
                    args.putInt("msg", R.string.barcode_delete);
                    deleteGigFragment.setArguments(args);
                    deleteGigFragment.show(((BarcodesActivity) getContext()).getSupportFragmentManager(), "deleteGig");
                }
            }
        })
                .setNegativeButton(R.string.edit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (getActivity() instanceof BarcodesActivity) {
                            ((BarcodesActivity) getContext()).editBarcode(barcode);
                        }
                    }
                })
                .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //User cancelled the dialog
                    }
                });
        return builder.create();
    }
}