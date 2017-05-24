package com.example.nick.smartpatroldrone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.vov.vitamio.LibsChecker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        Intent intent = new Intent(MainActivity.this, DroneControlActivity.class);
        MainActivity.this.startActivity(intent);
        MainActivity.this.finish();
    }
}