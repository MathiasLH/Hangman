package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class HangmanLogic implements Serializable {
    ArrayList<String> possibleWords = new ArrayList<String>();
    private String wordToGuess;
    private ArrayList<String> usedLetters = new ArrayList<String>();
    private String visibleWord;
    private int wrongGuesses;
    private InputStream inputFile;
    private boolean lastLetterWasCorrect;
    private boolean gameIsWon;
    private boolean gameIsLost;
    private File wordList;

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
    }

    private String getNewWord(){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputFile));
        try {
            int nextWord = new Random(System.currentTimeMillis()).nextInt(41237);
            System.out.println(nextWord);
            for(int i = 0; i < nextWord; i++){
                br.readLine();
            }
            String line = br.readLine();
            System.out.println(line);
           // br.close();
            return line;
            //possibleWords.add(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
        System.out.println("The letter of choice was: " + letter);
        if (usedLetters.contains(letter)) return;
        if (gameIsWon || gameIsLost) return;

        usedLetters.add(letter);

        if (wordToGuess.contains(letter)) {
            lastLetterWasCorrect = true;
            System.out.println("The letter was correct: " + letter);
        } else {
            lastLetterWasCorrect = false;
            System.out.println("The letter was wrong: " + letter);
            wrongGuesses++;
            if (wrongGuesses > 5) {
                gameIsLost = true;
                visibleWord = wordToGuess;

            }
        }
        updateVisibleWord();
    }
}