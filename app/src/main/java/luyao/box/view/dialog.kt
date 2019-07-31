package luyao.box.view

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.input.input
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import luyao.box.R
import luyao.box.formatSize
import luyao.box.toFormatTime
import luyao.util.ktx.ext.Hash
import luyao.util.ktx.ext.hash
import luyao.util.ktx.ext.visible
import java.io.File

/**
 * Created by luyao
 * on 2019/7/31 9:03
 */
fun showPropertiesDialog(context: Context, file: File) {
    val dialog = MaterialDialog(context)
        .title(R.string.properties)
        .positiveButton(R.string.confirm)
        .customView(R.layout.dialog_properties, scrollable = true, horizontalPadding = true)
    val customView = dialog.getCustomView()

    CoroutineScope(Dispatchers.Main).launch {
        file.run {
            customView.findViewById<TextView>(R.id.title_name).text = name
            customView.findViewById<TextView>(R.id.title_location).text = path
            customView.findViewById<TextView>(R.id.title_date).text = lastModified().toFormatTime("MMM dd yyyy | HH:mm")

            customView.findViewById<TextView>(R.id.title_size).text = withContext(Dispatchers.IO) { formatSize }

            if (isFile) {
                customView.findViewById<LinearLayout>(R.id.properties_dialog_md5).visible()
                customView.findViewById<LinearLayout>(R.id.properties_dialog_sha256).visible()
                customView.findViewById<TextView>(R.id.title_md5).text =
                    withContext(Dispatchers.IO) { this@run.hash(Hash.MD5) }
                customView.findViewById<TextView>(R.id.title_sha256).text =
                    withContext(Dispatchers.IO) { this@run.hash(Hash.SHA256) }
            }
        }
    }

    dialog.show()
}

fun showCreateFileOrFolderDialog(context: Context, isFolder: Boolean, func: (name: String) -> Unit) {
    MaterialDialog(context).show {
        title(text = if (isFolder) "新文件夹" else "新文件")
        input(hint = "请输入新名称", allowEmpty = false) { _, text ->
            func(text.toString())
        }
        positiveButton(R.string.confirm)
        negativeButton(R.string.cancel)
    }
}
