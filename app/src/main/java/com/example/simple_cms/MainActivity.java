package com.example.simple_cms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.simple_cms.account.LogIn;
import com.example.simple_cms.create.CreateActionsMain;
import com.example.simple_cms.import_google_drive.ImportGoogleDrive;
import com.example.simple_cms.my_storyboards.MyStoryBoard;

public class MainActivity extends AppCompatActivity {

    //private final static String TAG_DEBUG = "MainActivity";
    private View topBar;
    private Button buttCreateMenu, buttMyStoryboardsMenu, buttImportGoogleDrive, buttConnectMenu, buttAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topBar = findViewById(R.id.top_bar);
        buttCreateMenu = topBar.findViewById(R.id.butt_create_menu);
        buttMyStoryboardsMenu = topBar.findViewById(R.id.butt_my_storyboards_menu);
        buttImportGoogleDrive = topBar.findViewById(R.id.butt_import_google_drive);
        buttConnectMenu = topBar.findViewById(R.id.butt_connect_menu);
        buttAccount = topBar.findViewById(R.id.butt_account);

        buttCreateMenu.setOnClickListener((view) -> {
            try {
                Intent intent = new Intent(getApplicationContext(), CreateActionsMain.class);
                startActivity(intent);
            }catch (Exception e){

            }
        });

        buttMyStoryboardsMenu.setOnClickListener((view) -> {
            Intent intent = new Intent(getApplicationContext(), MyStoryBoard.class);
            startActivity(intent);
        });

        buttImportGoogleDrive.setOnClickListener((view) -> {
            Intent intent = new Intent(getApplicationContext(), ImportGoogleDrive.class);
            startActivity(intent);
        });


        buttAccount.setOnClickListener((view) -> {
            Intent intent = new Intent(getApplicationContext(), LogIn.class);
            startActivity(intent);
        });

    }
}
