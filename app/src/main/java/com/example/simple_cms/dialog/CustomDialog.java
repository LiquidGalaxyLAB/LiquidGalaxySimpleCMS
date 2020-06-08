package com.example.simple_cms.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.simple_cms.MainActivity;
import com.example.simple_cms.R;

import java.util.Objects;

/**
 * Create the toast for the message to the user
 */
public class CustomDialog extends AppCompatDialogFragment {

   //private final static String TAG_DEBUG = "DEBUGG_TOAST";

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.dialog_fragment, null);

        v.getBackground().setAlpha(220);

        TextView textMessage = v.findViewById(R.id.message);

        Bundle bundle = getArguments();
        String text = Objects.requireNonNull(bundle).getString("TEXT","");
        textMessage.setText(text);


        Button ok = v.findViewById(R.id.ok);
        ok.setOnClickListener(view -> dismiss());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        return builder.create();
    }

    public static void showDialog(AppCompatActivity activity, String Message){
        CustomDialog dialogFragment = new CustomDialog();
        Bundle bundle = new Bundle();
        bundle.putString("TEXT", Message);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(activity.getSupportFragmentManager(),"Image Dialog");
    }
}
