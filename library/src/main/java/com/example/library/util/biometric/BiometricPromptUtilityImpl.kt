package com.example.library.util.biometric

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.library.R

internal class BiometricPromptUtilityImpl(private val callback: BiometricCallback) :
    BiometricPromptUtility {

    private val tag = "BiometricPromptUtility"
    private var mMaxRetries = 3
    private var mFailedTries = 0

    private var isAutoCancelElabled = true
    private var mPrompt: BiometricPrompt? = null
    private var negitiveButtonText: String? = null

    private var useDevicePin: Boolean = false

    /**
     * Create biometrics authentication
     *
     * @param activity: Activity instance
     * @return BiometricPrompt
     */
    private fun createBioAuthentication(
        activity: AppCompatActivity
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)
        mPrompt = getBioMetricPrompt(activity, executor, getBioMetricCallback())
        return mPrompt!!
    }

    /**
     * Get biometrics callback
     *
     */
    private fun getBioMetricCallback() = object : BiometricPrompt.AuthenticationCallback() {

        override fun onAuthenticationError(errCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errCode, errString)
            if (errCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                this@BiometricPromptUtilityImpl.callback.passwordAuthenticationSelected()
                mPrompt!!.cancelAuthentication()
            } else {
                this@BiometricPromptUtilityImpl.callback.showErrorMessage(errString.toString())
            }
            mPrompt = null
            Log.d(tag, "errCode is $errCode and errString is: $errString")
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            if (isAutoCancelElabled) {
                if (mFailedTries < mMaxRetries) {
                    mFailedTries++
                } else {
                    //If same object used multiple times by client
                    mFailedTries = 0
                    mPrompt?.cancelAuthentication()
                    this@BiometricPromptUtilityImpl.callback.isAuthenticationSuccess(
                        false
                    )
                }
            }
            mPrompt = null
            Log.d(tag, "User biometric rejected.")
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            Log.d(tag, "Authentication was successful")
            decryptServerTokenFromStorage(result)
            mPrompt = null
        }
    }

    /**
     * Get biometrics prompt
     *
     * @param activity: Activity instance
     * @param executor: Executor instance
     * @param callback: BiometricPrompt.AuthenticationCallback instance
     */
    private fun getBioMetricPrompt(
        activity: AppCompatActivity,
        executor: java.util.concurrent.Executor,
        callback: BiometricPrompt.AuthenticationCallback
    ) = BiometricPrompt(activity, executor, callback)

    /**
     * Show biometrics authentication
     *
     * @param activity: Activity instance
     * @param retries: no of attempts
     * @param negitiveButtonText: negative button text
     * @param useDevicePin: use device pin flag, true/false
     */
    override fun showBioAuthentication(
        activity: AppCompatActivity,
        retries: Int?,
        negitiveButtonText: String?,
        useDevicePin: Boolean
    ) {
        if (mPrompt != null) {
            return
        }
        if (retries == null || retries == 0) {
            isAutoCancelElabled = false
        }
        this.useDevicePin = useDevicePin
        this.negitiveButtonText = negitiveButtonText
        checkAndAuthenticate(activity)

    }

    /**
     * Show biometrics prompt
     *
     * @param activity: Activity instance
     */
    private fun showBiometricPrompt(activity: AppCompatActivity) {
        if (mPrompt != null) {
            return
        }
        val promptInfo = this.createPromptInfo(activity)
        this.createBioAuthentication(activity)
            .authenticate(promptInfo)
    }

    /**
     * Decrypt server token from device storage
     *
     * @param authResult: BiometricPrompt.AuthenticationResult instance
     */
    private fun decryptServerTokenFromStorage(authResult: BiometricPrompt.AuthenticationResult) {
        this@BiometricPromptUtilityImpl.callback.isAuthenticationSuccess(true)
    }

    /**
     * Check and authenticate biometrics selection
     * if not success, then handle error scenarios
     *
     * @param activity: Activity instance
     */
    private fun checkAndAuthenticate(activity: AppCompatActivity) {
        val biometricManager = getBioMetric(activity)
        println("Biometric manager $biometricManager")
        when (biometricManager.canAuthenticate(getSecurityType())) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                showBiometricPrompt(activity)
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                this.callback.isHardwareSupported(false)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                this.callback.isHardwareSupported(false)
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                this.callback.isBiometricEnrolled(false)
            }
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                this.callback.isHardwareSupported(false)
            }
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                this.callback.biometricErrorSecurityUpdateRequired()
            }
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                this.callback.isHardwareSupported(false)
            }
        }
    }

    /**
     * Invoke biometrics
     *
     * @param activity: Activity instance
     * @return BiometricManager
     */
    private fun getBioMetric(activity: AppCompatActivity): BiometricManager =
        BiometricManager.from(activity)


    /**
     * Get device security type
     *
     * @return Int
     */
    private fun getSecurityType(): Int {
        if (useDevicePin) {
            return BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
        }
        return BiometricManager.Authenticators.BIOMETRIC_STRONG
    }

    /**
     * Create biometrics prompt info
     *
     * @param activity: Activity instance
     * @return BiometricPrompt.PromptInfo instance
     */
    private fun createPromptInfo(activity: AppCompatActivity): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder().apply {
            setTitle(activity.getString(R.string.dialog_biometric_prompt_title))
            setDescription(activity.getString(R.string.dialog_biometric_prompt_desc))
            setConfirmationRequired(false)
            setAllowedAuthenticators(getSecurityType())
            if (!useDevicePin) {
                setAllowedAuthenticators(getSecurityType())
                setNegativeButtonText(negitiveButtonText!!)
                negitiveButtonText = null
            }
        }.build()
}