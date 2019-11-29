package luyao.box.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import luyao.box.bean.HistoryBean
import luyao.box.util.FloatWindowManager
import luyao.box.util.Preference
import luyao.util.ktx.ext.loge

class BoxAccessibilityService : AccessibilityService() {

    companion object {
        var instance: BoxAccessibilityService? = null
    }

//    private var showWindow by Preference(Preference.SHOW_WINDOW, false)

    override fun onInterrupt() {

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            "${event.packageName}$\n${event.className}".loge("box")
                FloatWindowManager.addItem(HistoryBean(event.packageName?.toString(), event.className?.toString()))
        }
    }

    override fun onServiceConnected() {
       "onServiceConnected".loge("box")
        instance = this
        super.onServiceConnected()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        "onUnbind".loge("box")
        instance = null
        return super.onUnbind(intent)
    }
}