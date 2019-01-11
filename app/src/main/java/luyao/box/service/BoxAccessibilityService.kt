package luyao.box.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import luyao.box.bean.HistoryBean
import luyao.box.util.FloatWindowManager
import luyao.box.util.Preference

class BoxAccessibilityService : AccessibilityService() {

    companion object {
        var instance: BoxAccessibilityService? = null
    }

    private var showWindow by Preference(Preference.SHOW_WINDOW, false)

    override fun onInterrupt() {

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            Log.e("box", "${event.packageName}$\n${event.className}")
            if (showWindow)
                FloatWindowManager.addItem(HistoryBean(event.packageName.toString(), event.className.toString()))
        }
    }

    override fun onServiceConnected() {
        Log.e("box", "onServiceConnected")
        instance = this
        super.onServiceConnected()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.e("box", "onUnbind")
        instance = null
        return super.onUnbind(intent)
    }
}