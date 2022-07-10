package com.example.qrscan;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qrscan.Retrofit.MyService;
import com.example.qrscan.Retrofit.RetrofitClient;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    TextView createAccountTextView;
    TextInputEditText editTextPassword, editTextRegNumber;
    Button buttonSignIn;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    MyService myService;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // init service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        myService = retrofitClient.create(MyService.class);

        // init view
        editTextRegNumber = findViewById(R.id.text_input_username);
        editTextPassword = findViewById(R.id.text_input_password);
        buttonSignIn = findViewById(R.id.button_sign_in);
        buttonSignIn.setOnClickListener(v -> loginUser(Objects.requireNonNull(editTextRegNumber.getText()).toString(),
                Objects.requireNonNull(editTextPassword.getText()).toString()));

        createAccountTextView = findViewById(R.id.text_create_account);
        createAccountTextView.setOnClickListener(v -> {
            View register_layout = LayoutInflater.from(LoginActivity.this)
                    .inflate(R.layout.register_layout, null);

            new MaterialAlertDialogBuilder(LoginActivity.this)
                    .setIcon(R.drawable.ic_user)
                    .setTitle("Register account")
                    .setMessage("Please fill out all fields")
                    .setView(register_layout)
                    .setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("REGISTER", (dialog, which) -> {
                        TextInputEditText editUsernameRegister = register_layout.findViewById(R.id.text_input_username_register);
                        TextInputEditText editPasswordRegister = register_layout.findViewById(R.id.text_input_password_register);

                        if (TextUtils.isEmpty(Objects.requireNonNull(editUsernameRegister.getText()).toString())) {
                            Toast.makeText(LoginActivity.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (TextUtils.isEmpty(Objects.requireNonNull(editPasswordRegister.getText()).toString())) {
                            Toast.makeText(LoginActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        registerUser(editUsernameRegister.getText().toString(),
                                editPasswordRegister.getText().toString());
                    }).show();
        });
    }

    private void registerUser(String username, String password) {
        compositeDisposable.add(myService.registerUser(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> Toast.makeText(LoginActivity.this, "" + response, Toast.LENGTH_SHORT).show()));
    }

    private void loginUser(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        compositeDisposable.add(myService.loginUser(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> Toast.makeText(LoginActivity.this, "" + response, Toast.LENGTH_SHORT).show()));

    }
}
