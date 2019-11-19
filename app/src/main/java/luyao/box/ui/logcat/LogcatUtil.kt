package luyao.box.ui.logcat

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import luyao.box.bean.LogcatBean
import java.io.BufferedReader
import java.io.InputStreamReader

object LogcatUtil {

    val channel = Channel<LogcatBean>()
    val logList = ArrayList<LogcatBean>()
    var keyWords = ""
    var level = Log.VERBOSE


    fun start() {
        CoroutineScope(Dispatchers.IO).launch {
            Runtime.getRuntime().exec("logcat -c") // 清除日志
            val process = Runtime.getRuntime().exec("logcat -v time")
            val inputStream = process.inputStream
            val inputStreamReader = InputStreamReader(inputStream)
            val reader = BufferedReader(inputStreamReader)

            var logcat: String? = ""
            do {
                logcat = reader.readLine()
                val log = LogcatBean(logcat)
                if (check(log)) {
                    logList.add(log)
                    channel.send(log)
                }
            } while (logcat != null)
        }
    }

     fun check(log: LogcatBean): Boolean {
        var flag = true
        if (keyWords != "" && !log.logString.contains(keyWords, false)) flag = false
        if (level!=Log.VERBOSE && log.level != level) flag = false
        return flag
    }
}