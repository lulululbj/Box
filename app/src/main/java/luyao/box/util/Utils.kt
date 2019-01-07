package luyao.box.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import luyao.box.R
import luyao.box.toast

object  Utils {

    fun copy(context: Context,text:String){
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData=ClipData.newPlainText("text",text)
        cm.primaryClip=clipData
        context.toast(R.string.has_copied)
    }
}