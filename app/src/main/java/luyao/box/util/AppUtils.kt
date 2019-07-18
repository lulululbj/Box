package luyao.box.util

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

/**
 * Created by luyao
 * on 2018/12/28 15:52
 */
object AppUtils {

    /**
     * 获取已安装非系统应用
     */
    fun getInstalledApp(context: Context): List<PackageInfo> {
        val packageManager = context.packageManager
        return packageManager.getInstalledPackages(0)
            .filter { (ApplicationInfo.FLAG_SYSTEM and it.applicationInfo.flags) == 0 }
    }


    fun getAppIcon(context: Context, packageInfo: PackageInfo): Drawable {
        return packageInfo.applicationInfo.loadIcon(context.packageManager)
    }

    fun getAppName(context: Context, packageName: String): String {
        return context.packageManager.getPackageInfo(packageName, 0).applicationInfo.loadLabel(context.packageManager)
            .toString()
    }

    fun getAppName(context: Context, packageInfo: PackageInfo): String {
        return packageInfo.applicationInfo.loadLabel(context.packageManager).toString()
    }

    fun getAppVersionName(context: Context, packageName: String): String {
        return context.packageManager.getPackageInfo(packageName, 0).versionName
    }

    fun getAppVersionCode(context: Context, packageName: String): Int {
        return context.packageManager.getPackageInfo(packageName, 0).versionCode
    }

    fun getAppTargetSdkVersion(context: Context, packageName: String): Int {
        return context.packageManager.getPackageInfo(packageName, 0).applicationInfo.targetSdkVersion
    }

    fun getAppMinSdkVersion(context: Context, packageName: String): Int {
        return context.packageManager.getPackageInfo(packageName, 0).applicationInfo.minSdkVersion
    }

    fun openFile(context: Context, file: File, mineType: String) {
        if (!file.exists()) return


        val intent = Intent().run {
            action = Intent.ACTION_VIEW
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }

        if (Build.VERSION.SDK_INT >= 24) {
            intent.setDataAndType(
                FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file),
                mineType
            )
        } else {
            intent.setDataAndType(Uri.fromFile(file), mineType)
        }


        context.startActivity(Intent.createChooser(intent, "Open with"))
    }
}