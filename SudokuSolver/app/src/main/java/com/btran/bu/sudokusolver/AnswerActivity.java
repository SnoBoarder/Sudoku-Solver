package com.btran.bu.sudokusolver;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.btran.bu.sudokusolver.solver.DancingLinks;
import com.btran.bu.sudokusolver.util.StringUtil;
import com.btran.bu.sudokusolver.util.SudokuUtil;

import java.io.FileOutputStream;

public class AnswerActivity extends AppCompatActivity
{
	// define the Sudoku board URL
    private static final String PLAY_URL = "http://www.sudoku.com/";

    private ConnectivityManager _connectivityManager;
    private NetworkInfo _networkInfo;

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
        boolean isEmpty = intent.getBooleanExtra(MainActivity.EXTRA_EMPTY_CHECK, false);
        TextView textView = new TextView(this);
        textView.setTextSize(30);

        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        textView.setLayoutParams(layoutParams);

        if (!isEmpty)
        {
            textView.setBackground(getDrawable(R.drawable.answer_background));

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

            // store the input and output in portrait format in preparation for share functionality
            _inputAndOutput = StringUtil.createPortraitSudokuMessage(input, cells);
        }
        else
        {
            // state that there is no answer
            textView.setText(getResources().getString(R.string.no_answer));
        }

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.answer);
        layout.addView(textView);

        Log.i("Construction", "Displaying Answer Activity");

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

        // get connectivity manager and network info to confirm that the user has internet
        _connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        _networkInfo = _connectivityManager.getActiveNetworkInfo();
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
        boolean hasInternet = _networkInfo != null && _networkInfo.isConnectedOrConnecting();

        if (hasInternet)
        {
            // link the user to the Sudoku URL
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_URL));
            startActivity(browserIntent);
            Log.i("Browsing", "Successfully opening URL in browser");

            Toast.makeText(AnswerActivity.this, "You have Internet! Enjoy your Sudoku Game!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(AnswerActivity.this, "You don't have Internet. Try playing Sudoku later.", Toast.LENGTH_SHORT).show();
        }
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
