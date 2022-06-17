package com.example.qrscan;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Objects;

public class Login extends AsyncTask<Void, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    protected Context context;
    private String username;
    private String password;
    static ProgressDialog progress_dialog;
    private String FULLNAME, STUDENT_EMAIL;
    @SuppressLint("StaticFieldLeak")
    private View view;

    public Login(Context context, String username, String password, View view) {
        this.context = context;
        this.username = username;
        this.password = password;
        this.view = view;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress_dialog = new ProgressDialog(context);
        progress_dialog.setMessage("Trying to authenticate you. This may take time depending your internet connection.");
        progress_dialog.setTitle("Please wait...");
        progress_dialog.setCancelable(false);
        progress_dialog.setCanceledOnTouchOutside(false);
        progress_dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        username = username.toUpperCase();
        String loginUrl = "https://portal.mmust.ac.ke/login/sign-in";
        String actionUrl = "http://portal.mmust.ac.ke/api/login/user";

        Connection.Response loginFormResponse;
        try {
            String USER_AGENT = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Mobile Safari/537.36";

            loginFormResponse = Jsoup.connect(loginUrl)
                    .method(Connection.Method.GET)
                    .userAgent(USER_AGENT)
                    .timeout(50 * 1000)
                    .execute();

            String formData = "{username:\"" + username + "\",password:\"" + password + "\" }";

            // save cookies for login request and headers
            HashMap<String, String> cookies = new HashMap<>(loginFormResponse.cookies());
            HashMap<String, String> headers = new HashMap<>(loginFormResponse.headers());

            Document loginDocument = Jsoup.connect(actionUrl)
                    .headers(headers)
                    .userAgent(USER_AGENT)
                    .ignoreContentType(true)
                    .requestBody(formData)
                    .timeout(50 * 1000)
                    .cookies(cookies)
                    .post();

            String response = loginDocument.body().text();
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.getString("success").equals("true")) {
                String message = jsonObject.getString("data");
                JSONObject jsonObject1 = new JSONObject(message);
                String user = jsonObject1.getString("userRegister");
                JSONObject jsonObject2 = new JSONObject(user);

                FULLNAME = jsonObject2.getString("names");
                STUDENT_EMAIL = jsonObject2.getString("email");
            } else {
                String errorMessage = jsonObject.getString("message");
                new Handler(Looper.getMainLooper()).post(() -> showDialogTimeOut("Authentication Failed", errorMessage));
            }


        } catch (Exception e) {
            e.printStackTrace();

            if (Objects.requireNonNull(e.getMessage()).contains("timeout")) {

                new Handler(Looper.getMainLooper()).post(() -> showDialogTimeOut("Timed out", "Make sure you have a strong network connection and try again"));


            } else if (e.getMessage().contains("Connection timed out")) {

                new Handler(Looper.getMainLooper()).post(() -> showDialogTimeOut("Something went wrong.", "University's servers took so long to respond. Please try again later."));

            } else {

                new Handler(Looper.getMainLooper()).post(() -> showDialogTimeOut("Authentication Failed", "Are you sure that you provided correct details?"));

            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progress_dialog.dismiss();
        if (FULLNAME != null) {
            new Handler(Looper.getMainLooper()).post(() -> context.startActivity(new Intent(context, MainActivity.class)));
        }

    }

    private void showDialogTimeOut(String title, String message) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Retry", ((dialog, which) -> {
                    new Login(context, username, password, view).execute();
                    dialog.dismiss();
                }))
                .setNegativeButton("Cancel", ((dialog, which) -> dialog.dismiss()))
                .create()
                .show();
    }
}
