package com.lglab.diego.simple_cms.account;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.lglab.diego.simple_cms.R;
import com.lglab.diego.simple_cms.top_bar.TobBarActivity;

public class LogIn extends TobBarActivity {

    private Button buttAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_log_in);

        View topBar = findViewById(R.id.top_bar);
        buttAccount = topBar.findViewById(R.id.butt_account);

        changeButtonClickableBackgroundColor();
    }


    /**
     * Change the background color and the option clickable to false of the button_connect
     */
    private void changeButtonClickableBackgroundColor() {
        changeButtonClickableBackgroundColor(getApplicationContext(), buttAccount);
    }
}
