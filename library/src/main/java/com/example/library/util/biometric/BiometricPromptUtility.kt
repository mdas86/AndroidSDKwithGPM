package com.example.library.util.biometric

import androidx.appcompat.app.AppCompatActivity

interface BiometricPromptUtility {
    /**
     * Show biometrics authentication
     *
     * @param activity: Activity instance
     * @param retries: no of attempts
     * @param negitiveButtonText: negative button text
     * @param useDevicePin: user device pin flag, true/false
     */
    fun showBioAuthentication(
        activity: AppCompatActivity,
        retries: Int?,
        negitiveButtonText: String?,
        useDevicePin: Boolean = false
    )
}