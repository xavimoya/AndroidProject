package xavi.smartalarm;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

/**
 * Created by Xavi on 24/3/17.
 */

public class DialogPreference extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final ArrayList<Integer> mSelectedItems = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Preferences");
        builder.setMessage("Check when you want to receive a push notification");
        boolean[] clickedItems = new boolean[getResources().getStringArray(R.array.checks).length];

        //Set multi-choice items
        builder.setMultiChoiceItems(getResources().getStringArray(R.array.checks),clickedItems,new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick (DialogInterface dialog,int which, boolean isChecked){
                if (isChecked) mSelectedItems.add(which);
                else if (mSelectedItems.contains(which)) mSelectedItems.remove(Integer.valueOf(which));
            }
        });
        // Set the action buttons
        builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog,int id){
            // Save the mSelectedItem to SharedPreferences


            }
        });
        builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog,int id){

            }
        });
        return builder.create();
    }


}
