package com.btran.bu.sudokusolver.widget;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridLayout.LayoutParams;

/**
 * The Cell class encapsulates the formatting and layout parameters of a Sudoku Cell, allowing
 * instances to be created and their positions be set with a simple API of calling
 * "setParentAtRowAndColumn()".
 *
 * The Cell also provides the value of the Sudoku Cell and the ability to reset the Cell's contents.
 */
public class Cell
{
    // cache EditText settings for all Cell instances
    private static final String KEY_LISTENERS = "0123456789";
    private static final int MAX_LENGTH = 1;
    private static final InputFilter[] inputFilters = new InputFilter[]
            {
                    new InputFilter.LengthFilter(MAX_LENGTH)
            };
    private static final boolean SINGLE_LINE_ENABLED = true;

    private EditText _text;

    /**
     * Constructs the cell class with a custom EditText
     *
     * @param context
     */
    public Cell(Context context)
    {
        _text = new EditText(context);

        // setup EditText settings
        _text.setSingleLine(SINGLE_LINE_ENABLED);
        _text.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        _text.setKeyListener(DigitsKeyListener.getInstance(KEY_LISTENERS));
        _text.setFilters(inputFilters);
    }

    /**
     * Reset the Cell for future Sudoku board solving
     */
    public void reset()
    {
        _text.setText("");
    }

    /**
     * Place the cell within the grid layout at the specified row and column.
     *
     * @param parent - the cell's parent
     * @param row - the row that the cell will be set at
     * @param col - the column that the cell will be set at
     */
    public void setParentAtRowAndColumn(GridLayout parent, int row, int col)
    {
        // prepare the layout parameters for the EditText
        // TODO: Consider caching the layout params and only changing the spec row and spec column
        LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = LayoutParams.WRAP_CONTENT;
        layoutParams.height = LayoutParams.WRAP_CONTENT;

        // set the row and column in the correct location of the Sudoku Board
        layoutParams.rowSpec = GridLayout.spec(row);
        layoutParams.columnSpec = GridLayout.spec(col);

        // set the layout params and add the EditText to the GridLayout parent
        _text.setLayoutParams(layoutParams);
        parent.addView(_text);
    }

    /**
     * Get the value of the cell
     *
     * @return the value of the cell
     */
    public String getValue()
    {
        return _text.getText().toString();
    }
    public void setValue(String value) { _text.setText(value); }
}
