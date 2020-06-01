package com.example.simple_cms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.simple_cms.account.LogIn;
import com.example.simple_cms.create.CreateActionsMain;
import com.example.simple_cms.import_google_drive.ImportGoogleDrive;
import com.example.simple_cms.my_storyboards.MyStoryBoard;
import com.example.simple_cms.top_bar.TobBar;

public class MainActivity extends TobBar {

    //private final static String TAG_DEBUG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
