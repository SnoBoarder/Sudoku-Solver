package com.btran.bu.sudokusolver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btran.bu.sudokusolver.solver.DancingLinks;
import com.btran.bu.sudokusolver.util.SudokuUtil;

public class AnswerActivity extends AppCompatActivity
{
    private DancingLinks _solver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        // display the solution
        String displayString = "";
        for (int i = 0; i < cells.length; ++i)
        {
            if (i % SudokuUtil.TOTAL_COLUMN_CELLS == 0)
                displayString += "\n";

            displayString += " " + cells[i];

            displayString += "\t";
        }

        textView.setText(displayString);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.answer);
        layout.addView(textView);

        Log.i("Construction", "Displaying Answer Activity");
    }
}
