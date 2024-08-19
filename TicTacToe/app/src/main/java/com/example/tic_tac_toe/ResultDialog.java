package com.example.tic_tac_toe;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class ResultDialog extends Dialog {
    private String message, type;
    private TextView tv_result;
    private Button bt_playAgain, bt_restartGame, bt_mainMenu, bt_resume;
    private MainActivity mainActivity;
    public ResultDialog(@NonNull Context context, String message, String type, MainActivity mainActivity) {
        super(context);
        this.message = message;
        this.type = type;
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tv_result = findViewById(R.id.tv_result);
        tv_result.setText(message);

        bt_resume = findViewById(R.id.bt_resume);

        if(type == "end"){
            bt_resume.setVisibility(View.GONE);
        }else {
            bt_resume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivity.resumeGame();
                    dismiss();
                }
            });
        }

        bt_playAgain = findViewById(R.id.bt_playAgain);
        bt_playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.playAgain();
                dismiss();
            }
        });

        bt_restartGame = findViewById(R.id.bt_restartGame);
        bt_restartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.restartGame();
                dismiss();
            }
        });

        bt_mainMenu = findViewById(R.id.bt_mainMenu);
        bt_mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.returnToMainMenu();
                dismiss();
            }
        });
    }
}
