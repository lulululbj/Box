package luyao.box.ext

import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Created by luyao
 * on 2019/7/18 15:17
 */
fun execCmd(command: String): String {
    val process = Runtime.getRuntime().exec(command)

    val inputStream = process.inputStream
    val inputStreamReader = InputStreamReader(inputStream)
    val reader = BufferedReader(inputStreamReader, 8 * 1024)

    val builder = StringBuilder()
    var line = reader.readLine()

    while (line != "") {
        builder.append(line)
        line = reader.readLine()
    }

    return builder.toString()
}