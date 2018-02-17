package com.loopwiki.library;

/**
 * Created by sambad on 2/15/18
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.SparseArray;

import com.google.android.gms.vision.barcode.Barcode;
import com.loopwiki.library.admin.Admin_Main;
import com.loopwiki.library.users.Users_Main;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;


public class LoginActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener{
    Context mContext;
    SqliteHelper sqliteHelper;
    BarcodeReader mBarcodeReader;

    //This method is for handling fromHtml method deprecation
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        mBarcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);
        sqliteHelper = new SqliteHelper(this);
    }

    @Override
    public void onScanned(Barcode barcode) {
        mBarcodeReader.playBeep();
        String username= barcode.displayValue;
        User currentUser = sqliteHelper.Authenticate(new User(null, username));
        // ticket details activity by passing barcode
        if (username.equalsIgnoreCase("SUSSCS150041")) {
            //Admin login
            Intent intent = new Intent(LoginActivity.this, Admin_Main.class);
            startActivity(intent);
            finish();
        } else if (currentUser != null) {
            //user login
            Intent intent = new Intent(LoginActivity.this, Users_Main.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(LoginActivity.this, First_Activity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }
    @Override
    public void onBackPressed()
    {
        finish();
    }
}