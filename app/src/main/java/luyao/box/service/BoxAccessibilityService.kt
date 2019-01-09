package luyao.box.service

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class BoxAccessibilityService : AccessibilityService(){

    override fun onInterrupt() {

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            Log.e("box","${event.packageName}$\n${event.className}")
        }
    }
}