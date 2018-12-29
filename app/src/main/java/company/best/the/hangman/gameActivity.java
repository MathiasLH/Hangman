package company.best.the.hangman;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import logic.HangmanLogic;

import static company.best.the.hangman.MainActivity.game;

public class gameActivity extends AppCompatActivity implements View.OnClickListener{
    HangmanLogic game;
    ArrayList<String> words;
    TextView visibleWord, wrongLettersText;
    EditText inputLetter;
    Button guessButton;
    int errors = 0;
    int guesses = 0;
    final int mode = Activity.MODE_PRIVATE;
    final String myPrefs = "MyPreferences_001";
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        guessButton = findViewById(R.id.guessbutton);
        guessButton.setOnClickListener(this);
        visibleWord = findViewById(R.id.wordtoguess);
        inputLetter = findViewById(R.id.inputletter);
        sp = getSharedPreferences(myPrefs, 0);
        words = readFile();
        game = new HangmanLogic(searchWords());
        game.reset();
        visibleWord.setText(game.getVisibleWord());
    }

    private ArrayList<String> searchWords() {
        return new ArrayList<>(Arrays.asList(words.stream().filter(x -> !sp.getBoolean("allowDashes", true) ? !x.contains("-"):true)
                .filter(x-> x.length() <= sp.getInt("maxWordLength", 10)).toArray(String[]::new)));
    }

    private void nextImage(){
        ImageView image = findViewById(R.id.image);
        errors++;
        switch(errors){
            case 0:
                image.setImageResource(R.drawable.ic_errors_0);
                break;
            case 1:
                image.setImageResource(R.drawable.ic_errors_1);
                break;
            case 2:
                image.setImageResource(R.drawable.ic_errors_2);
                break;
            case 3:
                image.setImageResource(R.drawable.ic_errors_3);
                break;
            case 4:
                image.setImageResource(R.drawable.ic_errors_4);
                break;
            case 5:
                image.setImageResource(R.drawable.ic_errors_5);
                break;
            case 6:
                image.setImageResource(R.drawable.ic_errors_6);
                break;
        }
    }

    private void getGameState(){
        Intent resultIntent = new Intent(this, resultActivity.class);
        if(game.isGameIsLost() || game.isGameIsWon()){
            updateScore(game.isGameIsWon());
            if(game.isGameIsWon()){
                resultIntent.putExtra("result", true);
                resultIntent.putExtra("guesses", guesses);
            }else{
                resultIntent.putExtra("result", false);
                resultIntent.putExtra("word", game.getWordToGuess());
            }
            startActivity(resultIntent);
            reset();
        }
    }

    private void updateScore(boolean gameWon){
        SharedPreferences.Editor editor = sp.edit();
        if(gameWon){
            int prevWins = sp.getInt("gameWins", 0);
            editor.putInt("gameWins", prevWins+1);
        }else{
            int prevLosses = sp.getInt("gameLosses", 0);
            editor.putInt("gameLosses", prevLosses+1);
        }
        editor.apply();
    }

    private void reset() {
        errors = -1;
        nextImage();
        guesses = 0;
        game.reset();
        TextView wrongLetters = findViewById(R.id.incorrecttext);
        wrongLetters.setText("Incorrect letters:");
        TextView visibleWord = findViewById(R.id.wordtoguess);
        visibleWord.setText(game.getVisibleWord());
    }

    private ArrayList<String> readFile(){
        ArrayList<String> possibleWords = new ArrayList<String>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(openFileInput("internalWords")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

    @Override
    public void onClick(View v) {
        TextView wrongLetters = findViewById(R.id.incorrecttext);
        String letter = inputLetter.getText().toString().toLowerCase();
        System.out.println("The guessed letter is: " + letter);
        System.out.println("usedLetters contains:");
        for(int i = 0; i < game.getUsedLetters().size(); i++){
            System.out.println(game.getUsedLetters().get(i));
        }
        switch(v.getId()){
            case R.id.guessbutton:
                if(!game.getUsedLetters().contains(letter)){
                    game.guessLetter(letter);
                    guesses++;
                    if(!game.wasLastLetterCorrect()){
                        wrongLetters.append(" " + inputLetter.getText());
                        nextImage();
                    }
                    visibleWord.setText(game.getVisibleWord());

                    getGameState();
                }
                inputLetter.setText("");
                break;
        }
    }
}
