package luyao.box.ui.editor

import android.annotation.SuppressLint
import android.view.View
import androidx.appcompat.widget.SearchView
import kotlinx.android.synthetic.main.activity_text_viewer.*
import luyao.box.R
import luyao.box.common.base.BaseActivity
import java.io.File

class TextViewerActivity : BaseActivity() {

    private val filePath by lazy { intent.getStringExtra("filePath") }
    override fun getLayoutResId() = R.layout.activity_text_viewer

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {

        mToolbar.title= File(filePath).name
        mToolbar.inflateMenu(R.menu.toolbar_search)

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.setAppCacheEnabled(true)
        webSettings.setSupportZoom(true)
        webSettings.defaultTextEncodingName = "utf-8"

        initSearchView()
    }

    override fun initData() {

//        webView.loadData(File(filePath).readText(),"text/xml","utf-8")
        webView.loadUrl("file:///$filePath")
    }

    private fun initSearchView(){
        val menuItem = mToolbar.menu.findItem(R.id.menu_webview_search)
        val searchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                findUp.visibility=View.VISIBLE
                findDown.visibility=View.VISIBLE
                webView.findAllAsync(newText)
                return true
            }

        })

        searchView.setOnCloseListener {
            findUp.visibility= View.GONE
            findDown.visibility= View.GONE
            false
        }

        findDown.setOnClickListener { webView.findNext(true) }
        findUp.setOnClickListener { webView.findNext(false) }
    }
}