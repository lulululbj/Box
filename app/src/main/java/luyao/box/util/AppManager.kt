package luyao.box.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import luyao.box.bean.AppBean
import luyao.box.util.AppUtils.getAppIcon
import luyao.box.util.AppUtils.getAppName
import luyao.box.util.AppUtils.getInstalledApp
import java.io.File


/**
 * Created by luyao
 * on 2018/12/29 13:42
 */
object AppManager {

    fun getInstalledAppBean(context: Context, isSystem: Boolean = false): List<AppBean> {
        val appBeanList = mutableListOf<AppBean>()
        getInstalledApp(context, isSystem).forEach {

            val appBean = AppBean(
                getAppName(context, it),
                it.packageName,
                it.versionName,
                it.applicationInfo.sourceDir,
                getAppIcon(context, it),
                it
            )
            appBeanList.add(appBean)
        }
        return appBeanList
    }


    fun getLocalApkBean(context: Context):List<AppBean>{
        val appBeanList = mutableListOf<AppBean>()
        Environment.getExternalStorageDirectory().walk().filter { it.isFile && it.name.endsWith(".apk") }
            .sortedBy { it.name[0].toLowerCase() }
            .map { it to context.packageManager.getPackageArchiveInfo(it.path,0) }
            .forEach {
                val pkgInfo = it.second
                val appBean = AppBean(
                    it.first.name,
                    pkgInfo.packageName,
                    pkgInfo.versionName,
                    it.first.path,
                    getAppIcon(context, pkgInfo),
                    it.second
                )
                appBeanList.add(appBean)
            }
        return appBeanList
    }

    fun shareApk(mContext: Context, apkFile: File) {
        val intent = Intent()
        intent.run {
            action = Intent.ACTION_SEND
            type = "application/vnd.android.package-archive"
            putExtra(Intent.EXTRA_STREAM, Uri.parse(apkFile.path))
        }
        mContext.startActivity(Intent.createChooser(intent, "Share to"))
    }

//    fun getApkFile(context: Context,appBean: AppBean):File{
//
//    }


}