package luyao.box.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ProgressBar
import luyao.box.dp2px
import luyao.box.getHeight
import luyao.box.getWidth

/**
 * Created by luyao
 * on 2019/1/15 10:28
 */
class ProgressWebView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : WebView(context, attrs, defStyleAttr) {

    val progressBar by lazy { ProgressBar(context, null, android.R.attr.progressBarStyle) }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(
        context,
        attributeSet,
        android.R.attr.webViewStyle
    )

    init {
        progressBar.layoutParams =
                LayoutParams(context.dp2px(80), context.dp2px(80),(context.getWidth()-context.dp2px(80))/2,(context.getHeight()-context.dp2px(130))/2)
        Log.e("box","$bottom $top")
        addView(progressBar)

        webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress == 100) {
                    progressBar.visibility = GONE
                } else {
                    progressBar.visibility = VISIBLE
                    progressBar.progress = newProgress
                }
                super.onProgressChanged(view, newProgress)
            }
        }
    }
}