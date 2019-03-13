package luyao.box.ui.setting

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import luyao.box.BASE_PATH
import luyao.box.R
import luyao.box.common.util.FileUtils
import java.io.File

/**
 * Created by luyao
 * on 2019/1/17 14:11
 */
class SettingFragment : PreferenceFragmentCompat() {

    private val DELETE_APK = 1
    private val CLEAR_CACHE = 2

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference_setting)

        initListener()
    }

    private fun initListener() {
        findPreference("setting_delete_apk").setOnPreferenceClickListener {
            showDeleteDialog(DELETE_APK)
            true
        }

        findPreference("setting_clear_cache").setOnPreferenceClickListener {
            showDeleteDialog(CLEAR_CACHE)
            true
        }
    }

    private fun showDeleteDialog(flag: Int) {
        activity?.let {
            MaterialDialog(it)
                .title(R.string.note)
                .message(if (flag == DELETE_APK) R.string.delete_apk_note else R.string.clear_cache_note)
                .positiveButton { delete(flag) }
                .negativeButton { }
                .show()
        }
    }


    private fun delete(flag: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            if (flag == DELETE_APK) {
                filter(File(BASE_PATH))
            } else if (flag == CLEAR_CACHE) {
                fileList.add(File(BASE_PATH))
            }
            launch(Dispatchers.IO) {
                for (file in fileList)
                    FileUtils.deleteFile(file)
                fileList.clear()
            }
        }
    }

    private val fileList = arrayListOf<File>()

    private fun filter(file: File) {

        if (file.isFile) {
            if (file.name.endsWith(".apk")) fileList.add(file)
        } else if (file.listFiles() != null && file.listFiles().isNotEmpty()) {
            for (subFile in file.listFiles()) {
               filter(subFile)
            }
        }

    }

}