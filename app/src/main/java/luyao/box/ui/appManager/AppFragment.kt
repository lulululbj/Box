package luyao.box.ui.appManager

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_app.*
import luyao.box.R
import luyao.box.adapter.AppAdapter
import luyao.box.bean.AppBean
import luyao.util.ktx.base.BaseFragment

/**
 * Created by luyao
 * on 2019/10/29 14:06
 */
class AppFragment : BaseFragment() {

    private val type by lazy { arguments?.getInt(TYPE, -1) }
    private val isReverse by lazy { arguments?.getBoolean(REVERSE, false) }

    companion object {
        const val TYPE = "type"
        const val REVERSE = "reverse"
        const val TYPE_SYSTEM = 0
        const val TYPE_THIRD = 1
        const val TYPE_LOCAL = 2
        fun getInstance(type: Int, isReverse: Boolean) = AppFragment().apply {
            arguments = Bundle().apply {
                putInt(TYPE, type)
                putBoolean(REVERSE, isReverse)
            }
        }
    }

    private val appAdapter by lazy { AppAdapter(onlyReverse = isReverse ?: false) }

    override fun getLayoutResId() = R.layout.fragment_app

    override fun initView() {
        initRecycleView()
    }

    override fun initData() {
        refresh()
    }

    private fun initRecycleView() {
        val itemDecoration = DividerItemDecoration(
            activity,
            DividerItemDecoration.VERTICAL
        )
        context?.let {
            itemDecoration.setDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        it,
                        R.color.divider_bg
                    )
                )
            )
        }

        appAdapter.setOnItemClickListener { _, _, position ->

        }

        appAdapter.setOnReverseApp { app ->
            (activity as? AppManagerActivity)?.reverseApp(app)
        }

        appAdapter.openLoadAnimation()

        appShimmerRecycleView.run {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(itemDecoration)
            setHasFixedSize(true)
            adapter = appAdapter
        }
    }

    fun refresh() {
        appShimmerRecycleView.showShimmerAdapter()
        (activity as? AppManagerActivity)?.let {
            when (type) {
                TYPE_SYSTEM -> it.loadSystemApp()
                TYPE_THIRD -> it.loadThirdApp()
                TYPE_LOCAL -> it.loadLocalApk()
            }
        }
    }

    fun refreshUI(installedAppBean: List<AppBean>) {
//        loadingView.gone()
//        appRecycleView.visible()
        appShimmerRecycleView.hideShimmerAdapter()
        appAdapter.setNewData(installedAppBean)
    }
}