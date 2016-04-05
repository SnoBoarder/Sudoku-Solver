package com.btran.bu.sudokusolver;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btran.bu.sudokusolver.solver.DancingLinks;
import com.btran.bu.sudokusolver.util.SudokuUtil;

import java.io.FileOutputStream;

public class AnswerActivity extends AppCompatActivity
{
    private static final String HISTORY_HEADER = "Input:\t\t\t\t\t\t\t\t\t\t\t\t\t\tOutput:\n";

    private DancingLinks _solver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.answer_title);

        Intent intent = getIntent();
        String[] cellData = intent.getStringArrayExtra(MainActivity.EXTRA_CELLS);
        TextView textView = new TextView(this);
        textView.setTextSize(14);

        // convert cell data to an int array
		int[] cells = new int[cellData.length];
        SudokuUtil.convertToIntArray(cellData, cells);

        // get the solution and populate it to the passed in cells array
		_solver = new DancingLinks();
        _solver.loadAndSearch(cells);

        // store input and output into history file
        int[] input = new int[cellData.length];
        SudokuUtil.convertToIntArray(cellData, input);
        storeHistory(input, cells);

        // display the solution
        textView.setText(getDisplayString(cells));

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.answer);
        layout.addView(textView);

        Log.i("Construction", "Displaying Answer Activity");
    }

    private String getDisplayString(int[] cells)
    {
        String retVal = "";
        for (int i = 0; i < cells.length; ++i)
        {
            if (i % SudokuUtil.TOTAL_COLUMN_CELLS == 0)
                retVal += "\n";

            retVal += " " + cells[i];

            retVal += "\t";
        }

        return retVal;
    }

    /**
     * Store the History of all Sudoku Boards input and output
     *
     * @param input the provided input from the user
     * @param output the solution
     */
    private void storeHistory(int[] input, int[] output)
    {
        // create formatted string
        String appendString = HISTORY_HEADER;

        // create the input and output format for the text file
        for (int row = 0; row < SudokuUtil.TOTAL_ROW_CELLS; ++row)
        {
            // display formatted row of input
            appendString += getFormattedRow(input, row);

            // create separator between the input and output
            appendString += "\t|\t";

            // display formatted row of output
            appendString += getFormattedRow(output, row);

            // go to the next line
            appendString += "\n";
        }

        appendString += "\n";

        // TODO: Consider using XML data instead of pure text.
        try
        {
            FileOutputStream fos = openFileOutput(SudokuUtil.HISTORY_FILE, Context.MODE_APPEND);
            fos.write(appendString.getBytes());
        }
        catch (Exception e) {
            Log.i("Appending Fail", "Failed to append to History File.");
        }
    }

    /**
     * Get the formatted row of a provided Sudoku board
     *
     * @param input Sudoku Board
     * @param row The row we want from the Sudoku board
     * @return Formatted row
     */
    private String getFormattedRow(int[] input, int row)
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
                retVal += "\t";
            }

            // add dashes for empty cell items
            int cell = input[i];
            retVal += (cell == 0) ? "--" : cell;
        }

        return retVal;
    }
}
