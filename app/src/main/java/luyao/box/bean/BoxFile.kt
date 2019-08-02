package luyao.box.bean

import luyao.box.util.MimeType
import java.io.File

class BoxFile(override val filePath: String) : IFile, Comparable<BoxFile> {

    override fun getFile() = File(filePath)

    override var checked: Boolean = false

    override fun getName(): String = File(filePath).name

    override fun getParent(): String = File(filePath).parent

    override fun getParentFile(): IFile = BoxFile(File(filePath).path)

    override fun getPath(): String = File(filePath).path

    override fun canExecute(): Boolean = File(filePath).canExecute()

    override fun canRead(): Boolean = File(filePath).canRead()

    override fun canWrite(): Boolean = File(filePath).canWrite()

    override fun exists(): Boolean = File(filePath).exists()

    override fun isDirectory(): Boolean = File(filePath).isDirectory

    override fun isFile(): Boolean = File(filePath).isFile

    override fun isHidden(): Boolean = File(filePath).isHidden

    override fun listFile(): List<IFile> {
        val fileList = ArrayList<IFile>()
        for (file in File(filePath).listFiles()) {
            fileList.add(BoxFile(file.path))
        }
        return fileList
    }

    override fun length(): Long = File(filePath).length()

    override fun lastModified(): Long = File(filePath).lastModified()

    override fun getTotalSpace(): Long = File(filePath).totalSpace

    override fun getFreeSpace(): Long = File(filePath).freeSpace

    override fun getUsableSpace(): Long = File(filePath).usableSpace

    override fun getMimeType(): String? = MimeType.getMimeType(getFile().extension, isDirectory())

    override fun delete() = File(filePath).deleteRecursively()

    override fun compareTo(other: BoxFile): Int = if (this.isDirectory() && !other.isDirectory()) -1
    else if (!this.isDirectory() && other.isDirectory()) 1
    else this.getPath().compareTo(other.getPath())

}