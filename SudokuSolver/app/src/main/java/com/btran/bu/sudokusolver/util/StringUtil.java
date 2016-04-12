package com.btran.bu.sudokusolver.util;

/**
 * This class handles formatting special strings for the Sudoku Boards
 */
public class StringUtil
{
    private static final String LANDSCAPE_HEADER = "Input:\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tOutput:\n";
    private static final String PORTRAIT_INPUT_HEADER = "Input:\n";
    private static final String PORTRAIT_OUTPUT_HEADER = "Output:\n";

    /**
     * Create a portrait representation of the input and output of the Sudoku Board
     *
     * @param input the Sudoku Board without the answer
     * @param output the Sudoku Board with the answer
     * @return a formatted portrait String of the Sudoku input and output
     */
    public static String createPortraitSudokuMessage(int[] input, int[] output)
    {
        // define the header for input
        String retVal = PORTRAIT_INPUT_HEADER;

        // display the input rows
        retVal += createSudokuMessage(input);

        // define the header for output
        retVal += PORTRAIT_OUTPUT_HEADER;

        // display the output rows
        retVal += createSudokuMessage(output);

        return retVal;
    }

    /**
     * Create a landscape representation of the input and output of the Sudoku Board
     *
     * @param input the Sudoku Board without the answer
     * @param output the Sudoku Board with the answer
     * @return a formatted landscape String of the Sudoku input and output
     */
    public static String createLandscapeSudokuMessage(int[] input, int[] output)
    {
        // create formatted string
        String retVal = LANDSCAPE_HEADER;

        // create the input and output format for the text file
        for (int row = 0; row < SudokuUtil.TOTAL_ROW_CELLS; ++row)
        {
            // display formatted row of input
            retVal += getFormattedRow(input, row);

            // create separator between the input and output
            retVal += "\t\t\t|\t\t\t";

            // display formatted row of output
            retVal += getFormattedRow(output, row);

            // go to the next line
            retVal += "\n";
        }

        retVal += "\n";

        return retVal;
    }

    /**
     * Get a formatted string of the cells passed into the function
     *
     * @param cells Cells that need to be formatted
     * @return formatted cells
     */
    public static String createSudokuMessage(int[] cells)
    {
        String retVal = "";
        for (int row = 0; row < SudokuUtil.TOTAL_ROW_CELLS; ++row)
        {
            // display formatted row of input
            retVal += getFormattedRow(cells, row);

            // go to the next line
            retVal += "\n";
        }

        return retVal;
    }

    /**
     * Get the formatted row of a provided Sudoku board
     *
     * @param input Sudoku Board
     * @param row The row we want from the Sudoku board
     * @return Formatted row
     */
    private static String getFormattedRow(int[] input, int row)
    {
        // get the first index of the row
        int beginIndex = SudokuUtil.getIndex(row, 0);

        // get the last index of the row
        int lastIndex = SudokuUtil.getIndex(row, SudokuUtil.TOTAL_COLUMN_CELLS);

        String retVal = "";
        // traverse the indices and format the row string
        for (int i = beginIndex; i < lastIndex; ++i)
        {
            if (i > beginIndex)
            {
                // insert tabs after the first item
                retVal += "\t\t";
            }

            // add dashes for empty cell items
            int cell = input[i];
            retVal += (cell == 0) ? "--" : cell;
        }

        return retVal;
    }
}
