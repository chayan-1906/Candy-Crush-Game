package com.example.candycrushgame;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private TextView textViewScore;
    private TextView textViewScoreResult;
    private final int[] candies = {
            R.drawable.bluecandy,
            R.drawable.greencandy,
            R.drawable.orangecandy,
            R.drawable.purplecandy,
            R.drawable.redcandy,
            R.drawable.yellowcandy,
    };
    private int widthOfBlock;
    private int noOfBlocks;
    private int widthOfScreen;
    private int heightOfScreen;
    private DisplayMetrics displayMetrics;  //To check the height and width of the screen...
    private final ArrayList<ImageView> candy = new ArrayList<> ();

    private int candyToBeDragged;
    private int candyToBeReplaced;
    private final int notCandy = R.drawable.transparent;
    private Handler handler;
    private int interval = 100;
    private int score;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        gridLayout = findViewById ( R.id.gridLayoutBoard );
        textViewScore = findViewById ( R.id.textViewScore );
        textViewScoreResult = findViewById ( R.id.textViewScoreResult );

        noOfBlocks = 8; //In a single row, there are 8 candies...
        displayMetrics = new DisplayMetrics ();
        getWindowManager ().getDefaultDisplay ().getMetrics ( displayMetrics );
        widthOfScreen = displayMetrics.widthPixels;
        heightOfScreen = displayMetrics.heightPixels;
        widthOfBlock = widthOfScreen / noOfBlocks;
        createBoard();
        for (final ImageView imageView : candy) {
            imageView.setOnTouchListener ( new OnSwipeListener ( this ) {
                @Override
                void onSwipeLeft() {
                    super.onSwipeLeft ( );
                    //Toast.makeText ( MainActivity.this, "Left", Toast.LENGTH_SHORT ).show ( );
                    candyToBeDragged = imageView.getId ();
                    candyToBeReplaced = candyToBeDragged - 1;
                    candyInterchange ();
                }

                @Override
                void onSwipeRight() {
                    super.onSwipeRight ( );
                    //Toast.makeText ( MainActivity.this, "Right", Toast.LENGTH_SHORT ).show ( );
                    candyToBeDragged = imageView.getId ();
                    candyToBeReplaced = candyToBeDragged + 1;
                }

                @Override
                void onSwipeUp() {
                    super.onSwipeUp ( );
                    //Toast.makeText ( MainActivity.this, "Up", Toast.LENGTH_SHORT ).show ( );
                    candyToBeDragged = imageView.getId ();
                    candyToBeReplaced = candyToBeDragged - noOfBlocks;
                    candyInterchange ();
                }

                @Override
                void onSwipeDown() {
                    super.onSwipeDown ( );
                    //Toast.makeText ( MainActivity.this, "Down", Toast.LENGTH_SHORT ).show ( );
                    candyToBeDragged = imageView.getId ();
                    candyToBeReplaced = candyToBeDragged + noOfBlocks;
                    candyInterchange ();
                }
            } );
        }
        handler = new Handler (  );
        startRepeat ();
    }

    private void createBoard() {

        gridLayout.setRowCount ( noOfBlocks );
        gridLayout.setColumnCount ( noOfBlocks );
        //Since we want a square...
        gridLayout.getLayoutParams ().width = widthOfScreen;
        gridLayout.getLayoutParams ().height = widthOfScreen;

        for (int i=0; i<noOfBlocks*noOfBlocks; i++) {
            ImageView imageView = new ImageView ( getApplicationContext () );
            imageView.setId ( i );
            imageView.setLayoutParams ( new android.view.ViewGroup.LayoutParams ( widthOfBlock, widthOfBlock ) );
            imageView.setMaxWidth ( widthOfBlock );
            imageView.setMaxHeight ( widthOfBlock );
            int randomCandy = (int) Math.floor ( Math.random () * candies.length ); //It generates random index between 0 and candies.length-1 i.e. 5...
            imageView.setImageResource ( candies[randomCandy] );
            imageView.setTag ( candies[randomCandy] );
            candy.add ( imageView );
            gridLayout.addView ( imageView );
        }

    }

    private void candyInterchange() {

        int background = (int) candy.get ( candyToBeReplaced ).getTag ();
        int background1 = (int) candy.get ( candyToBeDragged ).getTag ();
        candy.get ( candyToBeDragged ).setImageResource ( background );
        candy.get ( candyToBeReplaced ).setImageResource ( background1 );
        candy.get ( candyToBeDragged ).setTag ( background );
        candy.get ( candyToBeReplaced ).setTag ( background1 );
    }

    private void checkRowForThree() {

        for (int i=0; i<62; i++) {
            int chosenCandy = (int) candy.get ( i ).getTag ();
            boolean isBlank = (int) candy.get ( i ).getTag () == notCandy;
            Integer[] notValid = {6, 7, 14, 15, 22, 23, 30, 31, 38, 39, 46, 47, 54, 55};    //There is no index that will match the rows after index 6 & 7 and so on...
            List<Integer> list = Arrays.asList ( notValid );
            if (!list.contains ( i )) {
                int x = i;
                if ((int) candy.get ( x++ ).getTag () == chosenCandy && !isBlank &&
                        (int) candy.get ( x++ ).getTag () == chosenCandy &&
                        (int) candy.get ( x ).getTag () == chosenCandy) {
                    score = score + 3;
                    textViewScoreResult.setText ( String.valueOf ( score ) );
                    candy.get ( x ).setImageResource ( notCandy );
                    candy.get ( x ).setTag ( notCandy );
                    x--;
                    candy.get ( x ).setImageResource ( notCandy );
                    candy.get ( x ).setTag ( notCandy );
                    x--;
                    candy.get ( x ).setImageResource ( notCandy );
                    candy.get ( x ).setTag ( notCandy );
                }
            }
        }
        moveDownCandies ();
    }

    private void checkColumnForThree() {

        for (int i=0; i<46; i++) {
            int chosenCandy = (int) candy.get ( i ).getTag ();
            boolean isBlank = (int) candy.get ( i ).getTag () == notCandy;
                int x = i;
                if ((int) candy.get ( x ).getTag () == chosenCandy && !isBlank &&
                        (int) candy.get ( x+noOfBlocks ).getTag () == chosenCandy &&
                        (int) candy.get ( x + 2*noOfBlocks ).getTag () == chosenCandy) {
                    score = score + 3;
                    textViewScoreResult.setText ( String.valueOf ( score ) );
                    candy.get ( x ).setImageResource ( notCandy );
                    candy.get ( x ).setTag ( notCandy );
                    x += noOfBlocks;
                    candy.get ( x ).setImageResource ( notCandy );
                    candy.get ( x ).setTag ( notCandy );
                    x += noOfBlocks;
                    candy.get ( x ).setImageResource ( notCandy );
                    candy.get ( x ).setTag ( notCandy );
                }
        }
        moveDownCandies ();
    }

    private void moveDownCandies() {
        Integer[] firstRow = {0, 1, 2, 3, 4, 5, 6, 7};
        List<Integer> list = Arrays.asList ( firstRow );
        for (int i=55; i>=0; i--) {
            if ((int) candy.get ( i + noOfBlocks ).getTag () == notCandy) {
                candy.get ( i + noOfBlocks ).setImageResource ( (int) candy.get ( i ).getTag () );
                candy.get ( i + noOfBlocks ).setTag ( candy.get ( i ).getTag () );
                candy.get ( i ).setImageResource ( notCandy );
                candy.get ( i ).setTag ( notCandy );

                if (list.contains ( i ) && (int) candy.get ( i ).getTag () == notCandy) {
                    int randomColor = (int) Math.floor ( Math.random () * candies.length );
                    candy.get ( i ).setImageResource ( candies[randomColor] );
                    candy.get ( i ).setTag ( candies[randomColor] );
                }
            }
        }
        for (int i=0; i<8; i++) {
            if ((int) candy.get ( i ).getTag () == notCandy) {
                int randomColor = (int) Math.floor ( Math.random () * candies.length );
                candy.get ( i ).setImageResource ( candies[randomColor] );
                candy.get ( i ).setTag ( candies[randomColor] );
            }
        }
    }

    Runnable repeatChecker = new Runnable ( ) {
        @Override
        public void run() {
            try {
                checkRowForThree ();
                checkColumnForThree ();
                moveDownCandies ();
            } finally {
                handler.postDelayed ( repeatChecker, interval );
            }
        }
    };

    private void startRepeat() {
        repeatChecker.run ();
    }

}