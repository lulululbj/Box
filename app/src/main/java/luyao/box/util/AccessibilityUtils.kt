package luyao.box.util

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import luyao.box.service.BoxAccessibilityService

object AccessibilityUtils {

    fun checkBoxAccessibilityEnabled(context: Context): Boolean = checkAccessibilityEnabled(
        "${context.packageName}/${BoxAccessibilityService::class.java.canonicalName.replace(context.packageName, "")}",
        context
    )


    /*
     * 不可用，高版本直接返回 Collections.emptyList()
     */
    private fun checkAccessibilityEnabled(serviceName: String, context: Context): Boolean {
        val manager: AccessibilityManager =
            context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledAccessibilityServiceList =
            manager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC)
        for (service in enabledAccessibilityServiceList) {
            if (service.id == serviceName)
                return true
        }
        return false
    }

    fun goToAccessibilitySetting(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        context.startActivity(intent)
    }
}