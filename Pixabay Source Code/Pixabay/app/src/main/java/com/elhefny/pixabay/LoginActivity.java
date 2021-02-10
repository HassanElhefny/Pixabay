package com.elhefny.pixabay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    MaterialButton btn_login;
    TextInputEditText et_password, et_name;
    SharedPreferences sp;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeViews();
        sp = getSharedPreferences(getString(R.string.SharedPreferencesName), MODE_PRIVATE);
        editor = sp.edit();
        if (sp != null) {
            et_name.setText(sp.getString(getString(R.string.SharedPreferencesUserName), ""));
            et_password.setText(sp.getString(getString(R.string.SharedPreferencesUserPassword), ""));
        }

    }

    private void initializeViews() {
        btn_login = findViewById(R.id.login_btn_login);
        et_name = findViewById(R.id.login_et_name);
        et_password = findViewById(R.id.login_et_password);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        if (NetworkUtil.getConnectivityStatusString(this).equals("No internet is available")) {
            Toast.makeText(this, getString(R.string.NoInternetConnection), Toast.LENGTH_SHORT).show();
        } else {
            if (et_name.getText().length() == 0) {
                et_name.setError("Required");
                et_name.requestFocus();
            } else if (et_password.getText().length() == 0) {
                et_password.setError("Required");
                et_password.requestFocus();
            } else {
                editor.putString(getString(R.string.SharedPreferencesUserName), et_name.getText().toString().trim().toLowerCase());
                editor.putString(getString(R.string.SharedPreferencesUserPassword), et_password.getText().toString().trim().toLowerCase());
                editor.apply();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();
            }
        }
    }

}