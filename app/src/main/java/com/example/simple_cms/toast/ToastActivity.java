package com.example.simple_cms.toast;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simple_cms.R;

public class ToastModify {

    public static void showToast(Activity activity,String message){
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) activity.findViewById(R.id.toast_root));
        TextView toastText = layout.findViewById(R.id.toast_text);
        toastText.setText(message);
        Toast toast = new Toast(activity.getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
