package com.arassh.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String games = null;
        try {
            games = Downloader.downloadGames("https://sharetext.me/raw/zgzqhgvvqx");
            startGame(games);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void startGame(String games){
        TextView loading = (TextView) findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        Button btn1 = (Button) findViewById(R.id.game1Btn);
        Button btn2 = (Button) findViewById(R.id.game2Btn);
        Button btn3 = (Button) findViewById(R.id.game3Btn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SudokuActivity.class);
                intent.putExtra("game", getGame(games, 0));
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SudokuActivity.class);
                intent.putExtra("game", getGame(games, 1));
                startActivity(intent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SudokuActivity.class);
                intent.putExtra("game", getGame(games, 2));
                startActivity(intent);
            }
        });
    }

    protected String getGame(String games, int index){
        return games.split("-")[index];
    }
}






















