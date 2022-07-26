package com.example.qrscan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.fragment_check_out.*


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
            setDesiredBarcodeFormats()
            intentIntegrator.initiateScan()
        }
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(resultCode, data)
        if (result != null) {
            Toast.makeText(this@CheckOutFragment.requireActivity(), "saving", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun setDesiredBarcodeFormats() {
    }
}