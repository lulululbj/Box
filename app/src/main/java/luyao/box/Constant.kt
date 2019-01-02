package luyao.box

import android.os.Environment
import java.io.File

/**
 * Created by luyao
 * on 2019/1/2 10:04
 */

 val BASE_PATH="${Environment.getExternalStorageDirectory().path}${File.separator}Box${File.separator}"
 val APK_PATH="${BASE_PATH}apk${File.separator}"

