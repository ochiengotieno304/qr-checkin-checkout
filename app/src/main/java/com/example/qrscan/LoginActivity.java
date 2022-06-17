package com.example.qrscan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText editTextPassword;
    TextInputEditText editTextRegNumber;
    Button buttonSignIn;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextRegNumber = findViewById(R.id.text_input_username);
        editTextPassword = findViewById(R.id.text_input_password);
        buttonSignIn = findViewById(R.id.button_sign_in);
        progressDialog = new ProgressDialog(this);

        buttonSignIn.setOnClickListener(v -> {
            String password = Objects.requireNonNull(editTextPassword.getText()).toString().trim();
            String regNumber = Objects.requireNonNull(editTextRegNumber.getText()).toString().trim();

            if (password.isEmpty() || regNumber.isEmpty()) {
                new MaterialAlertDialogBuilder(LoginActivity.this)
                        .setTitle("Credentials cannot be empty")
                        .setMessage("Please provide valid credentials")
                        .setCancelable(true)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                return;
            }

            new Login(LoginActivity.this, regNumber, password, buttonSignIn);
            getMain();
        });
    }

    public void getMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
