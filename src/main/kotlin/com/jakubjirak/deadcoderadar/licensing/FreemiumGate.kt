package com.jakubjirak.deadcoderadar.licensing

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service

/**
 * Central gate for freemium feature access.
 * Free tier: single file scan, gutter icons, basic drift scores.
 * Paid tier: project-wide scanning, historical trends, CI export.
 */
@Service(Service.Level.APP)
class FreemiumGate {

    enum class LicenseStatus {
        FREE, TRIAL, PAID, EXPIRED
    }

    /**
     * Check if a paid feature is available for the current license.
     */
    @Suppress("UNUSED_PARAMETER")
    fun isPaidFeatureAvailable(featureId: String): Boolean {
        val status = getLicenseStatus()
        return status == LicenseStatus.PAID || status == LicenseStatus.TRIAL
    }

    fun getLicenseStatus(): LicenseStatus {
        // TODO: Integrate with JetBrains Marketplace Licensing API
        return LicenseStatus.FREE
    }

    companion object {
        const val FEATURE_PROJECT_SCAN = "project_scan"
        const val FEATURE_HISTORICAL_TRENDS = "historical_trends"
        const val FEATURE_CI_EXPORT = "ci_export"
        const val FEATURE_METHOD_LEVEL = "method_level"

        fun getInstance(): FreemiumGate =
            ApplicationManager.getApplication().getService(FreemiumGate::class.java)
    }
}
