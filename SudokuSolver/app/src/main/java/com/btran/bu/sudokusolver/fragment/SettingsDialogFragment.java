package com.btran.bu.sudokusolver.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.btran.bu.sudokusolver.R;

public class SettingsDialogFragment extends DialogFragment
{
    /**
     * Creating the settings dialog fragment, which will provide options
     * to customize the Sudoku board and other features.
     *
     * @param savedInstanceState
     * @return the settings dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.settings_title);
        builder.setMessage(R.string.settings_message);
//                .setPositiveButton(R.string.settings_positive_button, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id){
//
//                    }
//                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
