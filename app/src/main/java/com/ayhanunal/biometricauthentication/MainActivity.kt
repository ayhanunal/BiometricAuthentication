package com.ayhanunal.biometricauthentication


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executor


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val executor = ContextCompat.getMainExecutor(this)
        val biometricManager = BiometricManager.from(this)

        launchAuthentication.setOnClickListener {
            when (biometricManager.canAuthenticate()) {
                BiometricManager.BIOMETRIC_SUCCESS ->
                    authUser(executor)
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                    Toast.makeText(
                        this,
                        getString(R.string.error_msg_no_biometric_hardware),
                        Toast.LENGTH_LONG
                    ).show()
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                    Toast.makeText(
                        this,
                        getString(R.string.error_msg_biometric_hw_unavailable),
                        Toast.LENGTH_LONG
                    ).show()
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                    Toast.makeText(
                        this,
                        getString(R.string.error_msg_biometric_not_setup),
                        Toast.LENGTH_LONG
                    ).show()
            }
        }



    }


    private fun authUser(executor: Executor) {

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authentication")
            .setSubtitle("Verify Your Identity")
            .setDescription("Authentication Required To Continue !!")
            .setDeviceCredentialAllowed(true)
            .build()

        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    showStatusTextView("Authentication Success")
                }

                override fun onAuthenticationError(
                    errorCode: Int, errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    showStatusTextView("Error, Try Again")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    showStatusTextView("Authentication Failed")
                }
            })


        biometricPrompt.authenticate(promptInfo)

    }

    fun showStatusTextView(msg: String){
        statusTextView.text = msg
        statusTextView.visibility = View.VISIBLE
    }


}