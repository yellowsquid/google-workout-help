package com.example.testapp;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends WearableActivity {

    private TextView mTextView;
    private ProgressBar pBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);
        pBar = (ProgressBar) findViewById(R.id.determinateBar);

        pBar.setProgress(50);
        // Enables Always-on
        setAmbientEnabled();
    }
}
