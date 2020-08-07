package com.financial.transactiontracking;

import android.content.Context;

import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;


public class BiometricSecurityManager {

    private Context context;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @RequiresApi(api = Build.VERSION_CODES.P)
    BiometricSecurityManager(Context context, MainActivity activity, LoginHandler loginHandler) {
        System.out.println("Building manager");
        this.context = context;
        biometricPrompt = new BiometricPrompt(activity, ContextCompat.getMainExecutor(context), new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                System.out.println("Authentication failed");
                super.onAuthenticationError(errorCode, errString);
                if (errString == "Cancel") {
                    return;
                }
                Toast.makeText(context,
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                loginHandler.loginSuccess();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                loginHandler.loginFailure();
            }
        });
    }


    //Check if the user can use biometric security. If they can, show dialog.
    public void authenticate() {
        BiometricManager manager = BiometricManager.from(context);

        if (authenticationHandler(manager.canAuthenticate())) {
            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Authenticate User")
                    .setSubtitle("Biometric Authentication")
                    .setDescription("Please scan finger")
                    .setNegativeButtonText("Cancel").build();
            System.out.println("Build promptInfo");
            biometricPrompt.authenticate(promptInfo);
        }
    }

    private boolean authenticationHandler(int code) {
        switch (code) {
            case BiometricPrompt.ERROR_HW_UNAVAILABLE:
                Toast.makeText(context, "No sensor found", Toast.LENGTH_SHORT).show();
                return false;
            case BiometricPrompt.ERROR_UNABLE_TO_PROCESS:
                Toast.makeText(context, "Sensor was unable to process fingerprint. Please try again.",
                        Toast.LENGTH_LONG).show();
                return false;
            case BiometricPrompt.ERROR_TIMEOUT:
                Toast.makeText(context, "No finger was recognized within 30 seconds.",
                        Toast.LENGTH_SHORT).show();
                return false;
            case BiometricPrompt.ERROR_NO_SPACE:
                Toast.makeText(context, "Not enough storage to complete operation. Please free some space or login with username and password",
                        Toast.LENGTH_LONG).show();
                return false;
            case BiometricPrompt.ERROR_CANCELED:
                Toast.makeText(context, "The sensor was busy with something. Please try again.",
                        Toast.LENGTH_SHORT).show();
                return false;
            case BiometricPrompt.ERROR_LOCKOUT:
                Toast.makeText(context, "5 failed attempts have been made. Please try again in 30 seconds.",
                        Toast.LENGTH_LONG).show();
                return false;
            case BiometricPrompt.ERROR_LOCKOUT_PERMANENT:
                Toast.makeText(context, "Too many failed attempts have been made. Login will not be possible until unlocked with biometric PIN.",
                        Toast.LENGTH_LONG).show();
                return false;
            case BiometricPrompt.ERROR_USER_CANCELED:
                Toast.makeText(context, "You have cancelled the biometric login.",
                        Toast.LENGTH_SHORT).show();
                return false;
            case BiometricPrompt.ERROR_NO_BIOMETRICS:
                Toast.makeText(context, "No fingerprints were found on device. Please record at least one finger to continue.",
                        Toast.LENGTH_LONG).show();
                return false;
            case BiometricPrompt.ERROR_HW_NOT_PRESENT:
                Toast.makeText(context, "No sensor detected. Please login with username and password.",
                        Toast.LENGTH_SHORT).show();
                return false;
            case BiometricPrompt.ERROR_VENDOR:
                Toast.makeText(context, "An unforeseen error has occurred. Please restart the app and try again.",
                        Toast.LENGTH_LONG).show();
                return false;
            default:
                return true;
        }
    }
}
