package com.btran.bu.sudokusolver;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btran.bu.sudokusolver.solver.DancingLinks;
import com.btran.bu.sudokusolver.util.StringUtil;
import com.btran.bu.sudokusolver.util.SudokuUtil;

import java.io.FileOutputStream;

public class AnswerActivity extends AppCompatActivity
{
	// define the Sudoku board URL
    private static final String PLAY_URL = "http://www.sudoku.com/";

    private DancingLinks _solver;

    private String _inputAndOutput;

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
        textView.setTextSize(24);

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
        textView.setText(StringUtil.createSudokuMessage(cells));

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.answer);
        layout.addView(textView);

        Log.i("Construction", "Displaying Answer Activity");

        // store the input and output in portrait format in preparation for share functionality
        _inputAndOutput = StringUtil.createPortraitSudokuMessage(input, cells);

        // add the share button click listener, which share the answer
        Button shareButton = (Button) findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Sharing", "Clicked share button");
                shareSudokuAnswer(v);
            }
        });

        // add the play button click listener, which will send the user to the browser to play sudoku
		Button playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Playing", "Clicked play button");
                playSudoku(v);
            }
        });
    }

    /**
     * Share the Sudoku Answer with an available application
     *
     * @param v
     */
    private void shareSudokuAnswer(View v)
    {
        // create an ACTION_SEND intent that is provided the input and output of the Sudoku Board
        Intent sendAnswer = new Intent(Intent.ACTION_SEND);
        sendAnswer.putExtra(Intent.EXTRA_TEXT, _inputAndOutput);
        sendAnswer.setType("text/plain");

        // if applicable, create a chooser to allow the user to choose from several applications
        startActivity(Intent.createChooser(sendAnswer, getResources().getText(R.string.share_to)));
    }

    /**
     * Link the browser URL for a sudoku game!
     * @param v
     */
    private void playSudoku(View v)
    {
        // link the user to the Sudoku URL
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_URL));
        startActivity(browserIntent);
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
        String historyInputOutput = StringUtil.createLandscapeSudokuMessage(input, output);

        // TODO: Consider using XML data instead of pure text.
        try
        {
            FileOutputStream fos = openFileOutput(SudokuUtil.HISTORY_FILE, Context.MODE_APPEND);
            fos.write(historyInputOutput.getBytes());
        }
        catch (Exception e) {
            Log.i("Appending Fail", "Failed to append to History File.");
        }
    }
}
