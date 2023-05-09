package com.arassh.sudoku;

import android.os.Build;
import android.os.FileUtils;
import android.util.Log;

import com.arassh.sudoku.Solver.SudokuSolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Config {
    private final int[][] grid1;
    private final int[][] grid2;


    public Config(String game){
        String[] gameNumbers = game.split(",");
        grid1 = new int[9][9];
        grid2 = new int[9][9];
        for (int i = 0; i < gameNumbers.length; i++) {
            int number = Integer.parseInt(gameNumbers[i]);
            grid1[i / 9][i % 9] = number;
            grid2[i / 9][i % 9] = number;
        }
        SudokuSolver.solveSudoku(grid2, 0,0);
    }
    public int[][] getGrid1() {
        return this.grid1;
    }
    public int[][] getGrid2(){
        return this.grid2;
    }
}





