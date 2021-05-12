<h1 align="center"> Biometric Authentication <img src='https://image.flaticon.com/icons/png/512/125/125503.png' width=30, heihgt=30> </h1>


<p align="center">
One method of protecting sensitive information or premium content within your app is to request biometric authentication, such as using face recognition or fingerprint recognition. In this application, we will authenticate using fingerprints.
</p>


<p align="center">
  <img src='https://github.com/ayhanunal/BiometricAuthentication/blob/main/ss/biometric_app.gif' width=300 heihgt=300>
</p>

### Gradle 
Add a dependency code to your **module**'s `build.gradle` file.
```gradle
dependencies {
    //biometrics dependency
    implementation "androidx.biometric:biometric:1.0.1"
}
```
## Usage
Open MainActivity and add the following code at the bottom of onCreate:

```kotlin
val executor = ContextCompat.getMainExecutor(this)
val biometricManager = BiometricManager.from(this)
```

Next add the following code block into the button's setOnClickListener method.

```kotlin
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
```

You’ll now create the authUser function which takes executor as a paremeter. You’ll use this function to perform biometric authentication if the device can handle it.

```kotlin
private fun authUser(executor: Executor) {
    
    //Here you create the authentication dialog by specifying its properties.
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
                //When the user authenticates successfully, onAuthenticationSucceeded is triggered.
                showStatusTextView("Authentication Success")
            }

            override fun onAuthenticationError(
                errorCode: Int, errString: CharSequence
            ) {
                super.onAuthenticationError(errorCode, errString)
                //When an error occurs that can’t be recovered from, onAuthenticationError is triggered.
                showStatusTextView("Error, Try Again")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                //This callback triggers when a biometric is valid but isn’t recognized.
                showStatusTextView("Authentication Failed")
            }
        })


    //You authenticate the dialog by passing promptInfo, which holds the dialog properties, to biometricPrompt so you can see the dialog.
    biometricPrompt.authenticate(promptInfo) 

}

fun showStatusTextView(msg: String){
    statusTextView.text = msg
    statusTextView.visibility = View.VISIBLE //initially -> GONE
}
```
