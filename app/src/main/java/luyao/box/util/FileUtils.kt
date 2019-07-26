package luyao.box.util

import luyao.box.bean.BoxFile
import luyao.box.bean.IFile
import java.io.File
import java.util.*


class FileUtils {

    companion object {

        fun getFileList(path: String): List<IFile>? {
            val file = File(path)
            val fileList = ArrayList<BoxFile>()
            if (file.exists() && file.listFiles()!=null) {
                for (subFile in file.listFiles())
                   if (!subFile.isHidden) fileList.add(BoxFile(subFile.path))
            }
            fileList.sort()
            return fileList
        }
    }
}