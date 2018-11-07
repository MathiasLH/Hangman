package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import company.best.the.hangman.R;

public class HangmanLogic implements Serializable {
    private String[] possibleWords = new String[41238];
    private String wordToGuess;
    private ArrayList<String> usedLetters = new ArrayList<String>();
    private String visibleWord;
    private int wrongGuesses;
    private InputStream inputFile;
    private boolean lastLetterWasCorrect;
    private boolean gameIsWon;
    private boolean gameIsLost;


    public ArrayList<String> getUsedLetters() {
        return usedLetters;
    }

    public String getVisibleWord() {
        return visibleWord;
    }

    public String getWordToGuess() {
        return wordToGuess;
    }

    public int getWrongGuesses() {
        return wrongGuesses;
    }

    public boolean wasLastLetterCorrect() {
        return lastLetterWasCorrect;
    }

    public boolean isGameIsLost() {
        return gameIsLost;
    }

    public boolean isGameIsWon() {
        return gameIsWon;
    }

    public HangmanLogic(InputStream inputFile) {

        this.inputFile = inputFile;
        readFile();
    }

    private void readFile(){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputFile));
        for(int i = 0; i < 41238; i++){
            try {
                possibleWords[i] = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getNewWord(){
        int nextWord = new Random(System.currentTimeMillis()).nextInt(41237);
        return possibleWords[nextWord];
    }
    public void reset() {
        usedLetters.clear();
        wrongGuesses = 0;
        gameIsLost = false;
        gameIsWon = false;
        wordToGuess = getNewWord();
        updateVisibleWord();
    }


    private void updateVisibleWord() {
        visibleWord = "";
        gameIsWon = true;
        System.out.println("wordtoguess is: " + wordToGuess);
        for (int i = 0; i < wordToGuess.length(); i++) {
            String letter = wordToGuess.substring(i, i + 1);
            if (usedLetters.contains(letter)) {
                visibleWord = visibleWord + letter;
            } else {
                visibleWord = visibleWord + "*";
                gameIsWon = false;
            }
        }
    }

    public void guessLetter(String letter) {
        if (letter.length() != 1) return;
        if (usedLetters.contains(letter)) return;
        if (gameIsWon || gameIsLost) return;
        usedLetters.add(letter);
        if (wordToGuess.contains(letter)) {
            lastLetterWasCorrect = true;
        } else {
            lastLetterWasCorrect = false;
            wrongGuesses++;
            if (wrongGuesses > 5) {
                gameIsLost = true;
                visibleWord = wordToGuess;

            }
        }
        updateVisibleWord();
    }
}