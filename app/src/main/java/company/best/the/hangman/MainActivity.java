package company.best.the.hangman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.InputStream;

import logic.HangmanLogic;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    static HangmanLogic game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InputStream wordStream = getApplicationContext().getResources().openRawResource(R.raw.words);
        game = new HangmanLogic(wordStream);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.playbutton:
                Intent playIntent = new Intent(this, gameActivity.class);
                playIntent.putExtra("gameObject", game);
                startActivity(playIntent);
                break;
            case R.id.settingsbutton:
                Intent settingsIntent = new Intent(this, settingsActivity.class);
                startActivity(settingsIntent);
                break;
        }
    }
}
