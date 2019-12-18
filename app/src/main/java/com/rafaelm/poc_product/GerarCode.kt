package com.rafaelm.poc_product

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_gerar_code.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader

class GerarCode : AppCompatActivity() {

    private val PREFS_NAME = "produto"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerar_code)

        val sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val text = sharedPref.getInt("qtd", 0 )
        val ptd = sharedPref.getString("ptd","")
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.encodeBitmap(text.toString() + ptd.toString(), BarcodeFormat.QR_CODE, 512, 512)

        txtprodutos.setText("Nesta caixa tem : "+text.toString()+" produtos\nProdutos: " + ptd.toString())
        qrgerador.setImageBitmap(bitmap)

    }
}

