package com.example.asus.myapplication;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

import static com.example.asus.myapplication.CalculatorThread.BASE_URL;

public class GameActivity extends Activity implements CalculatorThread.CalculatorThreadListener, YesNoFragment.YesNoFragmentListener {


    public static int numOfBombs;
    public static int SizeOfBoard;//for the calculations imagine size is 8X8
    final public boolean[] arrayOfChangedButtons = new boolean[SizeOfBoard * SizeOfBoard+1];

    //Positions relative to pressed buttons
    final private int North = -SizeOfBoard;
    final private int East = 1;
    final private int West=-1;
    final private int South=SizeOfBoard;
    final private int NorthWest=-SizeOfBoard-1;
    final private int NorthEast=-SizeOfBoard+1;
    final private int SouthWest=SizeOfBoard-1;
    final private int SouthEast=SizeOfBoard+1;

    public  Stopwatch s= new Stopwatch();
    public static long timeElapsed;
    public boolean[] booleanBombArray = new boolean[SizeOfBoard*SizeOfBoard+1];//boolean true if there's a bomb, false if not, the array is all false at the start
    public static boolean onFirstClickDoOnce;
    public int numberOfCellsThatAreNotBombs=(SizeOfBoard*SizeOfBoard)-numOfBombs;
    public ImageButton imageButton;
    public static boolean viewMode;
    private LinearLayout verticalLayout;
    private int IntentStopper=0;
    private int FlagsPlaced=0;
    
    int[] corners = new int[]{1, SizeOfBoard, SizeOfBoard * (SizeOfBoard - 1) + 1, SizeOfBoard * SizeOfBoard};//1 , SizeOfBoard , 57 , 64
    int[] sides = new int[(SizeOfBoard - 2) * 4];
    int[] inside = new int[SizeOfBoard * SizeOfBoard - 4 - (SizeOfBoard - 2) * 4];

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        onFirstClickDoOnce=true;
        viewMode=false;
        int id = 1;

        textView=findViewById(R.id.FlagsPlaced);
        TextView tempTextView=findViewById(R.id.BombsChosen);
        final String text = "Bombs Chosen: " + numOfBombs;
        tempTextView.setText(text);
        final String tempText = "Flags Placed: " + FlagsPlaced;
        textView.setText(tempText);

        final RadioButton radioFlag = findViewById(R.id.flag);
        final Button btnForNormalBackground=new Button(this);

        verticalLayout = findViewById(R.id.verticalLayout);
        for (int i = 0; i < SizeOfBoard; i++) {
            LinearLayout horizontalLayout = new LinearLayout(this);
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            verticalLayout.addView(horizontalLayout, layoutParams);
            for (int j = 0; j < SizeOfBoard; j++) {
                final ImageButton imageButton = new ImageButton(this);
                LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                horizontalLayout.addView(imageButton, btnParams);
                imageButton.setId(id++);
                imageButton.setTag(2);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!viewMode) {
                            if (onFirstClickDoOnce&&!radioFlag.isChecked()) {
                                onFirstClickDoOnce();
                                int id = imageButton.getId();
                                generateBombs(id);
                                String action = "add";
                                new CalculatorTask().execute(new CalculatorParams(1, 2, action));
                            }
                            onButtonClick(imageButton);
                        }
                    }
                });
                imageButton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if(!viewMode ) {
                            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                vibrator.vibrate(50);
                            }
                            if (Integer.parseInt(String.valueOf(imageButton.getTag())) != 2) {
                                Drawable drawable = btnForNormalBackground.getBackground();
                                imageButton.setBackground(drawable);
                                imageButton.setTag(2);
                                textView.setText(tempText);
                            } else {
                                imageButton.setBackgroundResource(R.drawable.tinyflag);
                                imageButton.setTag(1);
                                textView.setText(tempText);
                            }
                        }
                        return true;
                    }
                });
            }
        }
        generateArrays();
        Arrays.fill(arrayOfChangedButtons, Boolean.FALSE); //1-64 all false at start (no change in ui)
    }
    public void onFirstClickDoOnce(){
        if(onFirstClickDoOnce) {
            s.start();
            onFirstClickDoOnce = false;
        }
        else{
            timeElapsed=s.getElapsedTimeSecs();
            s.stop();
        }
    }
    public void generateBombs(int idToSkip) {

        Arrays.fill(booleanBombArray, Boolean.FALSE);

        Random r = new Random();
        for (int i = 0; i < numOfBombs; i++) {
            int num = r.nextInt(SizeOfBoard * SizeOfBoard+1 -1) + 1;
            if (booleanBombArray[num]||idToSkip==num) {
                i--;
            } else {
                booleanBombArray[num]=true;
            }
        }


    }
    private boolean containsIntInArray(int[] arr, int num) {
        for (int anArr : arr) {
            if (anArr == num) {
                return true;
            }
        }
        return false;
    }
    public void generateArrays() {
        //corners already listed above
        //SizeOfBoard=8
        for (int i = 0; i < SizeOfBoard - 2; i++) {
            sides[i] = 2 + i;//2 , 3 , 4 , 5 , 6 , 7
        }
        for (int i = 0; i < SizeOfBoard - 2; i++) {
            sides[SizeOfBoard - 2 + i] = i * SizeOfBoard + (SizeOfBoard + 1);//9 , 17 , 25 , 33 , 41 , 49
        }
        for (int i = 0; i < SizeOfBoard - 2; i++) {
            sides[SizeOfBoard * 2 - 2 * 2 + i] = 2 * SizeOfBoard + i * SizeOfBoard;//16 , 24 , 32 , 40 ,48 , 56
        }
        for (int i = 0; i < SizeOfBoard - 2; i++) {
            sides[SizeOfBoard * 3 - 2 * 3 + i] = SizeOfBoard * (SizeOfBoard - 1) + 2 + i;//51 , 59 , 60 , 61 , 62 , 63
        }
        int index = 0;
        for (int i = 0; i < SizeOfBoard - 2; i++) {
            for (int j = 0; j < SizeOfBoard - 2; j++) {
                inside[index] = 1 + SizeOfBoard + 1 + j + (SizeOfBoard * i);
                index++;
            }
        }
    }
    public void onButtonClick(View v) {

        imageButton = findViewById(v.getId());

        RadioButton radioFlag = findViewById(R.id.flag);
        RadioButton radioSearch = findViewById(R.id.search);

        if (radioFlag.isChecked()) {
            if (Integer.parseInt(String.valueOf(imageButton.getTag())) != 2) {
                Button btnForNormalBackground=new Button(this);
                Drawable drawable = btnForNormalBackground.getBackground();
                imageButton.setBackground(drawable);
                imageButton.setTag(2);
                String tempText = "Flags Placed: " + --FlagsPlaced;
                textView.setText(tempText);

            } else {
                imageButton.setBackgroundResource(R.drawable.tinyflag);
                imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);

                int tag = 1;
                imageButton.setTag(tag);
                String tempText = "Flags Placed: " + ++FlagsPlaced;
                textView.setText(tempText);
            }
        }
        if (radioSearch.isChecked()) {
            if (Integer.parseInt(String.valueOf(imageButton.getTag())) != 2) {
                Toast.makeText(this, "Remove Flag First", Toast.LENGTH_SHORT).show();
            } else {
                checkForBomb(imageButton.getId());
            }
        }
    }
    private void checkForBomb(int imageButtonIdNum) {//1-64

        imageButton=findViewById(imageButtonIdNum);

        if (!arrayOfChangedButtons[imageButtonIdNum]) {
            arrayOfChangedButtons[imageButtonIdNum] = true;
            numberOfCellsThatAreNotBombs--;
            if (booleanBombArray[ imageButtonIdNum ]) {
                IntentStopper=1;
                Toast.makeText(this, "GameOver", Toast.LENGTH_LONG).show();
                showAllBombs(imageButtonIdNum);//on lose show all bombs and send id of clicked bomb
                btnShowYesNoDialog(imageButton);
            } else {
                int numOfAreaBombs = 0;
                boolean isCorner = false, isSide = false, isInside = false;

                if (containsIntInArray(corners, imageButtonIdNum)) {
                    if (imageButtonIdNum == 1) {
                        if (booleanBombArray[ imageButtonIdNum + East]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + South]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + SouthEast]) numOfAreaBombs++;
                    } else if (imageButtonIdNum == SizeOfBoard) {
                        if (booleanBombArray[ imageButtonIdNum + West]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + South]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + SouthWest]) numOfAreaBombs++;
                    } else if (imageButtonIdNum == SizeOfBoard * (SizeOfBoard - 1) + 1) {
                        if (booleanBombArray[ imageButtonIdNum + North]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + East]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + NorthEast]) numOfAreaBombs++;
                    } else {//SizeOfBoard*SizeOfBoard=imageButtonIdNum
                        if (booleanBombArray[ imageButtonIdNum + North]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + NorthWest]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + West]) numOfAreaBombs++;
                    }
                    isCorner = true;
                } else if (containsIntInArray(sides, imageButtonIdNum)) {//to check which of the 4 sides chosen
                    int i = 0;
                    for (; i < (SizeOfBoard - 2) * 4; i++) {
                        if (sides[i] == imageButtonIdNum) {
                            break;
                        }
                    }
                    //SizeOfBoard=8
                    if (i < SizeOfBoard - 2) {//i<6
                        if (booleanBombArray[ imageButtonIdNum + East]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + West]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + SouthWest]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + South]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + SouthEast]) numOfAreaBombs++;
                    } else if (i < SizeOfBoard - 2 + SizeOfBoard - 2) {//6<12
                        if (booleanBombArray[ imageButtonIdNum + North]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + NorthEast]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + East]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + South]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + SouthEast]) numOfAreaBombs++;
                    } else if (i < SizeOfBoard * 2 - 2 * 2 + SizeOfBoard - 2) {//12<18
                        if (booleanBombArray[ imageButtonIdNum + North]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + NorthWest]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + West]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + South]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + SouthWest]) numOfAreaBombs++;
                    } else { // if(i<SizeOfBoard*3-2*3+SizeOfBoard-2){    //18<24
                        if (booleanBombArray[ imageButtonIdNum + North]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + NorthEast]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + East]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + West]) numOfAreaBombs++;
                        if (booleanBombArray[ imageButtonIdNum + NorthWest]) numOfAreaBombs++;
                    }
                    isSide = true;
                } else {   //(containsIntInArray(insides,imageButtonIdNum))   true
                    if (booleanBombArray[ imageButtonIdNum + West]) numOfAreaBombs++;
                    if (booleanBombArray[ imageButtonIdNum + East]) numOfAreaBombs++;
                    //sides
                    if (booleanBombArray[ imageButtonIdNum + South]) numOfAreaBombs++;
                    if (booleanBombArray[ imageButtonIdNum + SouthWest]) numOfAreaBombs++;
                    if (booleanBombArray[ imageButtonIdNum + SouthEast]) numOfAreaBombs++;
                    //bellow
                    if (booleanBombArray[ imageButtonIdNum + North]) numOfAreaBombs++;
                    if (booleanBombArray[ imageButtonIdNum + NorthWest]) numOfAreaBombs++;
                    if (booleanBombArray[ imageButtonIdNum + NorthEast]) numOfAreaBombs++;
                    //above
                    //isInside = true;
                }
                if(Integer.parseInt(String.valueOf(imageButton.getTag())) != 2){
                    imageButton.setImageResource(android.R.color.transparent);
                    String tempText = "Flags Placed: " + --FlagsPlaced;
                    textView.setText(tempText);
                    //remove flag
                }
                if (numOfAreaBombs != 0) {
                    //imageButton.setBackground();

                    if (numOfAreaBombs == 1) { imageButton.setBackgroundResource(R.drawable.number1);
                    } else if (numOfAreaBombs == 2) { imageButton.setBackgroundResource(R.drawable.number2);
                    } else if (numOfAreaBombs == 3) { imageButton.setBackgroundResource(R.drawable.number3);
                    } else if (numOfAreaBombs == 4) { imageButton.setBackgroundResource(R.drawable.number4);
                    } else if (numOfAreaBombs == 5) { imageButton.setBackgroundResource(R.drawable.number5);
                    } else if (numOfAreaBombs == 6) { imageButton.setBackgroundResource(R.drawable.number6);
                    } else if (numOfAreaBombs == 7) { imageButton.setBackgroundResource(R.drawable.number7);
                    } else if (numOfAreaBombs == 8) { imageButton.setBackgroundResource(R.drawable.number8);
                    }
                    imageButton.setEnabled(false);
                } else {//numOfAreaBombs == 0

                    imageButton.setBackgroundColor(Color.TRANSPARENT);
                    imageButton.setEnabled(false);
                    //show other cells
                    if (isCorner) {
                        if (imageButtonIdNum == 1) {
                            checkForBomb(imageButtonIdNum + 1);
                            checkForBomb(imageButtonIdNum + SizeOfBoard);
                            checkForBomb(imageButtonIdNum + SizeOfBoard + 1);
                        } else if (imageButtonIdNum == SizeOfBoard) {
                            checkForBomb(imageButtonIdNum - 1);
                            checkForBomb(imageButtonIdNum + SizeOfBoard - 1);
                            checkForBomb(imageButtonIdNum + SizeOfBoard);
                        } else if (imageButtonIdNum == SizeOfBoard * (SizeOfBoard - 1) + 1) {
                            checkForBomb(imageButtonIdNum - SizeOfBoard);
                            checkForBomb(imageButtonIdNum - SizeOfBoard + 1);
                            checkForBomb(imageButtonIdNum + 1);
                        } else {
                            checkForBomb(imageButtonIdNum - SizeOfBoard);
                            checkForBomb(imageButtonIdNum - SizeOfBoard - 1);
                            checkForBomb(imageButtonIdNum - 1);
                        }
                    } else if (isSide) {
                        int i = 0;
                        for (; i < (SizeOfBoard - 2) * 4; i++) {
                            if (sides[i] == imageButtonIdNum) {
                                break;
                            }
                        }
                        if (i < SizeOfBoard - 2) {
                            checkForBomb(imageButtonIdNum - 1);
                            checkForBomb(imageButtonIdNum + 1);
                            checkForBomb(imageButtonIdNum + SizeOfBoard);
                            checkForBomb(imageButtonIdNum + SizeOfBoard - 1);
                            checkForBomb(imageButtonIdNum + SizeOfBoard + 1);
                        } else if (i < SizeOfBoard  - 2  + SizeOfBoard - 2) {
                            checkForBomb(imageButtonIdNum - SizeOfBoard);
                            checkForBomb(imageButtonIdNum - SizeOfBoard + 1);
                            checkForBomb(imageButtonIdNum + 1);
                            checkForBomb(imageButtonIdNum + SizeOfBoard);
                            checkForBomb(imageButtonIdNum + SizeOfBoard + 1);
                        } else if (i < SizeOfBoard * 2 - 2 * 2 + SizeOfBoard - 2) {
                            checkForBomb(imageButtonIdNum - SizeOfBoard);
                            checkForBomb(imageButtonIdNum - SizeOfBoard - 1);
                            checkForBomb(imageButtonIdNum - 1);
                            checkForBomb(imageButtonIdNum + SizeOfBoard);
                            checkForBomb(imageButtonIdNum + SizeOfBoard - 1);
                        } else {// if(i<SizeOfBoard*3-2*3+SizeOfBoard-2)
                            checkForBomb(imageButtonIdNum - 1);
                            checkForBomb(imageButtonIdNum - SizeOfBoard - 1);
                            checkForBomb(imageButtonIdNum - SizeOfBoard);
                            checkForBomb(imageButtonIdNum - SizeOfBoard + 1);
                            checkForBomb(imageButtonIdNum + 1);
                        }
                    } else {//isInside
                        checkForBomb(imageButtonIdNum + 1);
                        checkForBomb(imageButtonIdNum - 1);
                        checkForBomb(imageButtonIdNum + SizeOfBoard);
                        checkForBomb(imageButtonIdNum + SizeOfBoard + 1);
                        checkForBomb(imageButtonIdNum + SizeOfBoard - 1);
                        checkForBomb(imageButtonIdNum - SizeOfBoard);
                        checkForBomb(imageButtonIdNum - SizeOfBoard - 1);
                        checkForBomb(imageButtonIdNum - SizeOfBoard + 1);
                    }
                }
            }
        }
        if (numberOfCellsThatAreNotBombs==0&&IntentStopper!=1){
            IntentStopper=1;
            onFirstClickDoOnce();//stop timer
            goToWonGameActivity();//won the game
        }
    }
    public void goToWonGameActivity() {
        if(IntentStopper==1) {//stops the creation of multiple activities
            viewMode = true;
            Intent intent = new Intent(this, WonGameActivity.class);
            startActivity(intent);
        }
    }
    private void showAllBombs(int idToSkip) {


        for(int i=1; i<=SizeOfBoard*SizeOfBoard ; i++) {
                int resID = getResources().getIdentifier(String.valueOf(i), "id", getPackageName());
                imageButton = ( findViewById(resID));
                if (booleanBombArray[i]) {
                    if(idToSkip!=i){
                        imageButton.setBackgroundResource(R.drawable.smallbomb);
                    }
                    else {
                        imageButton.setBackgroundResource(R.drawable.boomicon);
                    }
                }
            }
        }
    @Override
    public void onResult(int result) {

    }
    @Override
    public void onChoose(boolean isYes) {
        if(isYes) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }else {
            finish();
        }
    }
    @SuppressLint("StaticFieldLeak")
    private class CalculatorTask extends AsyncTask<CalculatorParams, Void, Integer> {
        @Override
        protected Integer doInBackground(CalculatorParams... params) {

            if (params == null || params.length != 1)
                return null;
            String action = params[0].action;
            int num1 = params[0].num1;
            int num2 = params[0].num2;

            String response = HttpRequest.httpGet(BASE_URL + "?action=" + action + "&num1=" + num1 + "&num2=" + num2);
            if(response == null)
                return null;
            try {
                return Integer.valueOf(response);
            } catch (Exception ex) {
                return null;
            }
        }
    }
    public void btnShowYesNoDialog(View view) {
        YesNoFragment yesNoFragment = new YesNoFragment();
        yesNoFragment.setQuestion("Play Again?");
        yesNoFragment.setYesNoFragmentListener(this);
        yesNoFragment.show(getFragmentManager(), "");
    }
}