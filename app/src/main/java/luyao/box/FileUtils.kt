package luyao.box

import kotlinx.coroutines.DEBUG_PROPERTY_NAME
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer

/**
 * Created by luyao
 * on 2019/7/23 9:29
 */

fun copyFile(sourceFile: File, destFile: File, func: (i: Int) -> Unit) {

    val inputStream = FileInputStream(sourceFile)
    val outputStream = FileOutputStream(destFile)
    val iChannel = inputStream.channel
    val oChannel = outputStream.channel

    val totalSize = sourceFile.length()
    val buffer = ByteBuffer.allocate(1024)
    var hasRead = 0f
    var progress = -1
    while (true) {
        buffer.clear()
        val read = iChannel.read(buffer)
        if (read == -1)
            break
        buffer.limit(buffer.position())
        buffer.position(0)
        oChannel.write(buffer)
        hasRead += read
        val newProgress = ((hasRead / totalSize)*100).toInt()
        if (progress!=newProgress){
            progress = newProgress
            func(progress)
        }
    }

    inputStream.close()
    outputStream.close()
}

fun main() {
    val sourceFile = File("D://src.zip")
    val destFile = File("D://src2.zip")
    copyFile(sourceFile,destFile) {
        println(it.toString())
    }
}