package luyao.box.ext

import android.util.Log
import luyao.box.BuildConfig

/**
 * Description: log相关，日志的开关和默认tag通过AndroidKtxConfig来配置
 * Create by lxj, at 2018/12/5
 */

const val BOX="box"

private enum class LogLevel{
    Verbose, Debug, Info, Warn, Error
}

fun String.v(tag: String = BOX){
    intervalLog(LogLevel.Verbose, tag, this )
}

fun String.d(tag: String = BOX){
    intervalLog(LogLevel.Debug, tag, this )
}
fun String.i(tag: String = BOX){
    intervalLog(LogLevel.Info, tag, this )
}
fun String.w(tag: String = BOX){
    intervalLog(LogLevel.Warn, tag, this )
}
fun String.e(tag: String = BOX){
    intervalLog(LogLevel.Error, tag, this )
}

private fun intervalLog(level: LogLevel, tag: String, msg: String){
    if(BuildConfig.DEBUG){
        when (level) {
            LogLevel.Verbose -> Log.v(tag, msg)
            LogLevel.Debug -> Log.d(tag, msg)
            LogLevel.Info -> Log.i(tag, msg)
            LogLevel.Warn -> Log.w(tag, msg)
            LogLevel.Error -> Log.e(tag, msg)
        }
    }
}
