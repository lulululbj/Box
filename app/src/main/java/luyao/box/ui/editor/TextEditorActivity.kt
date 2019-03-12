package luyao.box.ui.editor

import kotlinx.android.synthetic.main.activity_text_editor.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import luyao.box.R
import luyao.box.common.base.BaseActivity
import luyao.parser.xml.XmlParser
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

/**
 * Created by luyao
 * on 2019/1/3 16:18
 */
class TextEditorActivity : BaseActivity() {

    private val sourceDir by lazy { intent.getStringExtra("sourceDir") }


    override fun getLayoutResId() = R.layout.activity_text_editor

    override fun initView() {
    }

    override fun initData() {
        CoroutineScope(Dispatchers.Main).launch {
            val xml = async(Dispatchers.IO) {
                val zipFile = ZipFile(File(sourceDir))
                val zipEntry: ZipEntry? = zipFile.getEntry("AndroidManifest.xml")
                zipEntry?.run {
                    val inputStream = zipFile.getInputStream(zipEntry)
                    val xmlParser = XmlParser(inputStream)
                    return@async xmlParser.parse()
                }
            }
            textEdit.setText(xml.await()?.xmlContent ?: "")
        }
    }
}