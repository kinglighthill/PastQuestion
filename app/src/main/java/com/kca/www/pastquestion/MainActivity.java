package com.kca.www.pastquestion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void cbtGame (View v) {
        Intent intent = new Intent(this, CBTGame.class);
        startActivity(intent);
    }

    public void gpCalc (View v) {
        Intent intent = new Intent(this,GPCalc.class);
        startActivity(intent);
    }
}
