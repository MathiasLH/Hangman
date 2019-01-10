package company.best.the.hangman;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class resultActivity extends AppCompatActivity implements View.OnClickListener{
    boolean gameWon;
    String word;
    int guesses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        gameWon = getIntent().getBooleanExtra("result", false);
        word = getIntent().getStringExtra("word");
        guesses = getIntent().getIntExtra("guesses", 0);
        TextView result = findViewById(R.id.resulttext);
        TextView info = findViewById(R.id.infotext);
        ImageButton retrybutton = findViewById(R.id.tryagainbutton);
        retrybutton.setOnClickListener(this);
        ImageView image = findViewById(R.id.image);
        MediaPlayer mp = MediaPlayer.create(this, R.raw.lose);
        KonfettiView kv = findViewById(R.id.konf);
        if(gameWon){
            result.setText("You win!");
            info.setText("And you only guessed " + guesses + " times!");
            image.setImageResource(R.drawable.ic_errors_0);
            kv.build()
                    .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                    .setDirection(0.0, 359.0)
                    .setSpeed(1f, 5f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(2000L)
                    .addShapes(Shape.RECT, Shape.CIRCLE)
                    .addSizes(new Size(12, 5))
                    .setPosition(-50f, kv.getWidth() + 50f, -50f, -50f)
                    .streamFor(300, 5000L);
        }else{
            result.setText("You lose!");
            info.setText("But the word was " + word + ". Better luck next time!");
            image.setImageResource(R.drawable.ic_errors_6);
            mp.start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tryagainbutton:
                finish();
                break;
        }
    }
}
