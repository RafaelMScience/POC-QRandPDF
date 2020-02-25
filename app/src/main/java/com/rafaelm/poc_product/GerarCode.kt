package com.rafaelm.poc_product

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_gerar_code.*
import kotlinx.android.synthetic.main.activity_tela_inicial.*
import java.io.*
import java.lang.Exception

class GerarCode : AppCompatActivity() {

    private val PREFS_NAME = "produto"

    val file_name : String ="test_pdf.pdf"

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

        Dexter.withActivity(this)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener{
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    gerarpdf.setOnClickListener{
                        createPdfFile(Common.getAppPath(this@GerarCode)+file_name)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(this@GerarCode,"negado porfavor liberar permissao", Toast.LENGTH_LONG).show()
                }

            })
    }

    private fun createPdfFile(path: String) {
        val sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val text = sharedPref.getInt("qtd", 0 )
        val ptd = sharedPref.getString("ptd","")

        //criar um arquivo com os itens que retirei e quais itens e lista de produtos
        if(File(path).exists())
            File(path).delete()
        try{
            val document = Document()
            //save
            PdfWriter.getInstance(document, FileOutputStream(path))
            document.open()

            document.pageSize = PageSize.A4
            document.addTitle("NavegAM")
            document.addCreator("Raf")

            val colorAccent = BaseColor(0,153,204,255)
            val headingFontSize = 20.0f
            val valueFonSize = 26.0f

            val fontName = BaseFont.createFont("assets/fonts/Roboto-Regular.ttf","UTF-8",BaseFont.EMBEDDED)

            var titleSytle = Font(fontName, 36.0f,Font.NORMAL, BaseColor.BLACK)
            addNewItem(document,"Detalhe Produto",Element.ALIGN_CENTER, titleSytle)

            val headingStyle = Font(fontName, headingFontSize, Font.NORMAL,colorAccent)
            addNewItem(document,"quantidades: "+text.toString(),Element.ALIGN_LEFT,headingStyle)

            val valueStyle = Font(fontName, valueFonSize,Font.NORMAL, BaseColor.BLACK)
            addNewItem(document, "Produto: "+ptd.toString(),Element.ALIGN_LEFT, valueStyle)

            addLineSeperator(document)
        }catch (e:Exception){

        }
    }

    private fun addLineSeperator(document: Document) {

    }

    @Throws(DocumentException::class)
    private fun addNewItem(document: Document, text: String, align: Int, style: Font) {
        val chunk = Chunk(text,style)
        val p = Paragraph(chunk)
        p.alignment = align
        document.add(p)
    }
}

