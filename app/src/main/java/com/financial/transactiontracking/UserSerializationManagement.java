package com.financial.transactiontracking;

import android.app.Activity;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import static android.content.Context.MODE_PRIVATE;

public class UserSerializationManagement {

    public static void saveUser(Activity activity, User user) {
        String FILE_NAME = "user.txt";
        FileOutputStream fos = null;
        try {
            fos = activity.openFileOutput(FILE_NAME, MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            assert fos != null;
            fos.write(new Gson().toJson(user).getBytes());
            fos.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static User loadUser(Activity activity, boolean firstTime) {
            if (firstTime) {
                return new User();
            }

            FileInputStream fis = null;
            try {
                String FILE_NAME = "user.txt";
                fis = activity.openFileInput(FILE_NAME);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Scanner scanner = new Scanner(fis);
            scanner.useDelimiter("\\Z");
            String userJson = scanner.next();
            scanner.close();
        return new Gson().fromJson(userJson, User.class);
    }
}

