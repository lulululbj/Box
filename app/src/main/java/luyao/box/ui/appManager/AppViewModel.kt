package luyao.box.ui.appManager

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import luyao.box.bean.AppBean
import luyao.box.util.AppManager
import luyao.util.ktx.base.BaseViewModel

/**
 * Created by luyao
 * on 2019/10/29 14:19
 */
class AppViewModel : BaseViewModel() {

    private val _uiState = MutableLiveData<AppUIModel>()
    val uiState: LiveData<AppUIModel>
        get() = _uiState

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

    private fun emitUiState(
        systemAppBeanList: List<AppBean>? = null,
        thirdAppBeanList: List<AppBean>? = null,
        localAppBeanList: List<AppBean>? = null
    ) {
        val appUiModel = AppUIModel(systemAppBeanList, thirdAppBeanList, localAppBeanList)
        _uiState.value = appUiModel
    }

    class AppUIModel(
        val systemAppBeanList: List<AppBean>? = null, // 系统 app
        val thirdAppBeanList: List<AppBean>? = null, // 第三方 app
        val localAppBeanList: List<AppBean>? = null // 本地安装包
    )


}