package com.btran.bu.sudokusolver;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

public class AnswerActivity extends AppCompatActivity {
    // define the Sudoku board URL
    private static final String PLAY_URL = "http://www.sudoku.com/";
    private static final String PHONE_NUMBER = "1234567890";

    private static final int PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final int PERMISSIONS_REQUEST_INTERNET = 2;
    private static final int PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE = 3;

    private DancingLinks _solver;

    private String _inputAndOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        if (!isEmpty) {
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
        } else {
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
                playSudoku();
            }
        });

        // add the play button click listener, which will send the user to the browser to play sudoku
        Button callButton = (Button) findViewById(R.id.callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Calling", "Clicked call button");
                callSudokuSupport();
            }
        });


    }

    /**
     * Share the Sudoku Answer with an available application
     *
     * @param v
     */
    private void shareSudokuAnswer(View v) {
        // create an ACTION_SEND intent that is provided the input and output of the Sudoku Board
        Intent sendAnswer = new Intent(Intent.ACTION_SEND);
        sendAnswer.putExtra(Intent.EXTRA_TEXT, _inputAndOutput);
        sendAnswer.setType("text/plain");

        // if applicable, create a chooser to allow the user to choose from several applications
        startActivity(Intent.createChooser(sendAnswer, getResources().getText(R.string.share_to)));
    }

    /**
     * Link the browser URL for a Sudoku game!
     */
    private void playSudoku() {
        // Verify that the application has permission to access the user's internet
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            // The application does not have permission
            // Create a Request for Permission and asynchronously wait for the response
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PERMISSIONS_REQUEST_INTERNET);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE);
            Log.i("Permissions", "Requesting permissions to access \"INTERNET\" and \"ACCESS_NETWORK_STATE\"functionality");
            return;
        }

        // get connectivity manager and network info to confirm that the user has internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        boolean hasInternet = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (hasInternet) {
            // link the user to the Sudoku URL
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_URL));
            startActivity(browserIntent);

            Toast.makeText(AnswerActivity.this, "You have Internet! Enjoy your Sudoku Game!", Toast.LENGTH_SHORT).show();

            Log.i("Browsing", "User has internet. Success.");
        } else {
            Toast.makeText(AnswerActivity.this, "You don't have Internet. Try playing Sudoku later.", Toast.LENGTH_SHORT).show();

            Log.i("Browsing", "User has no internet. Fail.");
        }
    }

    /**
     * Call the Sudoku Support Team
     */
    private void callSudokuSupport()
    {
        // Verify that the application has permission to access the user's phone service
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            // The application does not have permission
            // Create a Request for Permission and asynchronously wait for the response
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CALL_PHONE);

            Log.i("Permissions", "Requesting permissions to access \"CALL_PHONE\" functionality");
        }
        else
        {
            // Permissions have been accepted by the user. Call Sudoku Support immediately
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + PHONE_NUMBER));
            startActivity(intent);

            Log.i("Calling", "Successfully calling Sudoku Support");
        }
    }

    /**
     * This function is required to handle all asynchronous callbacks that refer to permissions.
     *
     * In this case, we are handling the permission request to using the "CALL_PHONE" service.
     *
     * @param requestCode the permission request code that needs to be handled
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case PERMISSIONS_REQUEST_CALL_PHONE:
                // we have received permissions to use the CALL_PHONE feature
                // call the function again to initiate the call (this needs to
                // happen to make sure checkSelfPermission() validates the permissions)
                callSudokuSupport();
                break;
            case PERMISSIONS_REQUEST_INTERNET:
            case PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE:
                // we have received permissions to use the INTERNET and ACCESS_NETWORK_STATE features
                // call the function again to initiate the play functionality (this needs to
                // happen to make sure checkSelfPermission() validates the permissions)
                playSudoku();
                break;
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
