package luyao.box.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import luyao.box.bean.HistoryBean
import luyao.box.bean.LogcatBean
import luyao.box.ui.logcat.LogcatUtil
import luyao.box.util.FloatWindowManager

class LogcatService : Service() {

    lateinit var mListen : (LogcatBean) -> Unit


    inner class LogBinder : Binder(){
        fun getService():LogcatService{
            return this@LogcatService
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return LogBinder()
    }

    override fun onCreate() {
        super.onCreate()
        start()
    }

    private fun start() {
        FloatWindowManager.show()
        FloatWindowManager.clearView()
        CoroutineScope(Dispatchers.Main).launch {
            LogcatUtil.start()
            for (logcat in LogcatUtil.channel) {
                mListen.invoke(logcat)
                FloatWindowManager.addItem(HistoryBean("",logcat.logString))
            }
        }
    }

    fun logListener(listener : (LogcatBean) -> Unit){
        this.mListen=listener
    }


}