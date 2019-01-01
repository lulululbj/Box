package luyao.box.ui.appManager

import kotlinx.android.synthetic.main.title_layout.*
import luyao.box.R
import luyao.box.common.base.BaseActivity

/**
 * Created by luyao
 * on 2018/12/29 16:35
 */
class AppDetailActivity : BaseActivity() {
    override fun getLayoutResId() = R.layout.activity_app_detail

    override fun initView() {
        mToolbar.title="应用详情"
    }

    override fun initData() {
    }
}