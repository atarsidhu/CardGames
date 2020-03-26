package com.example.cardgames;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.zip.Inflater;

public class DifficultyPopUp extends AppCompatDialogFragment {

    private RadioButton rbNormal, rbHard;

    public static final String SHARED_PREF = "sharedPrefs";
    public static final String RBNORMAL = "rbNormal";
    public static final String RBHARD = "rbHard";

    private boolean onOff1, onOff2;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_difficulty_popup, null);
        View titleView = inflater.inflate(R.layout.dialog_title, null);

        builder.setView(view).setCustomTitle(titleView)
                .setNegativeButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveData();
            }
        });

        rbNormal = view.findViewById(R.id.radioButtonNormal);
        rbHard = view.findViewById(R.id.radioButtonHard);

        loadDate();
        updateViews();

        return builder.create();
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(RBNORMAL, rbNormal.isChecked());
        editor.putBoolean(RBHARD, rbHard.isChecked());

        editor.apply();
    }

    public void loadDate(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        onOff1 = sharedPreferences.getBoolean(RBNORMAL, true);
        onOff2 = sharedPreferences.getBoolean(RBHARD, false);
    }

    public void updateViews(){
        rbNormal.setChecked(onOff1);
        rbHard.setChecked(onOff2);
    }
}
