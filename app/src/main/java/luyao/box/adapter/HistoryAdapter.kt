package luyao.box.adapter

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import luyao.box.R
import luyao.box.bean.HistoryBean

/**
 * Created by luyao
 * on 2019/1/10 13:12
 */
class HistoryAdapter(layoutResId: Int = R.layout.item_history) :
    BaseQuickAdapter<HistoryBean, BaseViewHolder>(layoutResId) {

    private val SINGLE_WITH_LINE = 1
    private val SINGLE_NO_LINE = 2
    private val SAME_WITH_LINE = 3
    private val SAME_NO_LINE = 4
    private val FIRST_WITH_LINE = 5
    private val FIRST_NO_LINE = 6


    override fun convert(helper: BaseViewHolder, item: HistoryBean) {


        when (helper.itemViewType) {
            FIRST_NO_LINE -> {
                helper.setVisible(R.id.historyPackageName, true)
                helper.setText(R.id.historyPackageName, item.packageName)
                helper.getView<View>(R.id.historyLine).visibility = View.GONE
                helper.setTextColor(R.id.historyPackageName, Color.YELLOW)
                helper.setTextColor(R.id.historyWindowName, Color.YELLOW)
            }
            FIRST_WITH_LINE -> {
                helper.setVisible(R.id.historyPackageName, true)
                helper.setText(R.id.historyPackageName, item.packageName)
                helper.getView<View>(R.id.historyLine).visibility = View.VISIBLE
                helper.setTextColor(R.id.historyPackageName, Color.YELLOW)
                helper.setTextColor(R.id.historyWindowName, Color.YELLOW)
            }
            SINGLE_NO_LINE -> {
                helper.setVisible(R.id.historyPackageName, true)
                helper.setText(R.id.historyPackageName, item.packageName)
                helper.getView<View>(R.id.historyLine).visibility = View.GONE
            }

            SINGLE_WITH_LINE -> {
                helper.setVisible(R.id.historyPackageName, true)
                helper.setText(R.id.historyPackageName, item.packageName)
                helper.getView<View>(R.id.historyLine).visibility = View.VISIBLE
            }

            SAME_NO_LINE -> {
                helper.getView<TextView>(R.id.historyPackageName).visibility = View.GONE
                helper.getView<View>(R.id.historyLine).visibility = View.GONE
            }

            SAME_WITH_LINE -> {
                helper.getView<TextView>(R.id.historyPackageName).visibility = View.GONE
                helper.getView<View>(R.id.historyLine).visibility = View.VISIBLE
            }
        }

        helper.setText(R.id.historyWindowName, item.windowName)

    }

    override fun getItemViewType(position: Int): Int {

        if (itemCount < 2) return SINGLE_NO_LINE

        if (position == 0) {
            return if (data[position].packageName == data[position + 1].packageName)
                FIRST_NO_LINE
            else FIRST_WITH_LINE
        }

        if (position == itemCount - 1) {
            return if (data[position].packageName == data[position - 1].packageName)
                SAME_NO_LINE
            else SINGLE_NO_LINE
        }

        if (data[position].packageName == data[position - 1].packageName) {
            return if (data[position].packageName == data[position + 1].packageName)
                SAME_NO_LINE
            else SAME_WITH_LINE
        } else {
            return if (data[position].packageName == data[position + 1].packageName)
                SINGLE_NO_LINE
            else SINGLE_WITH_LINE
        }
    }
}