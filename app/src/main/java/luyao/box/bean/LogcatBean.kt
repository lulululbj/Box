package luyao.box.bean

import android.util.Log

data class LogcatBean(val logString: String) {

    val level: Int
        get() {
            when {
                logString.contains("V/") -> return Log.VERBOSE
                logString.contains("D/") -> return Log.DEBUG
                logString.contains("I/") -> return Log.INFO
                logString.contains("W/") -> return Log.WARN
                logString.contains("E/") -> return Log.ERROR
                logString.contains("A/") -> return Log.ASSERT
            }
            return Log.VERBOSE
        }
}