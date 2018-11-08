package company.best.the.hangman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class settingsActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<String> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        InputStream wordStream = getApplicationContext().getResources().openRawResource(R.raw.words);
        TextView searchField = findViewById(R.id.searchfield);
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
        words = readFile(wordStream);
        mAdapter = new myAdapter(words);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void updateList(){
        TextView searchField = findViewById(R.id.searchfield);
        String searchKey = searchField.getText().toString();
        ArrayList<String> newWords = searchWords(words, searchKey);
        RecyclerView.Adapter niceAdapter = new myAdapter(newWords);
        mRecyclerView.setAdapter(niceAdapter);
    }

    private ArrayList<String> readFile(InputStream inputFile){
        ArrayList<String> possibleWords = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputFile));
        for(int i = 0; i < 41238; i++){
            try {
                possibleWords.add(br.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return possibleWords;
    }

    private ArrayList<String> searchWords(ArrayList<String> dataset, String searchKey){
        ArrayList<String> newWords = new ArrayList<String>();
        for(int i = 0; i < dataset.size(); i++){
            if(dataset.get(i).contains(searchKey)){
                newWords.add(dataset.get(i));
            }
        }
        return newWords;
    }

    @Override
    public void onClick(View v) {

    }
}
