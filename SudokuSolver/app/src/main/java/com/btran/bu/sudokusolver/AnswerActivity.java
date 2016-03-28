package com.btran.bu.sudokusolver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AnswerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String[] cells = intent.getStringArrayExtra(MainActivity.EXTRA_CELLS);
        TextView textView = new TextView(this);
        textView.setTextSize(14);

        // parse the string and display
        String displayString = "";
        for (int i = 0; i < cells.length; ++i)
        {
            if (i % 9 == 0)
            {
                displayString += "\n";
            }

            String cell = cells[i];

            if (cell.isEmpty())
            {
                displayString += "  ";
            }
            else
            {
                displayString += cell;
            }

            displayString += "\t";
        }

        textView.setText(displayString);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.answer);
        layout.addView(textView);
    }
}
