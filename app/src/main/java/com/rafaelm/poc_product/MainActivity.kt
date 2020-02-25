package com.rafaelm.poc_product

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import github.nisrulz.qreader.QRDataListener
import github.nisrulz.qreader.QREader
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    private val PREFS_NAME = "produto"

    private var qrEader: QREader ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Dexter.withActivity(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object :PermissionListener{
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    setupcamera()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                   Toast.makeText(this@MainActivity,"negado porfavor liberar permissao da camera", Toast.LENGTH_LONG).show()
                }

            }).check()
    }

    private fun setupcamera(){
        btn_enable_disable.setOnClickListener {
            if (qrEader!!.isCameraRunning) {
                btn_enable_disable.text = "start"
                val intent = Intent(this,GerarCode::class.java)
                startActivity(intent)
                qrEader!!.stop()
                val sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sharedPref.edit()

                editor.putString("ptd",code_info.text.toString())
                editor.apply()
                finish()
            }
            else{
                btn_enable_disable.text ="confirmar"
                qrEader!!.start()
            }
        }

        setupQrEader()
    }

    private fun setupQrEader(){
        qrEader = QREader.Builder(this,cameraview, QRDataListener { data->
            code_info.post{code_info.text = data}
        }).facing(QREader.BACK_CAM)
            .enableAutofocus(true)
            .height(cameraview.height)
            .width(cameraview.width)
            .build()

    }

    override fun onResume() {
        super.onResume()
        if (qrEader != null)
            qrEader!!.initAndStart(cameraview)
    }

    override fun onPause() {
        super.onPause()
        if (qrEader != null)
            qrEader!!.releaseAndCleanup()
    }
}
