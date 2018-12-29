package company.best.the.hangman;
//
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class aboutActivity extends AppCompatActivity implements View.OnClickListener{
    final String myPrefs = "MyPreferences_001";
    SharedPreferences sp;
    TextView wintext, losetext;
    FileOutputStream fOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        wintext = findViewById(R.id.wintext);
        losetext = findViewById(R.id.losetext);
        sp = getSharedPreferences(myPrefs, 0);
        Button resetButton = findViewById(R.id.resetbutton);
        resetButton.setOnClickListener(this);
        updateWinLoss();

    }

    private void updateWinLoss(){
        int wins = sp.getInt("gameWins", 0);
        int losses = sp.getInt("gameLosses", 0);
        wintext.setText("Games won: " + Integer.toString(wins));
        losetext.setText("Games Lost: " + Integer.toString(losses));

    }

    private void resetWordList(){
        InputStream wordStream = getApplicationContext().getResources().openRawResource(R.raw.words_small);
        createLocalWordList(readFile(wordStream));
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.resetbutton:
                resetWordList();
        }
    }
}
