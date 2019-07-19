package luyao.box.ui.file

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import luyao.box.bean.BoxFile
import luyao.box.util.FileUtils
import luyao.util.ktx.base.BaseViewModel

/**
 * Created by luyao
 * on 2019/7/19 15:08
 */
class FileViewModel : BaseViewModel() {

    val fileListData: MutableLiveData<List<BoxFile>> = MutableLiveData()

    fun getFileListAsync(rootPath: String) {
        launch {
            val result = async(Dispatchers.IO) { FileUtils.getFileList(rootPath) }
            fileListData.value = result.await()
        }
    }


}