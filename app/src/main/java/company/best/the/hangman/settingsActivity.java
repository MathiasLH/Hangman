package company.best.the.hangman;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class settingsActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView wordLength, searchField;
    final int mode = Activity.MODE_PRIVATE;
    final String myPrefs = "MyPreferences_001";
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    ArrayList<String> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        InputStream wordStream = getApplicationContext().getResources().openRawResource(R.raw.words_small);
        words = readFile(wordStream);
        sp = getSharedPreferences(myPrefs, 0);
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        wordLength = findViewById(R.id.wordlength);
        searchField = findViewById(R.id.searchfield);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateList();
            }
        });
        wordLength.setText(Integer.toString(sp.getInt("maxWordLength", 0)));
        wordLength.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateWordLength();
            }
        });
        mAdapter = new myAdapter(words);
        updateList();
    }

    private void updateWordLength(){
        editor = sp.edit();
        if(!wordLength.getText().toString().matches("")){
            editor.putInt("maxWordLength", Integer.parseInt(wordLength.getText().toString()));
        }else{
            editor.putInt("maxWordLength", 0);
        }
        editor.commit();
        updateList();
    }

    private void updateList(){
        ArrayList<String> newWords = searchWords();
        RecyclerView.Adapter niceAdapter = new myAdapter(newWords);
        mRecyclerView.setAdapter(niceAdapter);
    }

    private ArrayList<String> readFile(InputStream inputFile){
        ArrayList<String> possibleWords = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputFile));
        for(int i = 0; i < 852; i++){
            try {
                possibleWords.add(br.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return possibleWords;
    }

    private ArrayList<String> searchWords() {
        return new ArrayList<>(Arrays.asList(words.stream().filter(x -> !searchField.getText().toString().matches("") ? x.contains(searchField.getText().toString()):true)
                                                            .filter(x-> x.length() <= sp.getInt("maxWordLength", 0)).toArray(String[]::new)));
    }
    @Override
    public void onClick(View v) {

    }
}
