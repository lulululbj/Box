package luyao.box.util

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yhao.floatwindow.FloatWindow
import com.yhao.floatwindow.MoveType
import com.yhao.floatwindow.Screen
import luyao.box.App
import luyao.box.R
import luyao.box.adapter.HistoryAdapter
import luyao.box.bean.HistoryBean

/**
 * Created by luyao
 * on 2019/1/10 14:37
 */
object FloatWindowManager {


    private val windowAdapter by lazy { HistoryAdapter() }
    private val view by lazy { LayoutInflater.from(App.CONTEXT).inflate(R.layout.window_history, null) }
    private val windowRecycleView by lazy { view.findViewById<RecyclerView>(R.id.windowRecycleView) }


    init {
        windowRecycleView.run {
            layoutManager = LinearLayoutManager(context)
            adapter = windowAdapter
        }

        FloatWindow.with(App.CONTEXT)
            .setView(view)
            .setWidth(Screen.width, 0.7f)
            .setHeight(Screen.height, 0.3f)
            .setDesktopShow(true)
            .build()

        addItem(HistoryBean("",""))
    }

    fun init(context: Context){
        windowRecycleView.run {
            layoutManager = LinearLayoutManager(context)
            adapter = windowAdapter
        }

        FloatWindow.with(context)
            .setView(view)
            .setWidth(Screen.width, 0.6f)
            .setHeight(Screen.width, 0.4f)
            .setMoveType(MoveType.active)
            .setDesktopShow(true)
            .build()
    }


    fun addItem(historyBean: HistoryBean) {
        windowAdapter.addData(0, historyBean)
        windowRecycleView.scrollToPosition(0)
//        windowAdapter.notifyDataSetChanged()
    }

    fun show(){
        FloatWindow.get().show()
    }

    fun hide(){
        FloatWindow.get().hide()
    }


}