//i didn't put any comments because i was lazy. have fun.
package company.best.the.hangman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import logic.HangmanLogic;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    static HangmanLogic game;
    static InputStream wordStream;
    ArrayList<String> words;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton playButton = findViewById(R.id.playbutton);
        playButton.setOnClickListener(this);
        ImageButton settingsButton = findViewById(R.id.settingsbutton);
        settingsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.playbutton:
                Intent playIntent = new Intent(this, gameActivity.class);
                startActivity(playIntent);
                break;
            case R.id.settingsbutton:
                Intent settingsIntent = new Intent(this, settingsActivity.class);
                startActivity(settingsIntent);
                break;
        }
    }
}
