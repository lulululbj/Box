package luyao.box.adapter

import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import luyao.box.R
import luyao.box.bean.MainBean

/**
 * Created by luyao
 * on 2019/1/14 9:17
 */
class MainAdapter(layoutResId: Int = R.layout.item_main) : BaseQuickAdapter<MainBean, BaseViewHolder>(layoutResId) {

    override fun convert(helper: BaseViewHolder, item: MainBean) {

        if (helper.itemViewType == 0) {
            helper.setImageResource(R.id.mainIcon, R.drawable.ic_add_circle)
            helper.getView<TextView>(R.id.mainName).visibility = View.GONE
        } else {
            helper.setImageResource(R.id.mainIcon, item.resId)
            helper.setText(R.id.mainName, item.name)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == data.size - 1)
            0
        else
            1
    }
}