package luyao.box

import android.content.Context
import android.widget.Toast

fun Context.toast(text:String){
    Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
}

fun Context.toast(resId:Int){
    Toast.makeText(this,resId,Toast.LENGTH_SHORT).show()
}