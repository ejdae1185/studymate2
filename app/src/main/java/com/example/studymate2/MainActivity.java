package com.example.studymate2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.appbar_main, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu :

                return true ;

            default :
                return super.onOptionsItemSelected(item) ;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button korButton = (Button) findViewById(R.id.btn_korean);
        korButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Koreanlist.class);
                startActivity(intent);
            }
        });


        Button engButton = (Button) findViewById(R.id.btn_english);
        engButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), English_Activity.class);
                startActivity(intent);

            }
        });

        Button mathButton = (Button) findViewById(R.id.btn_math);
        mathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MathList_Activity.class);
                startActivity(intent);

            }
        });


        Button scianceButton = (Button) findViewById(R.id.btn_sciance);
        scianceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScianceList_Activity.class);
                startActivity(intent);

            }
        });


        Button ArtButton = (Button) findViewById(R.id.btn_art);
        ArtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ArtList_Activity.class);
                startActivity(intent);

            }
        });

        Button musicButton = (Button) findViewById(R.id.btn_music);
        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MusicList_Activity.class);
                startActivity(intent);

            }
        });

        Button commuButton = (Button) findViewById(R.id.btn_community);
        commuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CommunityList_Activity.class);
                startActivity(intent);

            }
        });

        Button location = (Button) findViewById(R.id.action_location_main);
        commuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Location.class);
                startActivity(intent);

            }
        });
    }


}