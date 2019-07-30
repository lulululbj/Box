package luyao.box

import java.io.File

/**
 * Created by luyao
 * on 2019/7/18 9:25
 */

// long 格式化 GB MB KB

// 获取内部存储容量，可用，已用空间

// 获取 File 大小 文件/文件夹


// chmod Os.chmod()  是否可用？

// createSymlink ？

// isSymlink

// create

// delete deleteFile deleteFolder  delete(filter)

// writeToFile(InputStream)

// writeToFile(ByteArray)

// copyFile(File,File) copyFile(String,String)  /move

// copyFolder(File,File) copyFolder(String,String) /move
/**
 *   [destFile] dest file/folder
 *   [override] whether to override dest file/folder if exist
 *   [reserve] Whether to reserve source file/folder
 */
fun File.moveTo(destFile: File, override: Boolean = true, reserve: Boolean = true): Boolean {
    val dest = copyRecursively(destFile, override)
    if (!reserve) deleteRecursively()
    return dest
}

/**
 *   [destFolder] dest folder
 *   [overwrite] whether to override dest file/folder if exist
 *   [func] progress callback (from 0 to 100)
 */
fun File.moveToWithProgress(
    destFolder: File,
    overwrite: Boolean = true,
    reserve: Boolean = true,
    func: (file: File, i: Int) -> Unit
) {

    if (isDirectory) copyFolder(this, File(destFolder, name), overwrite, func)
    else copyFile(this, File(destFolder, name), overwrite, func)

    if (!reserve) deleteRecursively()
}


// close

fun File.rename(newName: String) =
    rename(File("$parent${File.separator}$newName"))

fun File.rename(newFile: File) =
    if (newFile.exists()) false else renameTo(newFile)


// isFile  isFolder

// create(是否保留源文件)

// listFile(filter) 是否遍历子文件夹

// getCharset

// 获取后缀名 获取文件名不含后缀

// mimeType

// parentPath

// 文件名冲突
