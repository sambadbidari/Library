package com.loopwiki.library.admin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.loopwiki.library.Message;
import com.loopwiki.library.R;
import com.loopwiki.library.SqliteHelper;

/**
 * Created by sambad on 2/15/18
 */

public class StudentActivity extends AppCompatActivity {
    EditText Name;
    EditText updateold;
    EditText updatenew;
    EditText delete;
    SqliteHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentactivity);
        Name= (EditText) findViewById(R.id.editName);
        updateold= (EditText) findViewById(R.id.editText3);
        updatenew= (EditText) findViewById(R.id.editText5);
        delete = (EditText) findViewById(R.id.editText6);

        helper = new SqliteHelper(this);
    }
    public void addUser(View view)
    {
        String t1 = Name.getText().toString();
        if (!helper.isUserExists(t1)) {
            if (t1.isEmpty()) {
                Message.message(getApplicationContext(), "User Name Empty");
            } else {
                long id = helper.insertData(t1);
                if (id <= 0) {
                    Message.message(getApplicationContext(), "Insertion Unsuccessful");
                    Name.setText("");
                } else {
                    Message.message(getApplicationContext(), "Insertion Successful");
                    Name.setText("");
                }
            }
        }
        else {
            Message.message(getApplicationContext(),"User Already Exist");
        }
    }

    public void viewdata(View view)
    {
        String data = helper.getData();
        Message.message(this,data);
    }

    public void update( View view)
    {
        String u1 = updateold.getText().toString();
        String u2 = updatenew.getText().toString();
        if(u1.isEmpty() || u2.isEmpty())
        {
            Message.message(getApplicationContext(),"Enter Data");
        }
        else
        {
            int a= helper.updateName( u1, u2);
            if(a<=0)
            {
                Message.message(getApplicationContext(),"Unsuccessful");
                updateold.setText("");
                updatenew.setText("");
            } else {
                Message.message(getApplicationContext(),"Updated");
                updateold.setText("");
                updatenew.setText("");
            }
        }

    }
    public void delete( View view)
    {
        String uname = delete.getText().toString();
        if(uname.isEmpty())
        {
            Message.message(getApplicationContext(),"Enter Data");
        }
        else{
            int a= helper.delete(uname);
            if(a<=0)
            {
                Message.message(getApplicationContext(),"Unsuccessful");
                delete.setText("");
            }
            else
            {
                Message.message(this, "DELETED");
                delete.setText("");
            }
        }
    }
}