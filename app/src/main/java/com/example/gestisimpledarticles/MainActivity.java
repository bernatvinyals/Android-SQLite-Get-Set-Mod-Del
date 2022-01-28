package com.example.gestisimpledarticles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final int ACTIVITY_SIMPLE = 0;
    private static final int ACTIVITY_FILTER = 1;
    private static final int ACTIVITY_ICON = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn;

        // layout simple
        btn = (Button) findViewById(R.id.btnLayoutSimple);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                activitySimple();
            }
        });

        setTitle("Article Menu");
    }

    private void activitySimple() {
        Intent i = new Intent(this, articleSimpleActivity.class );
        startActivityForResult(i,ACTIVITY_SIMPLE);
    }
}