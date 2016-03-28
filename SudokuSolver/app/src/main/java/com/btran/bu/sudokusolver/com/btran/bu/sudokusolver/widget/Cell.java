package com.btran.bu.sudokusolver.com.btran.bu.sudokusolver.widget;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridLayout.LayoutParams;

import com.btran.bu.sudokusolver.MainActivity;

public class Cell
{
    private static final String KEY_LISTENERS = "0123456789";
    private static final int MAX_LENGTH = 1;
    private static final InputFilter[] inputFilters = new InputFilter[]
            {
                    new InputFilter.LengthFilter(MAX_LENGTH)
            };

    private EditText _text;

    public Cell(Context context)
    {
        _text = new EditText(context);
    }

    public static int getIndex(int row, int col)
    {
        return row * MainActivity.TOTAL_COLUMN_CELLS + col;
    }

    public void reset()
    {
        _text.setText("");
    }

    public void setParentAtRowCol(GridLayout gridLayout, int row, int col)
    {
        _text.setSingleLine(true);
        _text.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        _text.setKeyListener(DigitsKeyListener.getInstance(KEY_LISTENERS));
        _text.setFilters(inputFilters);

        // TODO: See if layoutParams can be cached
        LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = LayoutParams.WRAP_CONTENT;
        layoutParams.height = LayoutParams.WRAP_CONTENT;
        layoutParams.rowSpec = GridLayout.spec(row);
        layoutParams.columnSpec = GridLayout.spec(col);
        _text.setLayoutParams(layoutParams);

        gridLayout.addView(_text);
    }

    public String getValue()
    {
        return _text.getText().toString();
    }
}
