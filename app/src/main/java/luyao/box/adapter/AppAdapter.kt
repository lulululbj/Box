package luyao.box.adapter

import android.content.Context
import android.os.Environment
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import jadx.api.JadxArgs
import jadx.api.JadxDecompiler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import luyao.box.APK_PATH
import luyao.box.R
import luyao.box.bean.AppBean
import luyao.box.ui.appManager.AppDetailActivity
import luyao.box.util.AppManager
import luyao.box.view.CircleProgressView
import luyao.util.ktx.ext.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * Created by luyao
 * on 2018/12/29 10:44
 */
class AppAdapter(layoutResId: Int = R.layout.item_app, onlyReverse: Boolean = false) :
    BaseQuickAdapter<AppBean, BaseViewHolder>(layoutResId) {

    private var onReverseApp: (AppBean) -> Unit = {}

    fun setOnReverseApp(app: (AppBean) -> Unit) {
        this.onReverseApp = app
    }

    private val _onlyReverse = onlyReverse

    override fun convert(helper: BaseViewHolder, item: AppBean) {
        helper.run {
            setImageDrawable(R.id.appIcon, item.icon)
            setText(R.id.appName, item.appName)

            getView<ImageButton>(R.id.appPop).setOnClickListener {
                showPopMenu(helper, helper.itemView.context, it, item)
            }

            getView<RelativeLayout>(R.id.itemAppRoot).setOnClickListener {
                if (_onlyReverse)  showPopMenu(helper, helper.itemView.context, it, item)
                else {
                    helper.itemView.context.startKtxActivity<AppDetailActivity>(
                        values = arrayListOf(
                            "packageInfo" to item.packageInfo,
                            "apkPath" to item.sourceDir
                        )
                    )
                }
            }
        }
    }

    private fun showPopMenu(
        helper: BaseViewHolder,
        context: Context,
        view: View,
        appBean: AppBean
    ) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(
            if (_onlyReverse) R.menu.menu_reverse else R.menu.menu_app,
            popupMenu.menu
        )
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_open_app -> context.openApp(appBean.packageName)
                R.id.menu_uninstall_app -> context.uninstallApp(appBean.packageName) // TODO 卸载之后刷新列表
                R.id.menu_app_properties -> context.goToAppInfoPage(appBean.packageName)
                R.id.menu_app_detail ->
                    context.startKtxActivity<AppDetailActivity>(value = "packageName" to appBean.packageName)
                R.id.menu_save_apk -> saveApk(helper, context, appBean)
                R.id.menu_share_apk -> shareApk(appBean)
                R.id.menu_open_in_store -> context.openInAppStore(appBean.packageName)
                R.id.menu_app_reverse, R.id.menu_reverse -> onReverseApp(appBean)
                R.id.menu_reverse_folder -> {
                }
            }
            true
        }
        popupMenu.show()
    }

    private fun saveApk(helper: BaseViewHolder, context: Context, appBean: AppBean) {
        MaterialDialog(context)
            .title(R.string.backup)
            .message(
                text = String.format(
                    "%s 的安装包将保存在手机根目录 Box/apk/%s 文件夹下",
                    appBean.appName,
                    appBean.appName
                )
            )
            .positiveButton { saveApkFile(helper, appBean) }
            .negativeButton { }
            .show()
    }

    private fun saveApkFile(helper: BaseViewHolder, appBean: AppBean) {
        CoroutineScope(Dispatchers.Main).launch {
            val progressView = helper.getView<CircleProgressView>(R.id.appProgressView)
            val apkFile = File(appBean.sourceDir)
            val destFile = File("$APK_PATH${appBean.appName}${File.separator}${apkFile.name}")
            if (!destFile.parentFile.exists()) destFile.parentFile.mkdirs()
            if (!destFile.exists()) destFile.createNewFile()
            progressView.visibility = View.VISIBLE
            val channel = Channel<Float>()
            launch(Dispatchers.IO) {
                val input = FileInputStream(apkFile)
                val output = FileOutputStream(destFile)
                val b = ByteArray(1024)
                var hasRead = 0f
                var read: Int
                do {
                    read = input.read(b)
                    if (read > 0) {
                        output.write(b, 0, read)
                        hasRead += read
                        channel.send(hasRead / apkFile.length())
                    }
                } while (read > 0)
                output.flush()
                output.close()
                input.close()
                channel.send(-1f)
                channel.close()
            }
            for (x in channel) {
                progressView.percent = x * 100
                if (x == -1f) {
                    progressView.visibility = View.GONE
                    toast(mContext, R.string.backup_finish, Toast.LENGTH_SHORT)
                }
            }
        }
    }

    private fun shareApk(appBean: AppBean) {
        val apkFile = File(appBean.sourceDir)
        val destFile = File("$APK_PATH${appBean.appName}${File.separator}${apkFile.name}")
        if (!destFile.exists()) {
            mContext.toast("请先备份安装包")
        } else {
            AppManager.shareApk(mContext, apkFile)
        }
    }

}

