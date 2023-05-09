package com.arassh.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.arassh.sudoku.Solver.SudokuSolver;

public class GameView extends View {

    private int width = 10;
    private int height = 10;


    private int gap = 10;
    private int headerLineHeight = 180;
    private int headerTopStart = 0;
    private int tableTopStart = headerLineHeight + 2 * gap;

    private Config config;
    private boolean startTimerThread = false;
    private boolean stoped = false;
    private int[][] grid;
    private int counter = 0;

    public GameView(Context context, Config config) {
        super(context);
        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;
        this.config = config;
        this.grid = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j] = config.getGrid1()[i][j];
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawHeader(canvas);
        drawBackground(canvas);
        drawTable(canvas);
        drawGridNumbers(canvas);

        if (!startTimerThread) {
            timer(canvas);
        }
        super.onDraw(canvas);
    }


    private void drawHeader(Canvas c) {
        int topEnd = headerTopStart + headerLineHeight;
        int topStartElement = headerTopStart + 2 * gap;
        int topEndElement = topEnd - 2 * gap;
        int useWidth = width - 8 * gap;
        int widthRect = (int) (useWidth * 0.33);
        int s1 = 2 * gap;
        int e1 = s1 + widthRect;
        int s2 = e1 + gap * 2;
        int e2 = s2 + widthRect;
        int textTopStart = (topStartElement + topEndElement) / 2 + 2 * gap;
        Paint p = new Paint();
        p.setColor(Color.parseColor("#2e75b6"));
        c.drawRect(0, headerTopStart, width, topEnd, p);
        // draw text
        p.setColor(Color.parseColor("#ced63c"));
        p.setTextSize(64);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(8);
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.parseColor("#FFFFFF"));
        c.drawText("Time: " + counter, s2 + gap, textTopStart, p);
    }

    private int getSizeOfCell() {
        return (int) ((width - 4 * gap) / 9);
    }

    private void drawBackground(Canvas c) {
        Paint p = new Paint();
        p.setColor(Color.parseColor("#00ff00"));
        c.drawRect(gap * 2 + getSizeOfCell() * 3, tableTopStart, gap * 2 + getSizeOfCell() * 6,
                tableTopStart + getSizeOfCell() * 3, p);
        c.drawRect(gap * 2, tableTopStart + getSizeOfCell() * 3, gap * 2 + getSizeOfCell() * 3,
                tableTopStart + getSizeOfCell() * 6, p);
        c.drawRect(gap * 2 + getSizeOfCell() * 6, tableTopStart + getSizeOfCell() * 3,
                gap * 2 + getSizeOfCell() * 9, tableTopStart + getSizeOfCell() * 6, p);
        c.drawRect(gap * 2 + getSizeOfCell() * 3, tableTopStart + getSizeOfCell() * 6,
                gap * 2 + getSizeOfCell() * 6, tableTopStart + getSizeOfCell() * 9, p);
    }

    private void drawTable(Canvas c) {
        int sizeOfCell = getSizeOfCell();
        Paint p = new Paint();
        p.setStrokeWidth(6);
        for (int i = 0; i <= 9; i++) {
            c.drawLine(gap * 2 + i * sizeOfCell, tableTopStart, gap * 2 + i * sizeOfCell, tableTopStart + sizeOfCell * 9, p);
        }

        for (int i = 0; i <= 9; i++) {
            c.drawLine(gap * 2, tableTopStart + i * sizeOfCell, width - gap * 2, tableTopStart + i * sizeOfCell, p);
        }
    }


    private void timer(Canvas c) {
        startTimerThread = true;
        int speed = 500;
        Handler handler = new Handler();
        Runnable runner = new Runnable() {
            @Override
            public void run() {
                if (stoped) {
                    return;
                }
                counter++;
                drawGridNumbers(c);
                hasNext();
                updateGrid();
                invalidate();
                handler.postDelayed(this, speed);
            }
        };
        handler.postDelayed(runner, speed * 3);
    }

    public void stopGame() {
        stoped = true;
    }

    public void resumeGame() {
        stoped = false;
        invalidate();
    }

    private void updateGrid() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] == 0) {
                    grid[i][j] = config.getGrid2()[i][j];
                    return;
                }
            }
        }
    }

    private void hasNext() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] == 0) {
                    return;
                }
            }
        }
        stoped = true;
    }

    private void drawGridNumbers(Canvas c) {
        Paint p = new Paint();
        p.setTextSize(64);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                p.setColor(Color.parseColor("#000000"));
                int number = grid[i][j];
                String value = number == 0 ? " " : number + "";
                if (number != config.getGrid1()[i][j]) {
                    p.setColor(Color.parseColor("#ff0000"));
                }
                c.drawText(value, i * getSizeOfCell() + (float) (getSizeOfCell() / 2),
                        gap * 2 + j * getSizeOfCell() + (float) (getSizeOfCell() / 2) + tableTopStart, p);
            }
        }

    }


}
