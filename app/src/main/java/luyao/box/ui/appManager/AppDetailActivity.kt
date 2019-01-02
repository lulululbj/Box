package luyao.box.ui.appManager

import kotlinx.android.synthetic.main.activity_app_detail.*
import kotlinx.android.synthetic.main.title_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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
 * on 2018/12/29 16:35
 */
class AppDetailActivity : BaseActivity() {

    private val mPackageName by lazy { intent.getStringExtra("packageName") }
    private val sourceDir by lazy { applicationContext.packageManager.getApplicationInfo(mPackageName, 0).sourceDir }

    override fun getLayoutResId() = R.layout.activity_app_detail

    override fun initView() {
        mToolbar.title = "应用详情"
    }

    override fun initData() {
        GlobalScope.launch(Dispatchers.Main) {

            val xml=GlobalScope.async(Dispatchers.IO) {
                val zipFile = ZipFile(File(sourceDir))
                val zipEntry: ZipEntry? = zipFile.getEntry("AndroidManifest.xml")
                zipEntry?.run {
                    val inputStream = zipFile.getInputStream(zipEntry)
                    val xmlParser = XmlParser(inputStream)
                    return@async xmlParser.parse()
                }
            }

            xmlContent.text=xml.await()
        }

    }
}