package luyao.box

import android.content.*
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.recyclerview.widget.RecyclerView
import luyao.util.ktx.ext.PaddingItemDecoration
import luyao.util.ktx.ext.toast
import java.io.Closeable
import java.io.File

// delete file && folder
fun File.deleteAll() {
    deleteFile(this)
}

fun deleteFile(file: File) {
    if (file.isFile || file.listFiles()==null || file.listFiles().isEmpty()) file.delete()
    else if (file.isDirectory) {
        for (subFile in file.listFiles())
            deleteFile(subFile)
    }
}

fun close(vararg args:Closeable){
    for (c in args) c.close()
}

fun Context.sendEmail(email: String, subject: String?, text: String?) {
    Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email")).run {
        subject?.let { putExtra(Intent.EXTRA_SUBJECT, subject) }
        text?.let { putExtra(Intent.EXTRA_TEXT, text) }
        startActivity(this)
    }
}

/**  [padding] is dp */
fun RecyclerView.itemPadding(padding:Int) {
    addItemDecoration(PaddingItemDecoration(padding, padding, padding, padding))
}

