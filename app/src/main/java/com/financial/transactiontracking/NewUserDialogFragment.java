package com.financial.transactiontracking;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


public class NewUserDialogFragment extends DialogFragment {
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    MainActivity activity;

    EditText username;
    EditText password;

    EditText firstName;
    EditText lastName;

    NewUserDialogFragment(Context context, MainActivity activity) {
        this.builder = new AlertDialog.Builder(context);
        this.activity = activity;
        createDialog();
    }

    public void createDialog() {
        LayoutInflater inflater = activity.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.new_user_dialog, null))
                .setPositiveButton(R.string.positive_button_cont, (dialog, id) -> {
                    username = alertDialog.findViewById(R.id.username_edit_text);
                    password = alertDialog.findViewById(R.id.password_edit_text);
                    firstName = alertDialog.findViewById(R.id.first_name_edit_text);
                    lastName = alertDialog.findViewById(R.id.last_name_edit_text);

                    activity.onPositiveClick(NewUserDialogFragment.this);
                })
                 .setNegativeButton(R.string.negative_button_dialog, (dialog, id) -> {
                     username = alertDialog.findViewById(R.id.username_edit_text);
                     password = alertDialog.findViewById(R.id.password_edit_text);
                     firstName = alertDialog.findViewById(R.id.first_name_edit_text);
                     lastName = alertDialog.findViewById(R.id.last_name_edit_text);

                     activity.onNegativeClick(NewUserDialogFragment.this);
                 });
        alertDialog = builder.create();

    }

    public void showDialog() {
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialog.show();
    }
}
