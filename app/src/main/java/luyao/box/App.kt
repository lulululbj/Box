package luyao.box

import android.app.Application
import android.content.Context
import com.tencent.bugly.crashreport.CrashReport
import luyao.util.ktx.Ktx
import luyao.util.ktx.ext.showLog
import kotlin.properties.Delegates

/**
 * Created by luyao
 * on 2018/12/29 13:33
 */
class App : Application() {

    companion object {
        var CONTEXT: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
        showLog = BuildConfig.DEBUG
        Ktx.watchAppLife = false
        CrashReport.initCrashReport(applicationContext, "ecba9e150d", BuildConfig.DEBUG)
    }
}