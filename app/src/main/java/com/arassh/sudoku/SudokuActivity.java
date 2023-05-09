package com.arassh.sudoku;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class SudokuActivity extends AppCompatActivity {
    GameView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent intent = getIntent();
        String game = ((Intent) intent).getStringExtra("game");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        view = new GameView(this, new Config(game));
        LinearLayout layout = (LinearLayout) findViewById(R.id.game_layout);
        layout.removeAllViews();
        layout.addView(view);


    }

    @Override
    public void onBackPressed() {
        view.stopGame();
        super.onBackPressed();
        this.finish();
    }



}