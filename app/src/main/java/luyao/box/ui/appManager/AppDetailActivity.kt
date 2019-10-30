package luyao.box.ui.appManager

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import kotlinx.android.synthetic.main.activity_app_detail.*
import kotlinx.android.synthetic.main.title_layout.*
import kotlinx.coroutines.*
import luyao.box.APK_PATH
import luyao.box.R
import luyao.box.ui.editor.TextViewerActivity
import luyao.box.util.AppUtils
import luyao.parser.xml.XmlParser
import luyao.util.ktx.base.BaseActivity
import luyao.util.ktx.ext.*
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

/**
 * Created by luyao
 * on 2018/12/29 16:35
 */
class AppDetailActivity : BaseActivity(){

    private val packageInfo by lazy { intent.getParcelableExtra("packageInfo") as PackageInfo}
    private val appName by lazy { AppUtils.getAppName(this,packageInfo) }
    private val mPackageName by lazy { packageInfo.packageName }
    private val filePath by lazy { "$APK_PATH$appName${File.separator}AndroidManifest.xml" }
    private val sourceDir by lazy { intent.getStringExtra("apkPath") }

    override fun getLayoutResId() = R.layout.activity_app_detail

    override fun initView() {
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        mToolbar.setNavigationOnClickListener { onBackPressed() }
        mToolbar.title = appName
        detailIcon.setImageDrawable(AppUtils.getAppIcon(this, packageInfo))
        detailRefresh.isRefreshing = true
        initListener()
    }

    override fun initData() {
        refresh()
    }

    private fun initListener() {
        detailRefresh.setOnRefreshListener { refresh() }
        detailManifest.setOnClickListener {
            startKtxActivity<TextViewerActivity>(value = "filePath" to filePath)
        }
        li_sigMD5.setOnClickListener { copyToClipboard("md5", sigMD5.text.toString()) }
        li_sigSHA1.setOnClickListener { copyToClipboard("sha1", sigSHA1.text.toString()) }
        li_sigSHA256.setOnClickListener { copyToClipboard("sha256", sig256.text.toString()) }
    }

    private fun refresh() {

        val pkgInfo = packageManager.getPackageArchiveInfo(sourceDir,PackageManager.GET_SIGNATURES)

       pkgInfo.signatures[0].toByteArray()?.let {
            sigMD5.text = it.hash(Hash.MD5)
            sigSHA1.text = it.hash(Hash.SHA1)
            sig256.text = it.hash(Hash.SHA256)
        }

        launch {
            val xmlAsync = async(Dispatchers.IO) {
                var xmlParser: XmlParser
                val zipFile = ZipFile(File(sourceDir))
                val zipEntry: ZipEntry? = zipFile.getEntry("AndroidManifest.xml")
                zipEntry?.run {
                    val inputStream = zipFile.getInputStream(zipEntry)
                    xmlParser = XmlParser(inputStream)
                    return@async xmlParser.parse()
                }
            }
            val xml = xmlAsync.await()
            launch(Dispatchers.IO) {
                val destFile = File(filePath)
                if (!destFile.parentFile.exists()) destFile.parentFile.mkdirs()
                if (!destFile.exists()) destFile.createNewFile()
                xml?.xmlContent?.let { destFile.writeText(it) }
            }

            detailRefresh.isRefreshing = false
            xml?.run {
                detailVersionName.text = versionName ?: "unknown"
                detailVersionCode.text = versionCode
                detailPackageMame.text = packageName
                detailTargetSdk.text = targetSdkVersion
                detailMinSdk.text = minSdkVersion
            }
        }
    }
}