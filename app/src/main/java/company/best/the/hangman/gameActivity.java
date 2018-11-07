package company.best.the.hangman;

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
        if(game.isGameIsLost() || game.isGameIsWon()){
            guessButton.setEnabled(false);
            TextView result = findViewById(R.id.resulttext);
            if(game.isGameIsWon()){
                result.setText("You win!");
            }else if(game.isGameIsLost()){
                result.setText("You lose!\nThe word was: " + game.getWordToGuess());
            }
        }
    }

    @Override
    public void onClick(View v) {
        TextView wrongLetters = findViewById(R.id.incorrecttext);
        switch(v.getId()){
            case R.id.guessbutton:
                if(!game.getUsedLetters().contains(inputLetter.getText().toString())){
                    game.guessLetter(inputLetter.getText().toString().toLowerCase());
                    if(!game.wasLastLetterCorrect()){
                        wrongLetters.append(" " + inputLetter.getText());
                        nextImage();
                    }
                }
                visibleWord.setText(game.getVisibleWord());
                inputLetter.setText("");
                getGameState();
                break;
        }
    }
}
