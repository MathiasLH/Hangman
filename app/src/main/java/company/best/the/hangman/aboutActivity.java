package company.best.the.hangman;
//
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class aboutActivity extends AppCompatActivity {
    final String myPrefs = "MyPreferences_001";
    SharedPreferences sp;
    TextView wintext, losetext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        wintext = findViewById(R.id.wintext);
        losetext = findViewById(R.id.losetext);
        sp = getSharedPreferences(myPrefs, 0);
        updateWinLoss();
    }

    private void updateWinLoss(){
        int wins = sp.getInt("gameWins", 0);
        int losses = sp.getInt("gameLosses", 0);
        wintext.setText("Games won: " + Integer.toString(wins));
        losetext.setText("Games Lost: " + Integer.toString(losses));

    }
}
