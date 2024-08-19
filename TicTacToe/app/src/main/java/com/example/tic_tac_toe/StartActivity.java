package com.example.tic_tac_toe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    private EditText et_playerOne, et_playerTwo;
    private Button bt_smallField, bt_largeField, bt_start;
    int fieldSize = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        et_playerOne = findViewById(R.id.et_playerOne);
        et_playerTwo = findViewById(R.id.et_playerTwo);

        bt_smallField = findViewById(R.id.bt_smallField);
        bt_smallField.setOnClickListener(new ChangeFieldSize());

        bt_largeField = findViewById(R.id.bt_largeField);
        bt_largeField.setOnClickListener(new ChangeFieldSize());

        bt_start = findViewById(R.id.bt_start);
        bt_start.setOnClickListener(new StartGame());
    }

    class ChangeFieldSize implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(view.equals(bt_smallField)){
                fieldSize = 5;

                bt_smallField.setTextColor(getResources().getColor(R.color.white));
                bt_smallField.setBackgroundColor(getResources().getColor(R.color.purple));
                bt_largeField.setTextColor(getResources().getColor(R.color.purple));
                bt_largeField.setBackgroundColor(getResources().getColor(R.color.white));
            }else if(view.equals(bt_largeField)){
                fieldSize = 10;

                bt_largeField.setTextColor(getResources().getColor(R.color.white));
                bt_largeField.setBackgroundColor(getResources().getColor(R.color.purple));
                bt_smallField.setTextColor(getResources().getColor(R.color.purple));
                bt_smallField.setBackgroundColor(getResources().getColor(R.color.white));
            }
        }
    }

    class StartGame implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String playerOneName = et_playerOne.getText().toString().trim();
            String playerTwoName = et_playerTwo.getText().toString().trim();

            if(containsLetters(playerOneName) && containsLetters(playerTwoName)){
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtra("PLAYER_ONE_NAME", playerOneName);
                intent.putExtra("PLAYER_TWO_NAME", playerTwoName);
                intent.putExtra("FIELD_SIZE", fieldSize);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(StartActivity.this, R.string.name_alert, Toast.LENGTH_SHORT).show();
            }
        }

        private boolean containsLetters(String str) {
            return str.matches(".*[a-zA-ZÀ-ÿ].*");
        }
    }

}