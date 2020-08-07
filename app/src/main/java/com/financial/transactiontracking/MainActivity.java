package com.financial.transactiontracking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.widget.LinearLayout;

import java.io.IOException;
import java.security.GeneralSecurityException;


public class MainActivity extends AppCompatActivity {
    Context context;
    LinearLayout parentLinearLayout;
    SharedPreferences encryptedSharedPrefrences;
    User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String KEY_ALIAS = getResources().getString(R.string.key_alias);

        context = getApplicationContext();
        parentLinearLayout = findViewById(R.id.parent_linear_layout);
        LoginHandler loginHandler = null;

        KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build();

        MasterKey masterKey = null;
        try {
            masterKey = new MasterKey.Builder(MainActivity.this)
                    .setKeyGenParameterSpec(spec)
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        try {
            encryptedSharedPrefrences = EncryptedSharedPreferences.create(
                    MainActivity.this,
                    "transactionTrackingPreferences",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        try {
            loginHandler = new LoginHandler(this, encryptedSharedPrefrences);
            loggedUser = UserSerializationManagement.loadUser(this, loginHandler.isFirstTime());
            loginHandler.setLoggedUser(loggedUser);
            if (loggedUser.getInstitutions().size() > 0) {
                System.out.println("Latest institution access token: " + loggedUser.getInstitutions().get(loggedUser.getInstitutions().size() - 1));
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }



    }


    public void onPositiveClick(NewUserDialogFragment dialogFragment) {
        encryptedSharedPrefrences.edit().putString("username", dialogFragment.username.getText().toString()).apply();
        encryptedSharedPrefrences.edit().putString("password", dialogFragment.password.getText().toString()).apply();

        loggedUser.setFirstLastName(dialogFragment.firstName.getText().toString(), dialogFragment.lastName.getText().toString());
        UserSerializationManagement.saveUser(this, loggedUser);
    }


    public void onNegativeClick(NewUserDialogFragment dialogFragment) {

    }
}