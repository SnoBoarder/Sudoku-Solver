package com.btran.bu.sudokusolver.util;

import com.btran.bu.sudokusolver.MainActivity;

import java.util.Random;

public class SudokuUtil
{
    public static final int EMPTY_CELL = 0;
    public static final int TOTAL_ROW_CELLS = 9;
    public static final int TOTAL_COLUMN_CELLS = 9;

    public static final int TOTAL_CELL_INPUTS = TOTAL_ROW_CELLS * TOTAL_COLUMN_CELLS;

    private static Random _generator = new Random();

    // Sample Sudoku Boards
    private static final int[][] SAMPLE_BOARDS = new int[][]
            {
                    new int[]{0,0,0,0,0,8,0,0,4,
                              0,8,4,0,1,6,0,0,0,
                              0,0,0,5,0,0,1,0,0,
                              1,0,3,8,0,0,9,0,0,
                              6,0,8,0,0,0,4,0,3,
                              0,0,2,0,0,9,5,0,1,
                              0,0,7,0,0,2,0,0,0,
                              0,0,0,7,8,0,2,6,0,
                              2,0,0,3,0,0,0,0,0},

                    new int[]{5,0,8,0,7,3,1,9,0,
                              9,0,0,6,0,0,4,0,8,
                              0,0,0,9,0,8,0,3,5,
                              0,7,0,0,0,0,0,6,0,
                              0,0,2,0,0,0,9,0,0,
                              0,1,0,0,0,0,0,8,0,
                              1,9,0,3,0,6,0,0,0,
                              2,0,3,0,0,7,0,0,9,
                              0,8,7,1,9,0,3,0,4},

                    new int[]{5,3,0,0,7,0,0,0,0,
                              6,0,0,1,9,5,0,0,0,
                              0,9,8,0,0,0,0,6,0,
                              8,0,0,0,6,0,0,0,3,
                              4,0,0,8,0,3,0,0,1,
                              7,0,0,0,2,0,0,0,6,
                              0,6,0,0,0,0,2,8,0,
                              0,0,0,4,1,9,0,0,5,
                              0,0,0,0,8,0,0,7,9}
            };

    /**
     * Get the index of a cell based when flattened into a single array
     *
     * @param row
     * @param col
     * @return
     */
    public static int getIndex(int row, int col)
    {
        return row * TOTAL_COLUMN_CELLS + col;
    }

    public static int[] getRandomBoard()
    {
        return SAMPLE_BOARDS[_generator.nextInt(SAMPLE_BOARDS.length)];
    }

    public static void convertToIntArray(String[] input, int[] output)
    {
        for (int i = 0; i < input.length; ++i)
        {
            output[i] = Integer.parseInt(input[i]);
        }
    }
}
