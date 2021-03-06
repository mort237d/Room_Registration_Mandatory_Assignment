package com.example.roomregistration_mandatoryassignment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import static com.example.roomregistration_mandatoryassignment.AddReservationActivity.currentDateTextview;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public static int staticYear;
    public static int staticMonth;
    public static int staticDayOfMonth;

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.staticYear = year;
        this.staticMonth = month;
        this.staticDayOfMonth = dayOfMonth;

        currentDateTextview.setText(getResources().getString(R.string.current_date) + " " + staticYear + "-" + staticMonth + "-" + staticDayOfMonth);
    }
}
