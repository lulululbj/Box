package luyao.box.common.util

import java.io.File
import java.io.FileFilter

/**
 * Created by luyao
 * on 2019/1/17 16:24
 */
object FileUtils {

    class FFilter : FileFilter {
        override fun accept(pathname: File?): Boolean {
            pathname?.let {
                return it.endsWith(".apk")
            }
            return false
        }

    }


}