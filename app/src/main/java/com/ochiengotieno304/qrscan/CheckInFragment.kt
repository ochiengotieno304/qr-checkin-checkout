package com.ochiengotieno304.qrscan

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.ochiengotieno304.qrscan.R
import com.ochiengotieno304.qrscan.Retrofit.MyService
import com.ochiengotieno304.qrscan.Retrofit.RetrofitClient
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.fragment_check_in.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class CheckInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_check_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        qr_button.setOnClickListener {
            val intentIntegrator = IntentIntegrator(this@CheckInFragment.requireActivity())
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            intentIntegrator.setOrientationLocked(true)
            resultLauncher.launch(intentIntegrator.createScanIntent())
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val sharedPreferences: SharedPreferences =
                    this@CheckInFragment.requireActivity()
                        .applicationContext
                        .getSharedPreferences("LoggedInUserPrefs", Context.MODE_PRIVATE)
                val token = sharedPreferences.getString("token", "")
                checkIn("Bearer $token")

            }
        }

    private var retrofitClient: Retrofit = RetrofitClient.getInstance()
    private var myService: MyService = retrofitClient.create(MyService::class.java)

    private fun checkIn(token: String) {
        val call: Call<ResponseBody> = myService.checkIn(token)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful) {
                    Toast.makeText(activity, "check in successful", Toast.LENGTH_SHORT)
                        .show()
                } else if (response.code() == 401) {
                    Log.i("request", call.request().toString())
                    Toast.makeText(activity, "something isn't right", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(activity, "" + t, Toast.LENGTH_SHORT).show()
            }
        })
    }
}

