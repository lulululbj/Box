package luyao.box.bean

import android.graphics.drawable.Drawable

/**
 * Created by luyao
 * on 2018/12/28 16:32
 */
data class AppBean(
    val appName: String,
    val packageName: String,
    val versionName: String,
    val sourceDir:String,
    val icon: Drawable
)