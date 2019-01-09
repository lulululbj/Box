package luyao.box.ui.activity

import kotlinx.android.synthetic.main.activity_current.*
import kotlinx.android.synthetic.main.title_layout.*
import luyao.box.R
import luyao.box.common.base.BaseActivity
import luyao.box.service.BoxAccessibilityService
import luyao.box.util.AccessibilityUtils

class CurrentActivity : BaseActivity() {

    override fun getLayoutResId() = R.layout.activity_current

    override fun initView() {
        mToolbar.title=getString(R.string.current_activity)
    }

    override fun initData() {
        initAccessibilityService()

    }

    private fun initAccessibilityService(){
        val isOpen=AccessibilityUtils.checkAccessibilityEnabled("$packageName/${BoxAccessibilityService::class.java.canonicalName.replace(packageName, "")}",this)
        accessibilitySwitch.isChecked=isOpen
        if (!isOpen)
            AccessibilityUtils.goToAccessibilitySetting(this)

    }
}