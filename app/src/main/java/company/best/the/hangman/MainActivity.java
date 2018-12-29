//i didn't put any comments because i was lazy. have fun.
package company.best.the.hangman;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import logic.HangmanLogic;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    static HangmanLogic game;
    static InputStream wordStream;
    ArrayList<String> words;
    FileOutputStream fOut;
    final String myPrefs = "MyPreferences_001";
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton playButton = findViewById(R.id.playbutton);
        playButton.setOnClickListener(this);
        ImageButton settingsButton = findViewById(R.id.settingsbutton);
        settingsButton.setOnClickListener(this);
        ImageButton helpButton = findViewById(R.id.helpbutton);
        helpButton.setOnClickListener(this);
        sp = getSharedPreferences(myPrefs, 0);
        InputStream wordStream = getApplicationContext().getResources().openRawResource(R.raw.words_small);
        if(sp.getBoolean("firstRun", true)){
            createLocalWordList(readFile(wordStream));
            editor = sp.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();
        }
    }

    private ArrayList<String> readFile(InputStream inputFile){
        ArrayList<String> possibleWords = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputFile));
        try {
            String line = br.readLine();
            while(line != null){

                possibleWords.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(possibleWords);
        return possibleWords;
    }

    private void createLocalWordList(ArrayList<String> words){
        {
            try {
                fOut = openFileOutput("internalWords", Context.MODE_PRIVATE);
                for(String s: words){
                    String str = s + "\n";
                    fOut.write(str.getBytes());
                }
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            case R.id.helpbutton:
                Intent helpIntent = new Intent(this, aboutActivity.class);
                startActivity(helpIntent);
                break;
        }
    }
}
