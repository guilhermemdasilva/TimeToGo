package com.guilermetell.timetogo;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
                    implements TimePickerDialog.OnTimeSetListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String id;

    public static TimePickerFragment newInstance(String param1, String param2) {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TimePickerFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        this.id = getArguments().getString("id");

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(id.equals(HomeActivity.SELECTED_HOUR)) {
            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putInt(HomeActivity.SELECTED_HOUR, hourOfDay).commit();
            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putInt(HomeActivity.SELECTED_MINUTE, minute).commit();
        } else if(id.equals(HomeActivity.SELECTED_WORK_HOUR)) {
            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putInt(HomeActivity.SELECTED_WORK_HOUR, hourOfDay).commit();
            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putInt(HomeActivity.SELECTED_WORK_MINUTE, minute).commit();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof TimePickerDialogCloseListener)
            ((TimePickerDialogCloseListener)activity).handleDialogClose(dialog);
    }
}
