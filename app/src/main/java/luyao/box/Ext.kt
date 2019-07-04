package luyao.box

import android.content.*
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import luyao.util.ktx.ext.toast
import java.io.Closeable
import java.io.File

// delete file && folder
fun File.deleteAll() {
    deleteFile(this)
}

fun deleteFile(file: File) {
    if (file.isFile || file.listFiles()==null || file.listFiles().isEmpty()) file.delete()
    else if (file.isDirectory) {
        for (subFile in file.listFiles())
            deleteFile(subFile)
    }
}


fun Context.startActivity(z: Class<*>, name: String, extra: Any) {
    Intent(this, z).run {
        when (extra) {
            is Int -> putExtra(name, extra)
            is Byte -> putExtra(name, extra)
            is Short -> putExtra(name, extra)
            is Long -> putExtra(name, extra)
            is Float -> putExtra(name, extra)
            is Double -> putExtra(name, extra)
            is Char -> putExtra(name, extra)
            is Boolean -> putExtra(name, extra)
            is String -> putExtra(name,extra)
        }
        startActivity(this)
    }
}

fun close(vararg args:Closeable){
    for (c in args) c.close()
}

fun Context.getAppSignature(context: Context, packageName: String=context.packageName): ByteArray {
    val packageInfo: PackageInfo =
        context.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
    val signatures = packageInfo.signatures
    return signatures[0].toByteArray()
}

fun Context.openInAppStore(packageName:String = this.packageName) {
    val intent = Intent(Intent.ACTION_VIEW)
    try {
        intent.data = Uri.parse("market://details?id=$packageName")
        startActivity(intent)
    } catch (ifPlayStoreNotInstalled: ActivityNotFoundException) {
        intent.data =
            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        startActivity(intent)
    }
}

fun Context.getAppInfoIntent(packageName: String = this.packageName): Intent =
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }

/** 跳转到应用信息页面 */
fun Context.goToAppInfoPage(packageName: String=this.packageName) {
    startActivity(getAppInfoIntent(packageName))
}

fun Context.openApp(packageName: String) =
    packageManager.getLaunchIntentForPackage(packageName)?.run { startActivity(this) }

fun Context.uninstallApp(packageName: String) {
    Intent(Intent.ACTION_DELETE).run {
        data = Uri.parse("package:$packageName")
        startActivity(this)
    }
}

fun Context.copyToClipboard(text:String){
    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData= ClipData.newPlainText("text",text)
    cm.primaryClip=clipData
    toast(text)
}

fun Context.goToAccessibilitySetting() = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).run { startActivity(this) }



