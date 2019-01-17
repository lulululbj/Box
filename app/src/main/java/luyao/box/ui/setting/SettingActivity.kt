package luyao.box.ui.setting

import kotlinx.android.synthetic.main.title_layout.*
import luyao.box.R
import luyao.box.common.base.BaseActivity

/**
 * Created by luyao
 * on 2019/1/17 14:03
 */
class SettingActivity : BaseActivity() {

    override fun getLayoutResId() = R.layout.activity_setting

    override fun initView() {
        mToolbar.title = getString(R.string.action_settings)

        this.supportFragmentManager.beginTransaction()
            .replace(R.id.setting_content, SettingFragment())
            .commit()
    }

    override fun initData() {
    }


}