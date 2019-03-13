package luyao.box.ui.appManager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_app_list.*
import kotlinx.android.synthetic.main.title_layout.*
import luyao.box.R
import luyao.box.adapter.AppAdapter
import luyao.box.common.base.BaseActivity
import luyao.box.util.AppManager

class AppListActivity : BaseActivity() {

    private val appAdapter by lazy { AppAdapter() }
    private val appReceiver by lazy { AppReceiver() }

    override fun getLayoutResId() = R.layout.activity_app_list

    override fun initView() {
        mToolbar.title = getString(R.string.app)
        initRefreshLayout()
        initRecycleView()
    }

    override fun initData() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        registerReceiver(appReceiver,intentFilter)
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
            startActivity(
                Intent(this, AppDetailActivity::class.java).putExtra(
                    "packageName",
                    appAdapter.getItem(position)?.packageName
                )
            )
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

    inner class AppReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_PACKAGE_ADDED ||
                    intent.action == Intent.ACTION_PACKAGE_REMOVED){
                refresh()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(appReceiver)
    }

}
