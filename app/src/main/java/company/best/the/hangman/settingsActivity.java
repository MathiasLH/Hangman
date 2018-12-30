package company.best.the.hangman;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArraySet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class settingsActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter niceAdapter;
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
        words = readFile();
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
        if(!newWords.isEmpty()){
            niceAdapter = new myAdapter(newWords);
            ((myAdapter) niceAdapter).setOnItemClickListener(new myAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    createDialogue(position);
                }
            });
            mRecyclerView.setAdapter(niceAdapter);
        }else{
            ArrayList<String> emptyArray = new ArrayList<>();
            emptyArray.add("Add word");
            niceAdapter = new myAdapter(emptyArray);
            ((myAdapter) niceAdapter).setOnItemClickListener(new myAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    addWordToList();
                }
            });
            mRecyclerView.setAdapter(niceAdapter);
        }
    }

    private void createDialogue(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ArrayList<String> newWords = searchWords();
        builder.setTitle(newWords.get(position));
        builder.setMessage("Delete word?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteLine(newWords.get(position));
                ((myAdapter) niceAdapter).removeAt(position);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void addWordToList(){
        try {
            FileOutputStream fOut = openFileOutput("internalWords", Context.MODE_APPEND);
            String s = searchField.getText().toString() + "\n";
            fOut.write(s.getBytes());
            fOut.close();
            words = readFile();
            updateList();
        } catch (IOException e) {
            e.printStackTrace();
        }
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



    private void deleteLine(String word){
        getBaseContext().deleteFile("internalWords");
        try {
            FileOutputStream fOut = openFileOutput("internalWords", Context.MODE_PRIVATE);
            for(String s: words){
                if(!s.equals(word)){
                    String str = s + "\n";
                    fOut.write(str.getBytes());
                }
            }
            fOut.close();
            words = readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> searchWords() {
        return new ArrayList<>(Arrays.asList(words.stream().filter(x -> !searchField.getText().toString().matches("") ? x.contains(searchField.getText().toString()):true)
                                                            .filter(x-> x.length() <= sp.getInt("maxWordLength", 0)).toArray(String[]::new)));
    }
    @Override
    public void onClick(View v) {

    }
}
