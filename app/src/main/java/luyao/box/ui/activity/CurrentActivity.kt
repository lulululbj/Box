package luyao.box.ui.activity

import android.app.AlertDialog
import com.lzf.easyfloat.permission.PermissionUtils
import kotlinx.android.synthetic.main.activity_current.*
import kotlinx.android.synthetic.main.title_layout.*
import luyao.box.R
import luyao.box.service.BoxAccessibilityService
import luyao.box.util.FloatWindowManager
import luyao.box.util.Preference
import luyao.util.ktx.base.BaseActivity
import luyao.util.ktx.ext.goToAccessibilitySetting

class CurrentActivity : BaseActivity() {

    private var windowEnabled by Preference(Preference.SHOW_WINDOW, true)

    override fun getLayoutResId() = R.layout.activity_current

    override fun initView() {
        checkPermission()
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        mToolbar.setNavigationOnClickListener { onBackPressed() }
        mToolbar.title = getString(R.string.current_activity)
    }

    override fun initData() {

    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    private fun refresh() {
        windowSwitch.isChecked = windowEnabled
        accessibilitySwitch.isChecked = BoxAccessibilityService.instance != null
        accessibilitySwitch.setOnCheckedChangeListener { _, _ -> goToAccessibilitySetting() }
        windowSwitch.setOnCheckedChangeListener { _, isChecked -> updateWindow(isChecked) }
    }

    /**
     * 检测浮窗权限是否开启，若没有给与申请提示框（非必须，申请依旧是EasyFloat内部内保进行）
     */
    private fun checkPermission() {

        if (PermissionUtils.checkPermission(this)) {
            if (windowEnabled) FloatWindowManager.init(this)
        } else {
            AlertDialog.Builder(this)
                .setMessage("使用悬浮窗功能，需要您授权悬浮窗权限。")
                .setPositiveButton("去开启") { _, _ ->
                    FloatWindowManager.init(this)
                }
                .setNegativeButton("取消") { _, _ -> }
                .show()
        }
    }

    private fun updateWindow(isOpen: Boolean) {
        windowEnabled = isOpen
        if (isOpen) {
            if (!FloatWindowManager.hasInit()) FloatWindowManager.init(this)
            FloatWindowManager.show()
        } else {
            FloatWindowManager.hide()
        }
    }
}