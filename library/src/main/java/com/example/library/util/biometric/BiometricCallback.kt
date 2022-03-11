package com.example.library.util.biometric

interface BiometricCallback {
    /**
     * Is authentication success
     *
     * @param success
     */
    fun isAuthenticationSuccess(success: Boolean)

    /**
     * Password authentication selected
     *
     */
    fun passwordAuthenticationSelected()

    /**
     * Show error message
     *
     * @param message
     */
    fun showErrorMessage(message: String)

    /**
     * Is hardware supported
     *
     * @param boolean
     */
    fun isHardwareSupported(boolean: Boolean)

    /**
     * Is sdk version supported
     *
     * @param boolean
     */
    fun isSdkVersionSupported(boolean: Boolean)

    /**
     * Is biometric enrolled
     *
     * @param boolean
     */
    fun isBiometricEnrolled(boolean: Boolean)

    /**
     * Biometric error security update required
     *
     */
    fun biometricErrorSecurityUpdateRequired()
}