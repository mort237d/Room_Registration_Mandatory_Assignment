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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import static com.example.roomregistration_mandatoryassignment.ui.login.LoginActivity.mAuth;

public class AddReservationActivity extends AppCompatActivity {

    private static final String TAG = "AddReservationActivity";

    private Spinner buildingsListSpinner;
    private Spinner fromTimesListSpinner;
    private Spinner toTimesListSpinner;
    private Spinner roomsListSpinner;
    private EditText purposeEditText;
    private TextView currentDateTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);

        buildingsListSpinner = findViewById(R.id.spinnerBuilding);
        fromTimesListSpinner = findViewById(R.id.spinnerFromTime);
        toTimesListSpinner = findViewById(R.id.spinnerToTime);
        roomsListSpinner = findViewById(R.id.spinnerRoom);
        purposeEditText = findViewById(R.id.purposeEditText);
        currentDateTextview = findViewById(R.id.currentDateTextview);

        currentDateTextview.setText("Current date: " + staticYear + "-" + staticMonth + "-" + staticDayOfMonth); //TODO make year and so on the days date

        getSupportActionBar().setTitle("Add new reservation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                "Select a building...",
                "A",
                "B",
                "C",
                "D"
        };

        final List<String> buildingsList = new ArrayList<>(Arrays.asList(buildings));

        ArrayAdapterInit(buildingsListSpinner, buildingsList);

        getAndShowAllRoomsInSpinner();

        String[] times = new String[]{
                "Select a time...",
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
        ReservationService reservationService = ApiUtils.getReservationService();

        Reservation reservation = new Reservation();
        /*reservation.setFromTime(tsToSec8601("2020-03-22T00:" + fromTimesListSpinner.getSelectedItem().toString()));
        reservation.setToTime(tsToSec8601("2020-03-22T00:" + toTimesListSpinner.getSelectedItem().toString()));*/
        Log.d(TAG, "AddReservationClick: " + staticYear + "-" + staticMonth + "-" + staticDayOfMonth + "T00:" + fromTimesListSpinner.getSelectedItem().toString());
        reservation.setFromTime(tsToSec8601(staticYear + "-" + staticMonth + "-" + staticDayOfMonth + "T00:" + fromTimesListSpinner.getSelectedItem().toString()));
        reservation.setToTime(tsToSec8601(staticYear + "-" + staticMonth + "-" + staticDayOfMonth + "T00:" + toTimesListSpinner.getSelectedItem().toString()));
        reservation.setRoomId(roomIds.get(roomsListSpinner.getSelectedItem().toString()));
        Log.d(TAG, "AddReservationClick: " + roomIds.get(roomsListSpinner.getSelectedItem().toString()));
        reservation.setUserId(mAuth.getCurrentUser().getEmail());
        reservation.setPurpose(purposeEditText.getText().toString());

        Call<Reservation> postReservation = reservationService.postReservation(reservation);
        postReservation.enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.isSuccessful());
                    //allReservations.add(reservation); //TODO make it work..... https://stackoverflow.com/questions/31367599/how-to-update-recyclerview-adapter-data
                    //singleRoomAdapter.notifyItemInserted(allReservations.size());
                } else {
                    String message = "Problem " + response.code() + " " + response.message();
                    Log.w(TAG, message);
                }
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });

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

    public void TimePicker(View view) {
        Log.d(TAG, "TimePicker: ");
    }

    public void DatePicker(View view) {
        Log.d(TAG, "DatePicker: ");
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
