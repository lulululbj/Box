package luyao.box.ui.activity

import kotlinx.android.synthetic.main.activity_current.*
import kotlinx.android.synthetic.main.title_layout.*
import luyao.box.R
import luyao.box.common.base.BaseActivity
import luyao.box.service.BoxAccessibilityService
import luyao.box.util.AccessibilityUtils
import luyao.box.util.FloatWindowManager
import luyao.box.util.Preference

class CurrentActivity : BaseActivity() {

    private var windowEnabled by Preference(Preference.SHOW_WINDOW, false)

    override fun getLayoutResId() = R.layout.activity_current

    override fun initView() {
        mToolbar.title = getString(R.string.current_activity)

        accessibilitySwitch.setOnCheckedChangeListener { _, _ -> AccessibilityUtils.goToAccessibilitySetting(this) }
        windowSwitch.setOnCheckedChangeListener { _, isChecked -> updateWindow(isChecked) }
    }

    override fun initData() {
    }

    override fun onResume() {
        super.onResume()
        windowSwitch.isChecked = windowEnabled
        accessibilitySwitch.isChecked = BoxAccessibilityService.instance != null
    }

    private fun initAccessibilityService() {

//        if (!accessibilitySwitch.isChecked)
//            AccessibilityUtils.goToAccessibilitySetting(this)
    }

    private fun updateWindow(isOpen: Boolean) {
        windowEnabled = isOpen
        if (isOpen && BoxAccessibilityService.instance!=null){
            FloatWindowManager.show()
        }else{
            FloatWindowManager.hide()
        }
    }
}