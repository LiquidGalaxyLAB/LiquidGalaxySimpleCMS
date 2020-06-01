package com.example.simple_cms.top_bar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.simple_cms.MainActivity;
import com.example.simple_cms.account.LogIn;
import com.example.simple_cms.create.CreateActionsMain;
import com.example.simple_cms.import_google_drive.ImportGoogleDrive;
import com.example.simple_cms.my_storyboards.MyStoryBoard;

/**
 *
 */
public class TobBar extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void buttCreateMenu(View view) {
        Intent intent = new Intent(getApplicationContext(), CreateActionsMain.class);
        startActivity(intent);
    }

    public void buttMyStoryboardsMenu(View view) {
        Intent intent = new Intent(getApplicationContext(), MyStoryBoard.class);
        startActivity(intent);
    }

    public void buttImportGoogleDrive(View view) {
        Intent intent = new Intent(getApplicationContext(), ImportGoogleDrive.class);
        startActivity(intent);
    }

    public void buttConnectMenu(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void buttAccount(View view) {
        Intent intent = new Intent(getApplicationContext(), LogIn.class);
        startActivity(intent);
    }
}
