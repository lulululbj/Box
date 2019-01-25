package luyao.box.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by luyao
 * on 2019/1/14 10:57
 */
class SpaceItemDecoration(space: Int) : RecyclerView.ItemDecoration() {

    private val mSpace = space

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = mSpace
        outRect.right = mSpace
        outRect.top = mSpace
        outRect.bottom = mSpace
    }
}