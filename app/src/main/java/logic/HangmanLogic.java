//Rewrote galgeleglogik to english because danish code is bad practice.
package logic;

import android.app.Activity;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import company.best.the.hangman.R;

public class HangmanLogic implements Serializable {
    private ArrayList<String> possibleWords;
    private String wordToGuess;
    private ArrayList<String> usedLetters = new ArrayList<String>();
    private String visibleWord;
    private int wrongGuesses;
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

    public boolean wasLastLetterCorrect() {
        return lastLetterWasCorrect;
    }

    public boolean isGameIsLost() {
        return gameIsLost;
    }

    public boolean isGameIsWon() {
        return gameIsWon;
    }

    public HangmanLogic(ArrayList<String> words) {
        this.possibleWords = words;
    }
    public HangmanLogic(){
        try {
            //fetchWordsFromInternet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getNewWord(){
        int nextWord = new Random(System.currentTimeMillis()).nextInt(possibleWords.size());
        return possibleWords.get(nextWord);
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