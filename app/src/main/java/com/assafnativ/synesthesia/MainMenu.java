package com.assafnativ.synesthesia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends AppCompatActivity {
    static public boolean isTestMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void onStartButtonClick(View view) {
        isTestMode = false;
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void onSettingsButtonClick(View view) {

    }

    public void onTestButtonClick(View view) {
        isTestMode = true;
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}
