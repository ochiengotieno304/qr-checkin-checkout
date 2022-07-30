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
import com.google.zxing.integration.android.IntentIntegrator
import com.ochiengotieno304.qrscan.Retrofit.MyService
import com.ochiengotieno304.qrscan.Retrofit.RetrofitClient
import kotlinx.android.synthetic.main.fragment_check_out.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class CheckOutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_check_out, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        qr_button.setOnClickListener {
            val intentIntegrator = IntentIntegrator(this@CheckOutFragment.requireActivity())
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            intentIntegrator.setOrientationLocked(true)
            resultLauncher.launch(intentIntegrator.createScanIntent())
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val sharedPreferences: SharedPreferences =
                    this@CheckOutFragment.requireActivity()
                        .applicationContext
                        .getSharedPreferences("LoggedInUserPrefs", Context.MODE_PRIVATE)
                val token = sharedPreferences.getString("token", "")
                checkOut("Bearer $token")

            }
        }

    private var retrofitClient: Retrofit = RetrofitClient.getInstance()
    private var myService: MyService = retrofitClient.create(MyService::class.java)

    private fun checkOut(token: String) {
        val call: Call<ResponseBody> = myService.checkOut(token)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful) {
                    val jsonObject = JSONObject(response.body()!!.string())
                    val responseMessage = jsonObject.getString("message")

                    Toast.makeText(activity, responseMessage, Toast.LENGTH_SHORT)
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