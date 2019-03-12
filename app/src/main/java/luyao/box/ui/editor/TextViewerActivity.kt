package luyao.box.ui.editor

import android.annotation.SuppressLint
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.widget.SearchView
import kotlinx.android.synthetic.main.activity_text_viewer.*
import luyao.box.R
import luyao.box.common.base.BaseActivity
import luyao.box.common.util.AppUtils
import java.io.File

class TextViewerActivity : BaseActivity() {

    private val filePath by lazy { intent.getStringExtra("filePath") }
    override fun getLayoutResId() = R.layout.activity_text_viewer

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {

        mToolbar.title = File(filePath).name
        mToolbar.inflateMenu(R.menu.toolbar_search)

        webView.settings.run {
            javaScriptEnabled=true
            setAppCacheEnabled(true)
            setSupportZoom(true)
            defaultTextEncodingName="utf-8"
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress == 100) {
                    progressBar.visibility = WebView.GONE
                } else {
                    progressBar.visibility = WebView.VISIBLE
                    progressBar.progress = newProgress
                }
                super.onProgressChanged(view, newProgress)
            }
        }
        initSearchView()
    }

    override fun initData() {
        webView.loadUrl("file:///$filePath")
    }

    private fun initSearchView() {
        val menuItem = mToolbar.menu.findItem(R.id.menu_webview_search)
        val searchView = menuItem.actionView as SearchView

        val anotherItem = mToolbar.menu.findItem(R.id.menu_webview_setting)
        anotherItem.setOnMenuItemClickListener {
            AppUtils.openFile(this, File(filePath), "text/xml")
            true
        }

        val mSearchAutoComplete = searchView.findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text)

        //设置输入框提示文字样式
        mSearchAutoComplete.setHintTextColor(resources.getColor(android.R.color.white))//设置提示文字颜色
        mSearchAutoComplete.setTextColor(resources.getColor(android.R.color.white))//设置内容文字颜色

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                findUp.visibility = View.VISIBLE
                findDown.visibility = View.VISIBLE
                webView.findAllAsync(newText)
                return true
            }

        })

        searchView.setOnCloseListener {
            findUp.visibility = View.GONE
            findDown.visibility = View.GONE
            false
        }

        findDown.setOnClickListener { webView.findNext(true) }
        findUp.setOnClickListener { webView.findNext(false) }
    }
}