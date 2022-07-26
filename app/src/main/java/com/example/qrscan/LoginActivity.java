package com.example.qrscan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrscan.Retrofit.MyService;
import com.example.qrscan.Retrofit.RetrofitClient;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    TextView createAccountTextView;
    TextInputEditText editTextPassword, editTextRegNumber;
    Button buttonSignIn;
    SharedPreferences sharedPreferences;
    String username;
    String password;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // init prefs
        sharedPreferences = getSharedPreferences("LoggedInUserPrefs", Context.MODE_PRIVATE);

        // init view
        editTextRegNumber = findViewById(R.id.text_input_username);
        editTextPassword = findViewById(R.id.text_input_password);
        buttonSignIn = findViewById(R.id.button_sign_in);
        buttonSignIn.setOnClickListener(v -> {
            username = Objects.requireNonNull(editTextRegNumber.getText()).toString();
            password = Objects.requireNonNull(editTextPassword.getText()).toString();
            loginUser(username, password);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", username);
            editor.putString("password", password);
            editor.apply();
        });

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

    Retrofit retrofitClient = RetrofitClient.getInstance();
    MyService myService = retrofitClient.create(MyService.class);

    private void registerUser(String username, String password) {
        Call<ResponseBody> call = myService.registerUser(username, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Toast.makeText(LoginActivity.this, "code: " + response.code(), Toast.LENGTH_LONG).show();
                Log.d("response", response.message());
                Log.d("response", String.valueOf(response.isSuccessful()));
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
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

        Call<ResponseBody> call = myService.loginUser(username, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "sign in successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else if (response.code() == 401) {
                    Toast.makeText(LoginActivity.this, "invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
