package luyao.box.ui.appManager

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import jadx.api.JadxArgs
import jadx.api.JadxDecompiler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import luyao.box.REVERSE_PATH
import luyao.box.bean.AppBean
import luyao.box.util.AppManager
import luyao.util.ktx.base.BaseViewModel
import java.io.File

/**
 * Created by luyao
 * on 2019/10/29 14:19
 */
class AppViewModel : BaseViewModel() {

    private val _uiState = MutableLiveData<AppUiModel>()
    val uiState: LiveData<AppUiModel>
        get() = _uiState

    private val _reverseUiState = MutableLiveData<ReverseUiModel>()
    val reverseUiState: LiveData<ReverseUiModel>
        get() = _reverseUiState

    fun getAppList(context: Context, containSystem: Boolean) {

        viewModelScope.launch(Dispatchers.Default) {
            val appResult = AppManager.getInstalledAppBean(context, containSystem)

            withContext(Dispatchers.Main) {
                if (containSystem)
                    emitUiState(null, appResult, null)
                else
                    emitUiState(appResult, null, null)
            }
        }

    }

    fun getLocalApkList(context: Context) {

        viewModelScope.launch(Dispatchers.Default) {
            val appResult = AppManager.getLocalApkBean(context)
            withContext(Dispatchers.Main) { emitUiState(null, null, appResult) }
        }
    }

    fun reverseApp(appBean: AppBean) {
        viewModelScope.launch(Dispatchers.Main) {
            emitReverseState(isLoading = true)
            val saveDir = File(REVERSE_PATH, "${appBean.appName}_${appBean.versionName}")
            withContext(Dispatchers.IO) {
                val apkFile = File(appBean.sourceDir)
                val args = JadxArgs().apply {
                    inputFiles.add(apkFile)
                    outDir = saveDir
                }
                val decompiler = JadxDecompiler(args)
                decompiler.setReverseCallBack { fileName ->
                    emitReverseState(currentFile = fileName)
                }
                decompiler.load()
                decompiler.save()
            }

            emitReverseState(showSuccess = saveDir)
        }
    }

    private fun emitUiState(
        systemAppBeanList: List<AppBean>? = null,
        thirdAppBeanList: List<AppBean>? = null,
        localAppBeanList: List<AppBean>? = null
    ) {
        val appUiModel = AppUiModel(systemAppBeanList, thirdAppBeanList, localAppBeanList)
        _uiState.value = appUiModel
    }

    class AppUiModel(
        val systemAppBeanList: List<AppBean>? = null, // 系统 app
        val thirdAppBeanList: List<AppBean>? = null, // 第三方 app
        val localAppBeanList: List<AppBean>? = null // 本地安装包
    )

    private fun emitReverseState(
        isLoading: Boolean? = null,
        currentFile: String? = null,
        showSuccess: File? = null,
        showError: Boolean? = null
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            val reverseUiModel = ReverseUiModel(isLoading, currentFile, showSuccess, showError)
            _reverseUiState.value = reverseUiModel
        }

    }

    class ReverseUiModel(
        val isLoading: Boolean? = null,
        val currentFile: String? = null,
        val showSuccess: File? = null,
        val showError: Boolean? = null
    )


}