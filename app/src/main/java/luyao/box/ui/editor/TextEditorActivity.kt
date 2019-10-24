package luyao.box.ui.editor

import kotlinx.android.synthetic.main.activity_text_editor.*
import kotlinx.coroutines.*
import luyao.box.R
import luyao.parser.xml.XmlParser
import luyao.util.ktx.base.BaseActivity
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

/**
 * Created by luyao
 * on 2019/1/3 16:18
 */
class TextEditorActivity : BaseActivity(),CoroutineScope by MainScope() {

    private val sourceDir by lazy { intent.getStringExtra("sourceDir") }


    override fun getLayoutResId() = R.layout.activity_text_editor

    override fun initView() {
    }

    override fun initData() {


        launch {
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

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}