package com.example.library.util.biometric

class BiometricManager {

    fun getBiometricUtility(callback: BiometricCallback): BiometricPromptUtility {
        return BiometricPromptUtilityImpl(callback)
    }
}