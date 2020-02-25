package com.rafaelm.poc_product

import android.content.Context
import android.os.Environment
import java.io.File

object Common{
    fun getAppPath(context: Context):String{
        val dir = File(Environment.getExternalStorageDirectory().toString()
        +File.separator
        +context.resources.getString(R.string.app_name)
        +File.separator)
        if(!dir.exists())
            dir.mkdir()
        return dir.path+File.separator
    }
}