package luyao.box

import android.content.Context
import android.view.View
import android.widget.Toast
import luyao.box.common.util.AppUtils
import luyao.box.common.util.FileUtils.deleteFile
import luyao.box.common.util.HashUtils
import java.io.File

fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.toast(resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

fun Context.px2dp(pxValue: Int): Int {
    val scale = resources.displayMetrics.density;
    return (pxValue / scale + 0.5f).toInt()
}

fun Context.dp2px(dpValue: Int): Int {
    val scale = resources.displayMetrics.density;
    return (dpValue * scale + 0.5f).toInt()
}

fun Context.getWidth() = resources.displayMetrics.widthPixels

fun Context.getHeight() = resources.displayMetrics.heightPixels

// delete file && folder
fun File.deleteAll() {
    deleteFile(this)
}

fun ByteArray.md5() = AppUtils.byte2HexStr(HashUtils.hash(this, HashUtils.Hash.MD5))
fun ByteArray.sha1() = AppUtils.byte2HexStr(HashUtils.hash(this, HashUtils.Hash.SHA1))
fun ByteArray.sha256() = AppUtils.byte2HexStr(HashUtils.hash(this, HashUtils.Hash.SHA256))

fun View.click(){

}



