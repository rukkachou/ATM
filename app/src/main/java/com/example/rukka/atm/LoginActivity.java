package com.example.rukka.atm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText edtAccount;
    private EditText edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtAccount = findViewById(R.id.edt_account);
        edtPassword = findViewById(R.id.edt_password);
    }

    public void login(View view) {
        String userAccount = edtAccount.getText().toString();
        String userPassword = edtPassword.getText().toString();
        if (userAccount.equals("Nori") && userPassword.equals("030")) {
            setResult(RESULT_OK);
            finish();
        }
    }

    public void quit(View view) {

    }
}