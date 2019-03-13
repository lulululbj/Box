package luyao.box

import android.os.Environment
import luyao.box.adapter.MainAdapter
import luyao.box.bean.MainBean
import luyao.box.ui.activity.CurrentActivity
import luyao.box.ui.appManager.AppListActivity
import luyao.box.ui.device.DeviceInfoActivity
import java.io.File

/**
 * Created by luyao
 * on 2019/1/2 10:04
 */

val BASE_PATH = "${Environment.getExternalStorageDirectory().path}${File.separator}Box${File.separator}"
val APK_PATH = "${BASE_PATH}apk${File.separator}"

const val GITHUB_PAGE = "https://github.com/lulululbj/Box"
const val ISSUE_URL="https://github.com/lulululbj/Box/issues"
const val DEVELOPER_EMAIL="sunluyao1993x@gmail.com"
const val HOME_PAGE = "http://sunluyao.com"

val MAIN_LIST = mutableListOf(
    MainBean(R.drawable.ic_menu_slideshow,"本机信息", DeviceInfoActivity::class.java),
    MainBean(R.drawable.ic_menu_camera, "应用管理", AppListActivity::class.java),
    MainBean(R.drawable.ic_menu_gallery, "Activity历史", CurrentActivity::class.java),
    MainBean(R.drawable.ic_add_circle,"Coming Soon",MainAdapter::class.java)
)



