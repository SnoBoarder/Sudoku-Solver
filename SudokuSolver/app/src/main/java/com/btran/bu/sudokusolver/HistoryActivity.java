package com.btran.bu.sudokusolver;

import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import com.btran.bu.sudokusolver.util.SudokuUtil;

import java.io.FileInputStream;

public class HistoryActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.history_title);

        TextView _historyText = (TextView) findViewById(R.id.historyText);
        _historyText.setText(getHistory());
        _historyText.setTextSize(22);

        BitmapDrawable bitmap = (BitmapDrawable) getDrawable(R.drawable.history_background);
        bitmap.setTileModeX(Shader.TileMode.CLAMP);
        bitmap.setTileModeY(Shader.TileMode.REPEAT);
        _historyText.setBackground(bitmap);
    }

    /**
     * @return the history from the history file
     */
    private String getHistory()
    {
        String text = "";
        try
        {
            FileInputStream fis = openFileInput(SudokuUtil.HISTORY_FILE);
            byte[] reader = new byte[fis.available()];
            while (fis.read(reader) != -1){} // read the entire file
            text = new String(reader);
        }
        catch (Exception e)
        {
            text = "Couldn't find history.";
        }

        return text;
    }
}
