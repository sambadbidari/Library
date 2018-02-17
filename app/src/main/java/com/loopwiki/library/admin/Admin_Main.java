package com.loopwiki.library.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.loopwiki.library.R;

/**
 * Created by sambad on 2/15/18
 */

public class Admin_Main extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_admin);
        Button book = (Button) findViewById(R.id.btn_book);
        Button student = (Button) findViewById(R.id.btn_student);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin_Main.this, ScanActivity.class);
                startActivity(intent);
            }
        });
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin_Main.this, StudentActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        finish();
    }
}