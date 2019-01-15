package luyao.box.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import luyao.box.bean.AppBean
import luyao.box.common.util.AppUtils.getAppIcon
import luyao.box.common.util.AppUtils.getAppName
import luyao.box.common.util.AppUtils.getInstalledApp
import java.io.File

/**
 * Created by luyao
 * on 2018/12/29 13:42
 */
object AppManager {

    fun getInstalledAppBean(context: Context): List<AppBean> {
        val installedAppBeanList = mutableListOf<AppBean>()
        getInstalledApp(context).forEach {
            val appBean = AppBean(
                getAppName(context, it),
                it.packageName,
                it.versionName,
                it.applicationInfo.sourceDir,
                getAppIcon(context, it)
            )
            installedAppBeanList.add(appBean)
        }
        return installedAppBeanList
    }

    fun shareApk(mContext: Context, apkFile: File) {
        val intent = Intent()
        intent.run {
            action = Intent.ACTION_SEND
            type = "application/vnd.android.package-archive"
            putExtra(Intent.EXTRA_STREAM, Uri.parse(apkFile.path))
        }
        mContext.startActivity(Intent.createChooser(intent,"Share to"))
    }

//    fun getApkFile(context: Context,appBean: AppBean):File{
//
//    }


}