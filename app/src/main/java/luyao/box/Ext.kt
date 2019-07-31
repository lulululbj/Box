package luyao.box

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import luyao.util.ktx.ext.PaddingItemDecoration
import java.io.Closeable
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

// delete file && folder
fun File.deleteAll() {
    deleteFile(this)
}

fun deleteFile(file: File) {
    if (file.isFile || file.listFiles() == null || file.listFiles().isEmpty()) file.delete()
    else if (file.isDirectory) {
        for (subFile in file.listFiles())
            deleteFile(subFile)
    }
}

fun close(vararg args: Closeable) {
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
fun RecyclerView.itemPadding(padding: Int) {
    addItemDecoration(PaddingItemDecoration(padding, padding, padding, padding))
}

fun Long.toFormatTime(format: String) = formatTime(this, format)

fun formatTime(time: Long, format: String): String = SimpleDateFormat(format, Locale.getDefault()).format(time)


