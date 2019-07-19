package luyao.box.bean

interface IFile {

    var checked: Boolean

    val filePath: String

    fun getName(): String
    fun getParent(): String
    fun getParentFile(): IFile
    fun getPath(): String
    fun canExecute(): Boolean
    fun canRead(): Boolean
    fun canWrite(): Boolean
    fun exists(): Boolean
    fun isDirectory(): Boolean
    fun isFile(): Boolean
    fun isHidden(): Boolean
    fun listFile(): List<IFile>
    fun length(): Long
    fun lastModified(): Long
    fun getTotalSpace(): Long
    fun getFreeSpace(): Long
    fun getUsableSpace(): Long
    fun getMimeType(): String?

    fun setSelectMode(boolean: Boolean) = { checked = boolean }
    fun isSelectMode() = checked
}