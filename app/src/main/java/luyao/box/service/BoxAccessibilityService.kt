package luyao.box.service

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import luyao.box.App
import luyao.box.bean.HistoryBean
import luyao.box.util.FloatWindowManager
import luyao.box.util.Preference

class BoxAccessibilityService : AccessibilityService() {

    private var showWindow by Preference(Preference.SHOW_WINDOW, false)
    private val floatWindowManager by lazy { FloatWindowManager(App.CONTEXT) }

    override fun onInterrupt() {

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            Log.e("box", "${event.packageName}$\n${event.className}")
            if (showWindow)
                floatWindowManager.addItem(HistoryBean(event.packageName.toString(), event.className.toString()))
        }
    }
}