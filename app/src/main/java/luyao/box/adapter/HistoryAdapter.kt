package luyao.box.adapter

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

    override fun convert(helper: BaseViewHolder, item: HistoryBean) {
        val position = helper.layoutPosition

//        if (position == 0) {
//            helper.setVisible(R.id.historyPackageName, true)
//            helper.setText(R.id.historyPackageName, item.packageName)
//        }
//
//        if (position < itemCount - 1) {
//            if (item.packageName == getItem(position + 1)?.packageName) {
//                getViewByPosition(position + 1, R.id.historyPackageName)?.visibility = View.GONE
//            } else {
//                (getViewByPosition(position + 1, R.id.historyPackageName) as TextView).text =
//                        getItem(position + 1)?.packageName
//
//            }
//        }
        helper.setText(R.id.historyPackageName,item.packageName)
        helper.setText(R.id.historyWindowName, item.windowName)

    }
}