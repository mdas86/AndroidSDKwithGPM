package com.example.sdkwithgpm4

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.library.MyLibProvider
import com.example.library.util.biometric.BiometricCallback
import com.example.library.util.biometric.BiometricManager
import com.example.library.util.biometric.BiometricPromptUtility

class MainActivity : AppCompatActivity() {

    // SDK biometrics utility variable
    private lateinit var biometricPromptUtility: BiometricPromptUtility

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Invoke biometric utility instance
        biometricPromptUtility =
            BiometricManager().getBiometricUtility(biometricCallback)

        val callLibrary: Button = findViewById(R.id.call_library)
        callLibrary.setOnClickListener {
            MyLibProvider.testSDK(this)
        }

        val biometrics: Button = findViewById(R.id.invoke_biometrics)
        biometrics.setOnClickListener {
            showBiometrics()
        }
    }


    private fun showBiometrics() {
        biometricPromptUtility.showBioAuthentication(this, null, "Use App Pin", false)
    }

    /**
     * Callback to handle biometrics response
     */
    private val biometricCallback = object : BiometricCallback {
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
}