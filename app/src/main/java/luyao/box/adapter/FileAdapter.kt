package luyao.box.adapter

import android.view.View
import android.widget.ImageButton
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import luyao.box.R
import luyao.box.bean.IFile

class FileAdapter(layoutResId: Int = R.layout.item_file) : BaseQuickAdapter<IFile, BaseViewHolder>(layoutResId) {

    var selectMode: Boolean = false
    lateinit var mMenuClickListener: ((View,IFile) -> Unit)

    fun setMenuClickListener(menuClickListener: (View,IFile) -> Unit) {
        this.mMenuClickListener = menuClickListener
    }

    override fun convert(helper: BaseViewHolder, item: IFile) {

        if (selectMode) {
            helper.setImageResource(
                R.id.fileMore,
                if (item.checked) R.drawable.ic_check_box_checked else R.drawable.ic_check_box_normal
            )
        } else {
            helper.setImageResource(R.id.fileMore, R.drawable.ic_more_vert)
        }
        helper.setText(R.id.fileName, item.getName())
        helper.setImageResource(R.id.fileIcon, if (item.isDirectory()) R.drawable.ic_folder else R.drawable.ic_file)

        helper.getView<ImageButton>(R.id.fileMore).setOnClickListener {
            mMenuClickListener(it,item)
        }
    }




//    fun setSelectMode(isSelect: Boolean) = {
//        selectMode = isSelect
//    }
//
//    fun isSelectMode(): Boolean = selectMode
}