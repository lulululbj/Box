package luyao.box.common.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
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
        return context.packageManager.getPackageInfo(packageName,0).applicationInfo.loadLabel(context.packageManager).toString()
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

    fun uninstallApp(context: Context, packageName: String) {
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:$packageName")
        context.startActivity(intent)
    }

    fun openApp(context: Context, packageName: String) {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        intent?.run { context.startActivity(this) }
    }

    fun openAppProperties(context: Context, packageName: String) {
        val intent = Intent(
            android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:$packageName")
        )
        context.startActivity(intent)
    }

    fun openInStore(context: Context, packageName: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        try {
            intent.data = Uri.parse("market://details?id=$packageName")
            context.startActivity(intent)
        } catch (ifPlayStoreNotInstalled: ActivityNotFoundException) {
            intent.data =
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            context.startActivity(intent)
        }

    }

    fun getAppSignature(context: Context, packageName: String): ByteArray {
        val packageInfo: PackageInfo =
            context.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        val signatures = packageInfo.signatures
        return signatures[0].toByteArray()
    }

    fun getAppSignatureMD5(context: Context, packageName: String): String =
        byte2HexStr(HashUtils.hash(getAppSignature(context, packageName), HashUtils.Hash.MD5))

    fun getAppSignatureSHA1(context: Context, packageName: String): String =
        byte2HexStr(HashUtils.hash(getAppSignature(context, packageName), HashUtils.Hash.SHA1))

    fun getAppSignatureSHA256(context: Context, packageName: String): String =
        byte2HexStr(HashUtils.hash(getAppSignature(context, packageName), HashUtils.Hash.SHA256))

    fun byte2HexStr(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            var hex = Integer.toHexString(b.toInt() and 0xFF)
            if (hex.length == 1) {
                hex = "0$hex"
            }
            sb.append(hex.toUpperCase()).append(":")
        }
        return sb.toString().dropLast(1)
    }

    fun openFile(context: Context, file: File,mineType:String) {
        if (!file.exists()) return


        val intent=Intent().run {
            action=Intent.ACTION_VIEW
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or  Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }

        if (Build.VERSION.SDK_INT >= 24) {
            intent.setDataAndType(FileProvider.getUriForFile(context, "luyao.box.fileprovider", file),mineType)
        } else {
            intent.setDataAndType(Uri.fromFile(file),mineType)
        }


        context.startActivity(Intent.createChooser(intent,"Open with"))
    }

    fun openBrowser(context: Context,url:String){
        val intent=Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
}