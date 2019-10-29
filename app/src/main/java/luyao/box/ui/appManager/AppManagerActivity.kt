package luyao.box.ui.appManager

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_app.*
import luyao.box.R
import luyao.util.ktx.base.BaseVMActivity
import luyao.util.ktx.ext.listener.onPageSelected

/**
 * Created by luyao
 * on 2019/10/29 13:50
 */
class AppManagerActivity : BaseVMActivity<AppViewModel>() {

    private var selectedPosition = 0
    private val titles = arrayListOf("三方应用", "系统应用", "本地安装包")
    private val fragments = ArrayList<AppFragment>()
    private val systemAppFragment by lazy { AppFragment.getInstance(AppFragment.TYPE_SYSTEM) }
    private val thirdAppFragment by lazy { AppFragment.getInstance(AppFragment.TYPE_THIRD) }
    private val localAppFragment by lazy { AppFragment.getInstance(AppFragment.TYPE_LOCAL) }

    override fun providerVMClass() = AppViewModel::class.java
    override fun getLayoutResId() = R.layout.activity_app

    init {
        fragments.run {
            add(systemAppFragment)
            add(thirdAppFragment)
            add(localAppFragment)
        }
    }

    override fun initView() {
        appToolbar.title = getString(R.string.reverse)
        setSupportActionBar(appToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        initViewPager()
        initRefreshLayout()
    }

    override fun initData() {
    }

    private fun initViewPager() {
        appViewPager.adapter = AppPagerAdapter(supportFragmentManager)
        appViewPager.offscreenPageLimit = 3
        appTabLayout.setupWithViewPager(appViewPager)
        appViewPager.onPageSelected { selectedPosition = it }
    }

    private fun initRefreshLayout() {
        appRefreshLayout.setOnRefreshListener {
            fragments[selectedPosition].refresh()
        }
    }

    fun loadSystemApp() {
        mViewModel.getAppList(this, true)
    }

    fun loadThirdApp() {
        mViewModel.getAppList(this, false)
    }

    fun loadLocalApk(){
        mViewModel.getLocalApkList(this)
    }


    override fun startObserve() {
        super.startObserve()
        mViewModel.uiState.observe(this, Observer {
            it.systemAppBeanList?.let { data ->
                systemAppFragment.refreshUI(data)
            }

            it.thirdAppBeanList?.let { data ->
                thirdAppFragment.refreshUI(data)
            }

            it.localAppBeanList?.let { data ->
                localAppFragment.refreshUI(data)
            }
        })
    }


    inner class AppPagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int) = fragments[position]

        override fun getCount() = fragments.size

        override fun getPageTitle(position: Int) = titles[position]

    }


}