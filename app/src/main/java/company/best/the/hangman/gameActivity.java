package company.best.the.hangman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import logic.HangmanLogic;

import static company.best.the.hangman.MainActivity.game;

public class gameActivity extends AppCompatActivity implements View.OnClickListener{
    HangmanLogic game;
    TextView visibleWord, wrongLettersText;
    EditText inputLetter;
    Button guessButton;
    int errors = 0;
    int guesses = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        guessButton = findViewById(R.id.guessbutton);
        guessButton.setOnClickListener(this);
        visibleWord = findViewById(R.id.wordtoguess);
        inputLetter = findViewById(R.id.inputletter);

        InputStream wordStream = getApplicationContext().getResources().openRawResource(R.raw.words);
        game = new HangmanLogic(wordStream);
        game.reset();
        visibleWord.setText(game.getVisibleWord());
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
            if(game.isGameIsWon()){
                resultIntent.putExtra("result", true);
                resultIntent.putExtra("guesses", guesses);
            }else{
                resultIntent.putExtra("result", false);
                resultIntent.putExtra("word", game.getWordToGuess());
            }
            startActivityForResult(resultIntent, 0);
            //startActivity(resultIntent);
            reset();
        }
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
