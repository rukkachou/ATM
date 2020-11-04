package com.example.rukka.atm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class AddActivity extends AppCompatActivity {

    private EditText edtDate;
    private EditText edtInfo;
    private EditText edtAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        edtDate = findViewById(R.id.edt_date);
        edtInfo = findViewById(R.id.edt_info);
        edtAmount = findViewById(R.id.edt_amount);
    }


    public void add(View view) {
        String date = edtDate.getText().toString();
        String info = edtInfo.getText().toString();
        int amount = Integer.parseInt(edtAmount.getText().toString());

        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("info", info);
        values.put("amount", amount);
        ExpenseHelper helper = new ExpenseHelper(this);
        long id = helper.getWritableDatabase().insert("expense", null, values);
        if (id > -1) {
            Snackbar.make(view, "Add Completed", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(view, "Add Failed", Snackbar.LENGTH_SHORT).show();
        }

        edtDate.setText("");
        edtInfo.setText("");
        edtAmount.setText("");
    }
}