package com.btran.bu.sudokusolver.util;

import com.btran.bu.sudokusolver.MainActivity;

public class SudokuUtil
{
    public static final int TOTAL_ROW_CELLS = 9;
    public static final int TOTAL_COLUMN_CELLS = 9;

    public static final int TOTAL_CELL_INPUTS = TOTAL_ROW_CELLS * TOTAL_COLUMN_CELLS;

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
}
