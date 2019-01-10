package luyao.box.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import kotlinx.android.synthetic.main.activity_current.*
import kotlinx.android.synthetic.main.title_layout.*
import luyao.box.R
import luyao.box.common.base.BaseActivity
import luyao.box.util.AccessibilityUtils
import luyao.box.util.Preference

class CurrentActivity : BaseActivity() {

    private var showWindow by Preference(Preference.SHOW_WINDOW, false)
    private val isOpen by lazy {
        AccessibilityUtils.checkBoxAccessibilityEnabled(this)
    }

    override fun getLayoutResId() = R.layout.activity_current

    override fun initView() {
        mToolbar.title = getString(R.string.current_activity)
        windowSwitch.isChecked=showWindow
        windowSwitch.setOnCheckedChangeListener { _, isChecked -> updateWindow(isChecked) }
    }

    override fun initData() {
        initAccessibilityService()
    }

    private fun initAccessibilityService() {
        accessibilitySwitch.isChecked = isOpen
        if (!isOpen)
            AccessibilityUtils.goToAccessibilitySetting(this)
    }

    private fun updateWindow(isOpen: Boolean) {
        if (AccessibilityUtils.checkBoxAccessibilityEnabled(this)) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1 && !Settings.canDrawOverlays(this)) {
                windowSwitch.isChecked = false
                showWindow = false
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            } else {
                showWindow = isOpen
                windowSwitch.isChecked = isOpen
            }
        } else {
            showWindow = false
            windowSwitch.isChecked = false
        }
    }
}