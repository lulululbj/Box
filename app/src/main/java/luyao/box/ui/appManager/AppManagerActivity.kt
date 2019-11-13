package luyao.box.ui.appManager

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_app.*
import luyao.box.R
import luyao.box.bean.AppBean
import luyao.box.ui.file.FileActivity
import luyao.util.ktx.base.BaseVMActivity
import luyao.util.ktx.ext.gone
import luyao.util.ktx.ext.listener.onPageSelected
import luyao.util.ktx.ext.loge
import luyao.util.ktx.ext.startKtxActivity

/**
 * Created by luyao
 * on 2019/10/29 13:50
 */
class AppManagerActivity : BaseVMActivity<AppViewModel>() {

    companion object{
        val REVERSE = "reverse"
    }

    private var selectedPosition = 0
    private val titles = arrayListOf("三方应用", "系统应用", "本地安装包")
    private val isReverse by lazy { intent.getBooleanExtra(REVERSE,false) }
    private val fragments = ArrayList<AppFragment>()
    private val systemAppFragment by lazy { AppFragment.getInstance(AppFragment.TYPE_SYSTEM,isReverse) }
    private val thirdAppFragment by lazy { AppFragment.getInstance(AppFragment.TYPE_THIRD,isReverse) }
    private val localAppFragment by lazy { AppFragment.getInstance(AppFragment.TYPE_LOCAL,isReverse) }


    override fun providerVMClass() = AppViewModel::class.java
    override fun getLayoutResId() = R.layout.activity_app

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
        fragments.run {
            add(thirdAppFragment)
            add(systemAppFragment)
            add(localAppFragment)
        }
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

    fun loadLocalApk() {
        mViewModel.getLocalApkList(this)
    }

    fun reverseApp(app: AppBean) {
        mViewModel.reverseApp(app)
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

            appRefreshLayout.isRefreshing = false
        })

        mViewModel.reverseUiState.observe(this, Observer {
            it.isLoading?.let { show ->
                reverseView.visibility = if (show) View.VISIBLE else View.GONE
            }

            it.currentFile?.let { fileName ->
                "reverse : $fileName".loge("box")
                currentReversetFile.text = fileName
            }

            it.showSuccess?.let { file ->
                reverseView.gone()
                startKtxActivity<FileActivity>(value = FileActivity.PATH to file.path)
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