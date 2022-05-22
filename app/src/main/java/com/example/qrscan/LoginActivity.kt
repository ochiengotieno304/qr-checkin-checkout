package com.example.qrscan

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*


class LoginActivity : AppCompatActivity() {


    private val USER_AGENT =
        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.64 Safari/537.36"
    private val loginFormUrl = "https://github.com/login"
    private val loginActionUrl = "https://github.com/session"

    private lateinit var usernameInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var homePage: Connection.Response


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameInput = findViewById(R.id.text_input_username)
        passwordInput = findViewById(R.id.text_input_password)
        val loginButton: Button = findViewById(R.id.button_sign_in)

//        login()

        loginButton.setOnClickListener {
            initializeHomePage()

//            if (getResponseCode() == 302) {
//                openMainActivity()
//            } else {
//                Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show()
//            }
        }
    }

    private fun getPassword(): String {
        return passwordInput.text.toString()
    }

    private fun getUsername(): String {
        return usernameInput.text.toString()
    }


    // redirect to MainActivity
    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, getUsername() + getPassword())
        }
        startActivity(intent)
    }

    //  get login status code
//    private fun getResponseCode(): Int {
//        return homePage.statusCode()
//
//    }

//################################    NEW METHOD IMPLEMENTATION   ################################
    // initialize homepage
@OptIn(DelicateCoroutinesApi::class)
private fun initializeHomePage() = GlobalScope.launch(Dispatchers.IO) {
    homePage = Jsoup.connect(loginActionUrl)
        .cookies(getCookies())
        .data(populateLoginForm())
        .method(Connection.Method.POST)
        .userAgent(USER_AGENT)
        .execute()
    println(homePage.parse().html())
    }


//    // get authToken
    private fun getAuthToken(): String {
        val loginForm = loginForm()
        val loginDocument: Document = loginForm.parse()

        return java.lang.String.valueOf(
            Objects.requireNonNull(
                loginDocument.select("#login > div.auth-form-body.mt-3 > form > input[type=hidden]:nth-child(1)")
                    .first()?.attr("value"),
            )
        )
    }
//
//    // get cookies
private fun getCookies(): MutableMap<String, String> {
       return loginForm().cookies()
    }
//
//    // populate login form with data
private fun populateLoginForm(): HashMap<String, String> {
    val formData: HashMap<String, String> = HashMap()

    formData["commit"] = "Sign in"
        formData["login"] = getUsername()
        formData["password"] = getPassword()
        formData["authenticity_token"] = getAuthToken()

    return formData
    }
//
//
//    // get login form
    private fun loginForm(): Connection.Response {
        return Jsoup.connect(loginFormUrl).method(Connection.Method.GET).userAgent(USER_AGENT)
            .execute()
    }


}

