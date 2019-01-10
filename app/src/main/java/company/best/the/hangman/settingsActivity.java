package company.best.the.hangman;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class settingsActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter niceAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView wordLength, searchField;
    private RadioButton onlineWords, offlineWords;
    private RadioGroup wordListGroup;
    final int mode = Activity.MODE_PRIVATE;
    final String myPrefs = "MyPreferences_001";
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    ArrayList<String> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sp = getSharedPreferences(myPrefs, 0);
        words = readFile();
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        wordLength = findViewById(R.id.wordlength);
        wordListGroup = findViewById(R.id.wordlistGroup);
        offlineWords = wordListGroup.findViewById(R.id.offlineRadio);
        offlineWords.setChecked(sp.getBoolean("useLocalWords", true));
        onlineWords = wordListGroup.findViewById(R.id.onlineRadio);
        onlineWords.setChecked(!sp.getBoolean("useLocalWords", false));
        wordListGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                editor = sp.edit();
                if(offlineWords.isChecked()){
                    editor.putBoolean("useLocalWords", true);
                    editor.commit();
                    createLocalWordList(readFile(),"internalWords");
                    updateList();
                }else{
                    editor.putBoolean("useLocalWords", false);
                    editor.commit();
                    words.clear();
                    words.add("Loading...");
                    updateList();
                    try {
                        new fetchWordsTask().execute(new URL("https://www.google.com"));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
            FileOutputStream fOut = openFileOutput(sp.getBoolean("useLocalWords", true) ? "internalWords":"internetWords", Context.MODE_APPEND);
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

            br = new BufferedReader(new InputStreamReader(openFileInput(sp.getBoolean("useLocalWords", true) ? "internalWords":"internetWords")));
            //br = new BufferedReader(new InputStreamReader(openFileInput("internalWords")));
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

    private void createLocalWordList(ArrayList<String> words, String name){
        {
            try {
                FileOutputStream fOut = openFileOutput(name, Context.MODE_PRIVATE);
                for(String s: words){
                    String str = s + "\n";
                    fOut.write(str.getBytes());
                }
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.words = words;
        }
    }

    public static String fetchURL(String URL) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL(URL).openStream()));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while(line != null){
            sb.append(line + "\n");
            line = br.readLine();
        }
        return sb.toString();
    }


    //A hangman application has no need for internet communication in my opinion,
    //however to satisfy requirements, here is the function for fetching words from a website.
    //also please dont just throw "Exception".
    public ArrayList<String> fetchWordsFromInternet() throws Exception{
        String data = fetchURL("https://en.wikipedia.org/wiki/Main_Page");
        data = data.substring(data.indexOf("<body")).
                replaceAll("<.+?>", " ").toLowerCase().
                replaceAll("&#198;", "æ").
                replaceAll("&#230;", "æ").
                replaceAll("&#216;", "ø").
                replaceAll("&#248;", "ø").
                replaceAll("&oslash;", "ø").
                replaceAll("&#229;", "å").
                replaceAll("[^a-zæøå]", " ").
                replaceAll(" [a-zæøå] "," ").
                replaceAll(" [a-zæøå][a-zæøå] "," ");
        words.clear();
        words.addAll(new HashSet<String>(Arrays.asList(data.split(" "))));
        Collections.sort(words);
        return words;
    }

    private void deleteLine(String word){
        getBaseContext().deleteFile(sp.getBoolean("useLocalWords", true) ? "internalWords":"internetWords");
        try {
            FileOutputStream fOut = openFileOutput(sp.getBoolean("useLocalWords", true) ? "internalWords":"internetWords", Context.MODE_PRIVATE);
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

    private class fetchWordsTask extends AsyncTask<URL, Integer, Long>{

        @Override
        protected Long doInBackground(URL... urls) {

            try {
                createLocalWordList(fetchWordsFromInternet(), "internetWords");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            updateList();
        }
    }

}
