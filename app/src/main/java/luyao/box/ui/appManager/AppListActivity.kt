package luyao.box.ui.appManager

import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_app_list.*
import kotlinx.android.synthetic.main.title_layout.*
import luyao.box.R
import luyao.box.adapter.AppAdapter
import luyao.box.common.base.BaseActivity
import luyao.box.util.AppManager

class AppListActivity : BaseActivity() {

    private val appAdapter by lazy { AppAdapter() }

    override fun getLayoutResId() = R.layout.activity_app_list

    override fun initView() {
        mToolbar.title = getString(R.string.app)
        initRefreshLayout()
        initRecycleView()
    }

    override fun initData() {
        refresh()
    }

    private fun initRefreshLayout() {
        appRefreshLayout.isRefreshing = true
        appRefreshLayout.setOnRefreshListener { refresh() }
    }

    private fun initRecycleView() {
        val itemDecoration = DividerItemDecoration(
            this,
            DividerItemDecoration.VERTICAL
        )
        itemDecoration.setDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.divider_bg)))

        appAdapter.setOnItemClickListener { _, _, position ->
            Toast.makeText(this, appAdapter.getItem(position)?.appName, Toast.LENGTH_SHORT).show()
        }

        appAdapter.openLoadAnimation()

        appRecycleView.run {
            layoutManager = LinearLayoutManager(this@AppListActivity)
            addItemDecoration(itemDecoration)
            setHasFixedSize(true)
            adapter = appAdapter
        }
    }

    private fun refresh() {
        val installedAppBean = AppManager.getInstalledAppBean(this)
        appAdapter.setNewData(installedAppBean)
        appRefreshLayout.isRefreshing = false
    }
}
