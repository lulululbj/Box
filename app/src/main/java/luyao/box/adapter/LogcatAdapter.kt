package luyao.box.adapter

import android.graphics.Color
import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import luyao.box.R
import luyao.box.bean.LogcatBean

class LogcatAdapter(layoutResId: Int = R.layout.item_logcat) :
    BaseQuickAdapter<LogcatBean, BaseViewHolder>(layoutResId) {
    override fun convert(helper: BaseViewHolder, item: LogcatBean) {
        helper.setText(R.id.logcatText, item.logString)

        if (item.level == Log.ERROR) {
            helper.setTextColor(R.id.logcatText, Color.RED)
        }else{
            helper.setTextColor(R.id.logcatText, Color.BLACK)

        }
    }
}