package com.example.sdkwithgpm4

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.cyberark.identity.util.biometric.CyberArkBiometricCallback
import com.cyberark.identity.util.biometric.CyberArkBiometricManager
import com.cyberark.identity.util.biometric.CyberArkBiometricPromptUtility
import com.example.library.MyLibProvider

class MainActivity : AppCompatActivity() {

    // SDK biometrics utility variable
    private lateinit var cyberArkBiometricPromptUtility: CyberArkBiometricPromptUtility

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Invoke biometric utility instance
        cyberArkBiometricPromptUtility =
            CyberArkBiometricManager().getBiometricUtility(biometricCallback)

        val callLibrary: Button = findViewById(R.id.call_library)
        callLibrary.setOnClickListener {
            MyLibProvider.testSDK(this)
        }

        val biometrics: Button = findViewById(R.id.invoke_biometrics)
        biometrics.setOnClickListener {
            showBiometrics()
        }
    }

    // ************************ Handle biometrics Start **************************** //
    /**
     * Show all strong biometrics in a prompt
     * negativeButtonText: "Use App Pin" text in order to handle fallback scenario
     * useDevicePin: true/false (true when biometrics is integrated with device pin as fallback else false)
     *
     */
    private fun showBiometrics() {
        cyberArkBiometricPromptUtility.showBioAuthentication(this, null, "Use App Pin", false)
    }

    /**
     * Callback to handle biometrics response
     */
    private val biometricCallback = object : CyberArkBiometricCallback {
        override fun isAuthenticationSuccess(success: Boolean) {
            // Show Authentication success message using Toast
            Toast.makeText(
                this@MainActivity,
                getString(R.string.authentication_is_successful),
                Toast.LENGTH_LONG
            ).show()
        }

        override fun passwordAuthenticationSelected() {
            Toast.makeText(
                this@MainActivity,
                "Password authentication is selected",
                Toast.LENGTH_LONG
            ).show()
        }

        override fun showErrorMessage(message: String) {
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
        }

        override fun isHardwareSupported(boolean: Boolean) {
            if (!boolean) {
                Toast.makeText(
                    this@MainActivity,
                    "Hardware is not supported",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        override fun isSdkVersionSupported(boolean: Boolean) {
            Toast.makeText(
                this@MainActivity,
                "SDK version is not supported",
                Toast.LENGTH_LONG
            ).show()
        }

        override fun isBiometricEnrolled(boolean: Boolean) {
            if (!boolean) {
                Toast.makeText(
                    this@MainActivity,
                    "Biometric is not enrolled",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        override fun biometricErrorSecurityUpdateRequired() {
            Toast.makeText(
                this@MainActivity,
                "Biometric error, security update is required",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    // ************************ Handle biometrics End ******************************** //
}