package com.example.roomregistration_mandatoryassignment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.roomregistration_mandatoryassignment.DatePickerFragment.staticDayOfMonth;
import static com.example.roomregistration_mandatoryassignment.DatePickerFragment.staticMonth;
import static com.example.roomregistration_mandatoryassignment.DatePickerFragment.staticYear;
import static com.example.roomregistration_mandatoryassignment.MainActivity.currentUser;

public class AddReservationActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private Spinner buildingsListSpinner;
    private Spinner fromTimesListSpinner;
    private Spinner toTimesListSpinner;
    private Spinner roomsListSpinner;
    private EditText purposeEditText;
    public static TextView currentDateTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);

        final Calendar c = Calendar.getInstance();
        staticYear = c.get(Calendar.YEAR);
        staticMonth = c.get(Calendar.MONTH);
        staticDayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        buildingsListSpinner = findViewById(R.id.spinnerBuilding);
        fromTimesListSpinner = findViewById(R.id.spinnerFromTime);
        toTimesListSpinner = findViewById(R.id.spinnerToTime);
        roomsListSpinner = findViewById(R.id.spinnerRoom);
        purposeEditText = findViewById(R.id.purposeEditText);
        currentDateTextview = findViewById(R.id.currentDateTextview);

        currentDateTextview.setText(getResources().getString(R.string.current_date) + " " + staticYear + "-" + staticMonth + "-" + staticDayOfMonth);

        SpinnerInit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void SpinnerInit(){
        String[] buildings = new String[]{
                getResources().getString(R.string.select_building),
                "A",
                "B",
                "C",
                "D"
        };

        final List<String> buildingsList = new ArrayList<>(Arrays.asList(buildings));

        ArrayAdapterInit(buildingsListSpinner, buildingsList);

        getAndShowAllRoomsInSpinner();

        String[] times = new String[]{
                getResources().getString(R.string.select_time),
                "9:00",
                "10:00",
                "11:00",
                "12:00",
                "13:00",
                "14:00",
                "15:00"
        };

        final List<String> timesList = new ArrayList<>(Arrays.asList(times));

        ArrayAdapterInit(fromTimesListSpinner, timesList);
        ArrayAdapterInit(toTimesListSpinner, timesList);
    }

    private void ArrayAdapterInit(Spinner spinner, List<String> list) {
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, list){
            @Override
            public boolean isEnabled(int position){
                if(position == 0) return false;
                else return true;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if(position == 0) tv.setTextColor(Color.GRAY);
                else tv.setTextColor(Color.BLACK);

                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String selectedItemText = (String) parent.getItemAtPosition(position);
                //if(position > 0) Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    Map<String, Integer> roomIds = new HashMap<String, Integer>();

    private void getAndShowAllRoomsInSpinner() {
        RoomService roomService = ApiUtils.getRoomService();
        Call<List<Room>> getAllRoomsCall = roomService.getAllRooms();
        getAllRoomsCall.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful()) {
                    List<Room> allRooms = response.body();

                    List<String> list = new ArrayList<String>();

                    list.add("Select a room...");

                    for (Room r: allRooms) {
                        //if (r.getName().contains(buildingsListSpinner.getSelectedItem().toString()))
                        list.add(r.getName());

                        roomIds.put(r.getName(), r.getId());
                    }

                    ArrayAdapterInit(roomsListSpinner, list);

                    Intent intent = getIntent();
                    int roomId = intent.getIntExtra("EXTRA", 0);
                    Log.d(TAG, "onCreate: " + roomId);
                    roomsListSpinner.setSelection(roomId);
                    Log.d(TAG, "onCreate: " + roomsListSpinner.getSelectedItemId());
                } else {
                    String message = "Problem " + response.code() + " " + response.message();
                    Log.d(TAG, message);
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void AddReservationClick(View view) {
        if (fromTimesListSpinner.getSelectedItemId() == 0 || toTimesListSpinner.getSelectedItemId() == 0 || roomsListSpinner.getSelectedItemId() == 0){
            Snackbar.make(view, R.string.must_fullfill, Snackbar.LENGTH_SHORT).show();
        }
        else {
            ReservationService reservationService = ApiUtils.getReservationService();

            Reservation reservation = new Reservation(tsToSec8601(staticYear + "-" + staticMonth + "-" + staticDayOfMonth + "T00:" + fromTimesListSpinner.getSelectedItem().toString()),
                    tsToSec8601(staticYear + "-" + staticMonth + "-" + staticDayOfMonth + "T00:" + toTimesListSpinner.getSelectedItem().toString()),
                    currentUser.getEmail(),
                    purposeEditText.getText().toString(),
                    roomIds.get(roomsListSpinner.getSelectedItem().toString()));
        /*reservation.setFromTime(tsToSec8601(staticYear + "-" + staticMonth + "-" + staticDayOfMonth + "T00:" + fromTimesListSpinner.getSelectedItem().toString()));
        reservation.setToTime(tsToSec8601(staticYear + "-" + staticMonth + "-" + staticDayOfMonth + "T00:" + toTimesListSpinner.getSelectedItem().toString()));
        reservation.setRoomId(roomIds.get(roomsListSpinner.getSelectedItem().toString()));
        reservation.setUserId(currentUser.getEmail());
        reservation.setPurpose(purposeEditText.getText().toString());*/


            Call<Integer> postReservation = reservationService.postReservation(reservation);
            postReservation.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse: " + String.valueOf(response.isSuccessful()));
                        finishThisActivity();
                    } else {
                        String message = "Problem " + response.code() + " " + response.message();
                        Log.w(TAG, message);

                        if (fromTimesListSpinner.getSelectedItemId() >= toTimesListSpinner.getSelectedItemId()){
                            Snackbar.make(view, R.string.wrong_time_settings, Snackbar.LENGTH_SHORT).show();
                        }
                        else{
                            Snackbar.make(view, R.string.room_already_booked, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.e(TAG, "onFailure: ", t);
                }
            });
        }
    }

    private void finishThisActivity(){
        finish();
    }

    public static Integer tsToSec8601(String timestamp){
        if(timestamp == null) return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date dt = sdf.parse(timestamp);
            long epoch = dt.getTime();
            return (int)(epoch/1000);
        } catch(ParseException e) {
            return null;
        }
    }

    public void DatePicker(View view) {
        Log.d(TAG, "DatePicker: ");
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
