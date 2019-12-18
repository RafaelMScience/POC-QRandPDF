package com.rafaelm.poc_product

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_tela_inicial.*


class TelaInicial : AppCompatActivity() {

    private val PREFS_NAME = "produto"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_inicial)

        val text = findViewById<EditText>(R.id.qtdproduto)

        val sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        var total = findViewById<EditText>(R.id.caixapdt)

        qrcode.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            val editor: SharedPreferences.Editor = sharedPref.edit()
            val media: Int = total.text.toString().toInt() - text.text.toString().toInt()
            editor.putInt("qtd", media)
            editor.apply()
            startActivity(intent)
        }
    }
}
