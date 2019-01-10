package luyao.box.util

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import luyao.box.R
import luyao.box.adapter.HistoryAdapter
import luyao.box.bean.HistoryBean
import luyao.box.dp2px

/**
 * Created by luyao
 * on 2019/1/10 14:37
 */
class FloatWindowManager(mContext: Context) {


    private val windowManager by lazy { mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager }
    private val windowAdapter by lazy { HistoryAdapter() }
    private val view by lazy { LayoutInflater.from(mContext).inflate(R.layout.window_history, null) }
    private val layoutParams: WindowManager.LayoutParams by lazy { WindowManager.LayoutParams() }
    private val windowRecycleView by lazy { view.findViewById<RecyclerView>(R.id.windowRecycleView) }

    init {
        layoutParams.run {
            gravity=Gravity.START and Gravity.TOP
            width = mContext.dp2px(200)
            height = mContext.dp2px(150)
            format = PixelFormat.TRANSPARENT
            flags = 0x18
            type = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1)
                WindowManager.LayoutParams.TYPE_TOAST
            else
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }


        windowRecycleView.run {
            layoutManager = LinearLayoutManager(mContext)
            adapter = windowAdapter
        }
        windowManager.addView(view, layoutParams)
    }

    fun addItem(historyBean: HistoryBean) {
        windowAdapter.addData(0, historyBean)
        windowAdapter.notifyDataSetChanged()
        windowRecycleView.scrollToPosition(0)
        windowManager.updateViewLayout(view, layoutParams)
    }


}