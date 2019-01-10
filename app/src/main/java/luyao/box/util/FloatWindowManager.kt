package luyao.box.util

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yhao.floatwindow.FloatWindow
import com.yhao.floatwindow.Screen
import luyao.box.R
import luyao.box.adapter.HistoryAdapter
import luyao.box.bean.HistoryBean
import luyao.box.dp2px

/**
 * Created by luyao
 * on 2019/1/10 14:37
 */
class FloatWindowManager(mContext: Context) {


    private val windowAdapter by lazy { HistoryAdapter() }
    private val view by lazy { LayoutInflater.from(mContext).inflate(R.layout.window_history, null) }
    private val windowRecycleView by lazy { view.findViewById<RecyclerView>(R.id.windowRecycleView) }

    init {

        windowRecycleView.run {
            layoutManager = LinearLayoutManager(mContext)
            adapter = windowAdapter
        }

        FloatWindow.with(mContext)
            .setView(view)
            .setWidth(Screen.width,0.6f)
            .setHeight(Screen.width,0.4f)
            .setDesktopShow(true)
            .build()
    }

    fun addItem(historyBean: HistoryBean) {
        windowAdapter.addData(0, historyBean)
        windowRecycleView.scrollToPosition(0)
    }


}