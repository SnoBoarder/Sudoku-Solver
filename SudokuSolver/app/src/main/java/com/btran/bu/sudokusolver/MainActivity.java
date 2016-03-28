package com.btran.bu.sudokusolver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity
{
    public final static String EXTRA_CELLS = "com.example.bttra.myfirstapp.CELLS";

    private static final int TOTAL_CELL_INPUTS = 27;

    private static final String CELL_PREFIX = "cell_";
    private static final String CELL_DEF_TYPE = "id";

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
                _cellInputs[i].setText("");
            }
        }
    }

    /**
     * Initialize the activity.
     */
    private void initialize()
    {
        // store the references to all the cells for future use
        // NOTE: The individual "EditText" objects should be generated dynamically to allow for
        // dynamic Sudoku Boards
        _cellInputs = new EditText[TOTAL_CELL_INPUTS];
        for (int i = 0; i < _cellInputs.length; ++i)
        {
            String cellId = CELL_PREFIX + (i + 1);
            int resId = getResources().getIdentifier(cellId, CELL_DEF_TYPE, getPackageName());
            _cellInputs[i] = (EditText) findViewById(resId);
        }
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
        for (int i = 0; i < _cellInputs.length; ++i)
        {
            // extract the cell value from the EditText and store it
            cells[i] = _cellInputs[i].getText().toString();

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
