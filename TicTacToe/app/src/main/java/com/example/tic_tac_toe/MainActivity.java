package com.example.tic_tac_toe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private TextView tv_playerOne, tv_playerTwo, tv_timer, tv_playerOneScore, tv_playerTwoScore;
    private ImageView[] fields;
    private LinearLayout ll_playerOne, ll_playerTwo, ll_playingField, ll_timer;
    private ArrayList<int[]> combinationList = new ArrayList<>();
    private int[] boxPositions;
    private Handler handler;
    private long startTime;
    private long elapsedTime;
    private int playerTurn = 1;
    private int selectedBoxes = 1;
    private int rows;
    private int cols;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startTimer();

        tv_playerOne = findViewById(R.id.tv_playerOne);
        tv_playerOne.setText(getIntent().getStringExtra("PLAYER_ONE_NAME"));

        tv_playerTwo = findViewById(R.id.tv_playerTwo);
        tv_playerTwo.setText(getIntent().getStringExtra("PLAYER_TWO_NAME"));

        tv_playerOneScore = findViewById(R.id.tv_playerOneScore);
        tv_playerTwoScore = findViewById(R.id.tv_playerTwoScore);

        tv_timer = findViewById(R.id.tv_timer);

        ll_playerOne = findViewById(R.id.ll_playerOne);
        ll_playerTwo = findViewById(R.id.ll_playerTwo);

        ll_playingField = findViewById(R.id.ll_playingField);

        ll_timer = findViewById(R.id.ll_timer);
        ll_timer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                stopTimer();

                ResultDialog resultDialog = new ResultDialog(MainActivity.this, getString(R.string.result_paused), "pause", MainActivity.this);
                resultDialog.setCancelable(false);
                resultDialog.show();
            }
        });

        rows = getIntent().getIntExtra("FIELD_SIZE" ,5);
        cols = getIntent().getIntExtra("FIELD_SIZE" ,5);

        fields = new ImageView[rows * cols];
        boxPositions = new int[rows * cols];

        for (int i = 0; i < rows; i++) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setWeightSum(rows);
            for (int j = 0; j < cols; j++) {
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
                imageView.setBackgroundResource(R.drawable.white_box);

                if(rows == 5){
                    imageView.setPadding(20, 20, 20, 20);
                }else if(rows == 10) {
                    imageView.setPadding(10, 10, 10, 10);
                }

                imageView.setAdjustViewBounds(true);
                imageView.setId(i * cols + j);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                layoutParams.setMargins(5, 5, 5, 5);
                imageView.setLayoutParams(layoutParams);

                fields[i * cols + j] = imageView;
                setFieldClickListener(imageView, i * cols + j);

                rowLayout.addView(imageView);
            }
            ll_playingField.addView(rowLayout);
        }

        Arrays.fill(boxPositions, 0);

        if(rows == 5) {
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 2; col++) {
                    int[] combination = new int[4];
                    for (int i = 0; i < 4; i++) {
                        combination[i] = row * 5 + col + i;
                    }
                    combinationList.add(combination);
                }
            }

            for (int col = 0; col < 5; col++) {
                for (int row = 0; row < 2; row++) {
                    int[] combination = new int[4];
                    for (int i = 0; i < 4; i++) {
                        combination[i] = (row + i) * 5 + col;
                    }
                    combinationList.add(combination);
                }
            }

            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 2; col++) {
                    int[] combination = new int[4];
                    for (int i = 0; i < 4; i++) {
                        combination[i] = (row + i) * 5 + col + i;
                    }
                    combinationList.add(combination);
                }
            }

            for (int row = 0; row < 2; row++) {
                for (int col = 3; col < 5; col++) {
                    int[] combination = new int[4];
                    for (int i = 0; i < 4; i++) {
                        combination[i] = (row + i) * 5 + col - i;
                    }
                    combinationList.add(combination);
                }
            }
        }else if(rows == 10) {
            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 6; col++) {
                    int[] combination = new int[5];
                    for (int i = 0; i < 5; i++) {
                        combination[i] = row * 10 + col + i;
                    }
                    combinationList.add(combination);
                }
            }

            for (int col = 0; col < 10; col++) {
                for (int row = 0; row < 6; row++) {
                    int[] combination = new int[5];
                    for (int i = 0; i < 5; i++) {
                        combination[i] = (row + i) * 10 + col;
                    }
                    combinationList.add(combination);
                }
            }

            for (int row = 0; row < 6; row++) {
                for (int col = 0; col < 6; col++) {
                    int[] combination = new int[5];
                    for (int i = 0; i < 5; i++) {
                        combination[i] = (row + i) * 10 + col + i;
                    }
                    combinationList.add(combination);
                }
            }

            for (int row = 9; row >= 4; row--) {
                for (int col = 0; col < 6; col++) {
                    int[] combination = new int[5];
                    for (int i = 0; i < 5; i++) {
                        combination[i] = (row - i) * 10 + col + i;
                    }
                    combinationList.add(combination);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    public void setFieldClickListener(ImageView field, int position) {
        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fieldClicked(position, fields[position]);
            }
        });
    }
    public void fieldClicked(int fieldPosition, ImageView imageView) {
        if (isBoxSelectable(fieldPosition)) {
            boxPositions[fieldPosition] = playerTurn == 1 ? 1 : 2;

            if(playerTurn == 1){
                imageView.setImageResource(R.drawable.x_image);
            }else{
                imageView.setImageResource(R.drawable.o_image);
            }

            if(checkWin()){
                stopTimer();

                ll_playerOne.setBackgroundResource(R.drawable.transparent_dashed_border);
                ll_playerTwo.setBackgroundResource(R.drawable.transparent_dashed_border);

                if(playerTurn == 1){
                    tv_playerOneScore.setText(String.valueOf(Integer.parseInt(tv_playerOneScore.getText().toString()) + 1));

                    ResultDialog resultDialog = new ResultDialog(MainActivity.this, tv_playerOne.getText().toString() + " " + getString(R.string.result_win), "end", MainActivity.this);
                    resultDialog.setCancelable(false);
                    resultDialog.show();
                }else{
                    tv_playerTwoScore.setText(String.valueOf(Integer.parseInt(tv_playerTwoScore.getText().toString()) + 1));

                    ResultDialog resultDialog = new ResultDialog(MainActivity.this, tv_playerTwo.getText().toString() + " " + getString(R.string.result_win), "end", MainActivity.this);
                    resultDialog.setCancelable(false);
                    resultDialog.show();
                }
            }else if(selectedBoxes == (rows * cols)){
                stopTimer();

                ResultDialog resultDialog = new ResultDialog(MainActivity.this, getString(R.string.result_draw), "end", MainActivity.this);
                resultDialog.setCancelable(false);
                resultDialog.show();

                ll_playerOne.setBackgroundResource(R.drawable.transparent_dashed_border);
                ll_playerTwo.setBackgroundResource(R.drawable.transparent_dashed_border);
            }else{
                selectedBoxes++;
                changePlayerTurn();
            }
        }
    }

    public boolean checkWin(){
        for(int i = 0; i < combinationList.size(); i++){
            int[] combination = combinationList.get(i);

            if(rows == 5){
                if(boxPositions[combination[0]] == playerTurn && boxPositions[combination[1]] == playerTurn && boxPositions[combination[2]] == playerTurn  && boxPositions[combination[3]] == playerTurn){
                    return true;
                }
            }else if (rows == 10){
                if(boxPositions[combination[0]] == playerTurn && boxPositions[combination[1]] == playerTurn && boxPositions[combination[2]] == playerTurn  && boxPositions[combination[3]] == playerTurn && boxPositions[combination[4]] == playerTurn){
                    return true;
                }
            }
        }

        return false;
    }
    public void changePlayerTurn(){
        playerTurn = playerTurn == 1 ? 2 : 1;

        if(playerTurn == 1){
            ll_playerOne.setBackgroundResource(R.drawable.white_dashed_border);
            ll_playerTwo.setBackgroundResource(R.drawable.transparent_dashed_border);
        }else{
            ll_playerOne.setBackgroundResource(R.drawable.transparent_dashed_border);
            ll_playerTwo.setBackgroundResource(R.drawable.white_dashed_border);
        }
    }

    public boolean isBoxSelectable(int boxPosition){
        return boxPositions[boxPosition] == 0;
    }

    public void startTimer() {
        handler = new Handler();
        startTime = SystemClock.uptimeMillis() - elapsedTime;

        handler.post(new Runnable() {
            @Override
            public void run() {
                long milliseconds = SystemClock.uptimeMillis() - startTime;
                int seconds = (int) (milliseconds / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                int hours = minutes / 60;
                minutes = minutes % 60;

                tv_timer.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void stopTimer(){
        handler.removeCallbacksAndMessages(null);
        elapsedTime = SystemClock.uptimeMillis() - startTime;
    }

    public void playAgain(){
        playerTurn = 1;
        selectedBoxes = 1;
        elapsedTime = 0;

        boxPositions = new int[rows * cols];
        Arrays.fill(boxPositions, 0);

        ll_playerOne.setBackgroundResource(R.drawable.white_dashed_border);
        ll_playerTwo.setBackgroundResource(R.drawable.transparent_dashed_border);

        for(int i = 0; i < fields.length; i++){
            fields[i] = findViewById(i);
            fields[i].setImageResource(R.drawable.white_box);
        }

        startTimer();
    }

    public void restartGame(){
        playerTurn = 1;
        selectedBoxes = 1;
        elapsedTime = 0;

        boxPositions = new int[rows * cols];
        Arrays.fill(boxPositions, 0);

        tv_playerOneScore.setText("0");
        tv_playerTwoScore.setText("0");

        ll_playerOne.setBackgroundResource(R.drawable.white_dashed_border);
        ll_playerTwo.setBackgroundResource(R.drawable.transparent_dashed_border);

        for(int i = 0; i < fields.length; i++){
            fields[i] = findViewById(i);
            fields[i].setImageResource(R.drawable.white_box);
        }

        startTimer();
    }

    public void resumeGame(){
        startTimer();
    }
    public void returnToMainMenu(){
        Intent intent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}