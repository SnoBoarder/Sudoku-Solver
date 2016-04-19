package com.btran.bu.sudokusolver;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import com.btran.bu.sudokusolver.fragment.SettingsDialogFragment;
import com.btran.bu.sudokusolver.util.SudokuUtil;
import com.btran.bu.sudokusolver.widget.Cell;

public class MainActivity extends ActionBarActivity
{
    public final static String EXTRA_CELLS = "com.btran.bu.sudokusolver.CELLS";

    private Cell[] _cells;

    /**
     * Override onCreate and initialize the Sudoku app.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
        {
            // initialize the board
            initialize();
        }
        else
        {
            Log.i("Saved Instance", "Reset board");

            if (_cells != null)
            {
                // clear the cell inputs
                for (int i = 0; i < _cells.length; ++i)
                {
                    _cells[i].reset();
                }
            }
        }
    }

    /**
     * Initialize the activity.
     */
    private void initialize()
    {
        Log.i("Initialize", "Initialize Sudoku Board");

        GridLayout gridLayout = (GridLayout) findViewById(R.id.cellGrid);
        gridLayout.setBackground(getDrawable(R.drawable.sudoku_background));
        gridLayout.setRowCount(SudokuUtil.TOTAL_ROW_CELLS);
        gridLayout.setColumnCount(SudokuUtil.TOTAL_COLUMN_CELLS);

        // dynamically create and set the cells within the grid layout
        // TODO: Consider dynamic Sudoku Boards
        _cells = new Cell[SudokuUtil.TOTAL_CELL_INPUTS];
        for (int row = 0; row < SudokuUtil.TOTAL_ROW_CELLS; ++row)
        {
            for (int col = 0; col < SudokuUtil.TOTAL_COLUMN_CELLS; ++col)
            {
                Cell cell = new Cell(this);
                cell.setParentAtRowAndColumn(gridLayout, row, col);
                _cells[SudokuUtil.getIndex(row, col)] = cell;
            }
        }

        Log.i("Initialize", "Set Submit Button's listener");

        // add the submit button click listener, which will submit the sudoku board
        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Submitting", "Clicked submit button");
                submitSudokuBoard(v);
            }
        });

        // add the history button click listener
        Button historyButton = (Button) findViewById(R.id.historyButton);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("History", "Clicked history button");
                startActivity(new Intent(v.getContext(), HistoryActivity.class));
            }
        });

        // test button to generate a sudoku board for testing
        Button generateButton = (Button) findViewById(R.id.generateButton);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Generating", "Clicked generate button");
                generateSudokuBoard(v);
            }
        });
    }

    /**
     * Create the options menu by inflating a xml defined menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        Log.i("Initialize", "Set menu to menu_my layout");
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    /**
     * Trigger the options item
     *
     * @param item the item that was selected in the options
     * @return true if it was successful, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.options_about:
                Log.i("about", "show about fragment");

                // show settings fragment
                DialogFragment newFragment = new SettingsDialogFragment();
                newFragment.show(getFragmentManager(), "settings");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Send the Sudoku Board to the Answer Activity.
     *
     * @param view
     */
    private void submitSudokuBoard(View view)
    {
        // Prepare the intent to the AnswerActivity
        Intent intent = new Intent(this, AnswerActivity.class);

        // aggregate all the cell data
        String[] cells = new String[SudokuUtil.TOTAL_CELL_INPUTS];
        for (int i = 0; i < _cells.length; ++i)
        {
            // extract the cell value from the EditText and store it
            String cell = _cells[i].getValue();
            cells[i] = cell.isEmpty() ? "0" : cell; // set "0" for empty cells
        }

        logInputSudokuBoard(cells);

        // pass the cell data with the intent
        intent.putExtra(EXTRA_CELLS, cells);

        startActivity(intent);
    }

    private void logInputSudokuBoard(String[] cells)
    {
        String logValue = "";

        for (int i = 0; i < _cells.length; ++i)
    {
        if (i > 0)
        {
            // comma separate each item
            logValue += ", ";
        }
        logValue += cells[i];
    }

        // log the submission with the string of cells
        Log.i("Submission", "Submitted Sudoku Board: " + logValue);
    }

    /**
     * Generate a sample Sudoku Board for testing
     *
     * This is a temporary button purely for testing.
     *
     * @param view
     */
    private void generateSudokuBoard(View view)
    {
        int[] cells = SudokuUtil.getRandomBoard();

        for (int row = 0; row < SudokuUtil.TOTAL_ROW_CELLS; ++row)
        {
            for (int col = 0; col < SudokuUtil.TOTAL_COLUMN_CELLS; ++col)
            {
                int index = SudokuUtil.getIndex(row, col);

                Cell cell = _cells[index];
                // set value based on the example sudoku board
                cell.setValue(String.valueOf(cells[index] == SudokuUtil.EMPTY_CELL ? "" : cells[index]));
            }
        }
    }
}
