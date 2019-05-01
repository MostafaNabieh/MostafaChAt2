package com.example.mostafachat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity {
    private final int welcomesound = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(welcomesound);
                } catch (Exception e) {

                } finally {
                    Mainactivity();
                }

            }
        };
        thread.start();
    }

    private void Mainactivity() {
        Intent main_intent = new Intent(this, MainActivity.class);
        startActivity(main_intent);
    }
}
