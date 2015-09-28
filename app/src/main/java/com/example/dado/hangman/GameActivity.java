package com.example.dado.hangman;

import android.app.Activity;
import android.os.Bundle;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


import java.util.Random;


public class GameActivity extends Activity {

    private String[] words;
    private Random rand;
    private String currWord;
    private LinearLayout wordLayout;
    private TextView[] charViews;
    private GridView letters;
    private LetterAdapter ltrAdapt;
    //body part images
    private ImageView[] bodyParts;
    //number of body parts
    private int numParts=6;
    //current part - will increment when wrong answers are chosen
    private int currPart;
    //number of characters in current word
    private int numChars = 0;
    //number correctly guessed
    private int numCorr = 0;

    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Resources res = getResources();
        words = res.getStringArray(R.array.words);
        rand = new Random();
        currWord = "";

        wordLayout = (LinearLayout)findViewById(R.id.word);
        letters = (GridView)findViewById(R.id.letters);

        bodyParts = new ImageView[numParts];
        bodyParts[0] = (ImageView)findViewById(R.id.head);
        bodyParts[1] = (ImageView)findViewById(R.id.body);
        bodyParts[2] = (ImageView)findViewById(R.id.arm1);
        bodyParts[3] = (ImageView)findViewById(R.id.arm2);
        bodyParts[4] = (ImageView)findViewById(R.id.leg1);
        bodyParts[5] = (ImageView)findViewById(R.id.leg2);

        playGame();
    }

    private void playGame() {
        currPart=0;
        numChars=currWord.length();
        numCorr=0;
        for(int p = 0; p < numParts; p++) {
            bodyParts[p].setVisibility(View.INVISIBLE);
        }
        ltrAdapt=new LetterAdapter(this);
        letters.setAdapter(ltrAdapt);
        String newWord = words[rand.nextInt(words.length)];
        while(newWord.equals(currWord)) newWord = words[rand.nextInt(words.length)];
        currWord = newWord;
        charViews = new TextView[currWord.length()];
        wordLayout.removeAllViews();


       /* Use a simple for loop to iterate over each letter of the answer,
       create a text view for each letter,
       and set the text view's text to the current letter.*/
        for (int c = 0; c < currWord.length(); c++) {
            charViews[c] = new TextView(this);
            charViews[c].setText(""+currWord.charAt(c));

            charViews[c].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            charViews[c].setGravity(Gravity.CENTER);
            charViews[c].setTextColor(Color.WHITE);
            charViews[c].setBackgroundResource(R.drawable.letter_bg);
            //add to layout
            wordLayout.addView(charViews[c]);
        }
    }

    public void letterPressed(View view) {
        String ltr=((TextView)view).getText().toString();
        Log.i("Game", "Bokstav: " + ltr);
        char letterChar = ltr.charAt(0);
        view.setEnabled(false);
        view.setBackgroundResource(R.drawable.letter_down);

        boolean correct = false;

        for(int k = 0; k < currWord.length(); k++) {
            Log.i("Game", "currWord: " + currWord.length());
            if(currWord.charAt(k)==letterChar){
                correct = true;
                numCorr++;
                charViews[k].setTextColor(Color.BLACK);
            }
        }
        Log.i("Game", "Lenght: " + currWord.length() + ", Number correct: " + numCorr);

        if (correct) {
            if (numCorr == currWord.length()) {
                disableBtns();

                // Display Alert Dialog
                AlertDialog.Builder winBuild = new AlertDialog.Builder(this);
                winBuild.setTitle("YAAAYYY!!!");
                winBuild.setMessage("You win!\n\nThe answer was:\n\n" + currWord);
                winBuild.setPositiveButton("Play Again",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GameActivity.this.playGame();
                            }
                        });

                winBuild.setNegativeButton("Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GameActivity.this.finish();
                            }
                        });

                winBuild.show();
            }
            else {
                //do nothing
            }
        }
           else if (currPart < numParts && correct == false) {
                //some guesses left
                bodyParts[currPart].setVisibility(View.VISIBLE);
                currPart++;
            }
            else{
                //user has lost
                disableBtns();

                // Display Alert Dialog
                AlertDialog.Builder loseBuild = new AlertDialog.Builder(this);
                loseBuild.setTitle("NOO!!!");
                loseBuild.setMessage("You lose!\n\nThe answer was:\n\n"+currWord);
                loseBuild.setPositiveButton("Play Again",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GameActivity.this.playGame();
                            }});

                loseBuild.setNegativeButton("Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GameActivity.this.finish();
                            }});

                loseBuild.show();
            }
        }


    public void disableBtns() {
        int numLetters = letters.getChildCount();
        for (int l = 0; l < numLetters; l++) {
            letters.getChildAt(l).setEnabled(false);
        }
    }


}
