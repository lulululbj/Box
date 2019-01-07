package luyao.box.ui.editor

import kotlinx.android.synthetic.main.activity_text_viewer.*
import luyao.box.R
import luyao.box.common.base.BaseActivity
import java.io.File
import java.nio.charset.Charset

class TextViewerActivity: BaseActivity() {

    private val filePath by lazy { intent.getStringExtra("filePath") }
    override fun getLayoutResId()= R.layout.activity_text_viewer

    override fun initView() {

    }

    override fun initData() {

        webView.loadData(File(filePath).readText(charset = Charset.forName("utf-8")),"*/*","utf-8")
//        webView.loadUrl("http://sunluyao.com")
    }
}