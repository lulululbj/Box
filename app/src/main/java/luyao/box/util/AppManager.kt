package luyao.box.util

import android.content.Context
import luyao.box.bean.AppBean
import luyao.box.common.util.AppUtils.getAppIcon
import luyao.box.common.util.AppUtils.getAppName
import luyao.box.common.util.AppUtils.getInstalledApp

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

//    fun getApkFile(context: Context,appBean: AppBean):File{
//
//    }


}