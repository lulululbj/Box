package luyao.box.adapter

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
        if (position == 0 || getItem(position - 1)?.packageName != item.packageName) {
            helper.setVisible(R.id.historyPackageName, true)
            helper.setText(R.id.historyPackageName, item.packageName)
        } else {
            helper.setVisible(R.id.historyPackageName, false)
        }
        helper.setText(R.id.historyWindowName, item.windowName)
    }
}