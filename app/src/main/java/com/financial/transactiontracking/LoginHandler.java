package com.financial.transactiontracking;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.Scanner;

public class LoginHandler {

    private EditText usernameEditText;
    private EditText passwordEditText;

    private CheckBox rememberMeCheckBox;
    private CheckBox biometricPreferredCheckBox;

    private BiometricSecurityManager biometricSecurityManager;
    private SharedPreferences encryptedSharedPreferences;
    private Activity activity;
    private boolean biometricDefault;
    private boolean rememberUsername;
    private boolean firstTime;
    private User loggedUser;

    LoginHandler(Activity activity, SharedPreferences encryptedSharedPreferences) throws GeneralSecurityException, IOException {
        this.encryptedSharedPreferences = encryptedSharedPreferences;
        this.activity = activity;

        ImageButton biometricSecurityButton = activity.findViewById(R.id.biometric_security_button);
        Button loginButton = activity.findViewById(R.id.login_button);

        usernameEditText = activity.findViewById(R.id.user_name_EditText);
        passwordEditText = activity.findViewById(R.id.password_edit_text);

        biometricPreferredCheckBox = activity.findViewById(R.id.biometric_check_box);
        rememberMeCheckBox = activity.findViewById(R.id.remember_me_check_box);

        biometricSecurityManager = new BiometricSecurityManager(activity.getApplicationContext(), (MainActivity) activity, this);
        firstTimeCheck();
        preferenceCheck();
        updateViews();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(login()) {
                    loginSuccess();
                }
                else {
                    loginFailure();
                }
            }
        });

        biometricSecurityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                biometricSecurityManager.authenticate();
            }
        });

    }

    private boolean login() {
        String storedUsername = encryptedSharedPreferences.getString("username", "usernameNotFound");
        String storedPassword = encryptedSharedPreferences.getString("password", "passwordNotFound");

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        return username.equals(storedUsername) && password.equals(storedPassword);

    }

    private void updateLoginPreferences() {
        if (!encryptedSharedPreferences.getBoolean("rememberMe", false)) {
            if (rememberMeCheckBox.isChecked()) {
                encryptedSharedPreferences.edit().putBoolean("rememberMe", true).apply();
            } else {
                encryptedSharedPreferences.edit().putBoolean("rememberMe", false).apply();
            }
        }
        if (!encryptedSharedPreferences.getBoolean("biometricLoginDefault", false)) {
            if (biometricPreferredCheckBox.isChecked()) {
                encryptedSharedPreferences.edit().putBoolean("biometricLoginDefault", true).apply();
            }
            else {
                encryptedSharedPreferences.edit().putBoolean("biometricLoginDefault", false).apply();
            }
        }
    }

    private void firstTimeCheck() {
        //If this is the first run of the app the following logic will return true.
        //Once the app is run again, the value of 'first-run' will have changed to false.
        if (encryptedSharedPreferences.getBoolean("first-run", true)) {
            encryptedSharedPreferences.edit().putBoolean("first-run", false).apply();

            new NewUserDialogFragment(activity, (MainActivity) activity).showDialog();
            firstTime = true;
        }
        else {
            firstTime = false;
        }
    }

    private void preferenceCheck() {
        //Check if the user wants to use biometrics as default login
        biometricDefault = encryptedSharedPreferences.getBoolean("biometricLoginDefault", false);

        //Check if the user wants their username to be remembered
        rememberUsername = encryptedSharedPreferences.getBoolean("rememberMe", false);
    }

    private void updateViews() {
        //If user wanted app to remember username
        if (rememberUsername) {
            usernameEditText.setText(encryptedSharedPreferences.getString("username", "Username"));
            rememberMeCheckBox.setChecked(true);
        }
        //If user wanted to use biometrics as default login
        if (biometricDefault) {
            biometricPreferredCheckBox.setChecked(true);
            biometricSecurityManager.authenticate();
        }
    }


    public String getUsername() {
        return usernameEditText.getText().toString();
    }

    public boolean isBiometricDefault() {
        return biometricDefault;
    }

    public boolean isRememberUsername() {
        return rememberUsername;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void loginSuccess() {
        System.out.println("Login Success");
        Toast.makeText(activity,
                "Authentication succeeded!", Toast.LENGTH_SHORT).show();
        updateLoginPreferences();
        Intent financialHomeIntent = new Intent(activity, FinancialHomeActivity.class);
        financialHomeIntent.putExtra("user", loggedUser);
        activity.startActivity(financialHomeIntent);
    }

    public void loginFailure() {
        System.out.println("Login Failure");
        Toast.makeText(activity, "Authentication failed",
                Toast.LENGTH_SHORT)
                .show();
        updateLoginPreferences();
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }
}
