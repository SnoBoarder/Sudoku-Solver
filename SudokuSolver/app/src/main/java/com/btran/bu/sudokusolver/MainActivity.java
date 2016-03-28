package com.btran.bu.sudokusolver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;

import com.btran.bu.sudokusolver.com.btran.bu.sudokusolver.widget.Cell;

public class MainActivity extends AppCompatActivity
{
    public final static String EXTRA_CELLS = "com.example.bttra.myfirstapp.CELLS";

    private static final int TOTAL_ROW_CELLS = 9;
    public static final int TOTAL_COLUMN_CELLS = 9;

    private static final int TOTAL_CELL_INPUTS = TOTAL_ROW_CELLS * TOTAL_COLUMN_CELLS;

    private static final String CELL_PREFIX = "cell_";
    private static final String CELL_DEF_TYPE = "id";

    private Cell[] _cells;
    private EditText[] _cellInputs;

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
            // clear the cell inputs
            for (int i = 0; i < _cellInputs.length; ++i)
            {
                _cells[i].reset();
            }
        }
    }

    /**
     * Initialize the activity.
     */
    private void initialize()
    {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.cellGrid);
        gridLayout.setRowCount(TOTAL_ROW_CELLS);
        gridLayout.setColumnCount(TOTAL_COLUMN_CELLS);

        // dynamically create and set the cells within the grid layout
        // TODO: Consider dynamic Sudoku Boards
        _cells = new Cell[TOTAL_CELL_INPUTS];
        for (int row = 0; row < 9; ++row)
        {
            for (int col = 0; col < 9; ++col)
            {
                Cell cell = new Cell(this);
                cell.setParentAtRowCol(gridLayout, row, col);
                _cells[Cell.getIndex(row, col)] = cell;
            }
        }

        // TODO: Create button event listener dynamically
    }

    /**
     * Send the Sudoku Board to the Answer Activity.
     *
     * @param view
     */
    public void submitSudokuBoard(View view)
    {
        // Prepare the intent to the AnswerActivity
        Intent intent = new Intent(this, AnswerActivity.class);

        // aggregate all the cell data
        String cellsString = "";
        String[] cells = new String[TOTAL_CELL_INPUTS];
        for (int i = 0; i < _cells.length; ++i)
        {
            // extract the cell value from the EditText and store it
            cells[i] = _cells[i].getValue();

            // keep track of the cell that will be submitted
            if (i > 0)
            {
                // comma separate each item
                cellsString += ", ";
            }
            cellsString += cells[i];
        }

        // log the submission with the string of cells
        Log.i("Submission", "Submitted Sudoku Board: " + cellsString);

        // pass the cell data with the intent
        intent.putExtra(EXTRA_CELLS, cells);

        startActivity(intent);
    }
}
