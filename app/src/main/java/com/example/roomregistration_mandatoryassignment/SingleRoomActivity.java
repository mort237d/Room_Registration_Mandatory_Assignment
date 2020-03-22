package com.example.roomregistration_mandatoryassignment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleRoomActivity extends AppCompatActivity{

    public static final String ROOM = "ROOM";
    private static final String TAG = "SingleRoomTag";
    private Room originalRoom;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_room);

        recyclerView = findViewById(R.id.singleRoomRecyclerView);

        Intent intent = getIntent();
        originalRoom = (Room) intent.getSerializableExtra(ROOM);

        getSupportActionBar().setTitle(originalRoom.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getAndShowAllReservationsForRoom();
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

    private void getAndShowAllReservationsForRoom() {
        ReservationService reservationService = ApiUtils.getReservationService();
        Call<List<Reservation>> getAllReservationsCall = reservationService.getReservationsByRoomId(originalRoom.getId());
        getAllReservationsCall.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful()) {
                    List<Reservation> allReservations = response.body();
                    Log.d(TAG, allReservations.toString());

                    populateRecyclerView(allReservations);
                } else {
                    String message = "Problem " + response.code() + " " + response.message();
                    Log.d(TAG, message);
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public static List<Reservation> allReservations;

    private void getAndShowAllReservationsForDate() {
        ReservationService reservationService = ApiUtils.getReservationService();

        Call<List<Reservation>> getAllReservationsCall = reservationService.getReservationsByRoomIdAndFromTime(originalRoom.getId(), 1234); //TODO set real from time (unix)
        getAllReservationsCall.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful()) {
                    allReservations = response.body();
                    Log.d(TAG, allReservations.toString());

                    populateRecyclerView(allReservations);
                } else {
                    String message = "Problem " + response.code() + " " + response.message();
                    Log.d(TAG, message);
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public static RecyclerViewSimpleAdapter singleRoomAdapter;

    private void populateRecyclerView(List<Reservation> allReservations) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        singleRoomAdapter = new RecyclerViewSimpleAdapter(this, allReservations);
        recyclerView.setAdapter(singleRoomAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void Booked(View view) {
        ToggleButton toggleButton = findViewById(R.id.bookedToggleButton);
        Log.d(TAG, "Booked: " + view.isSelected());
    }

    public void DatePicker(View view) {
        Log.d(TAG, "DatePicker: ");
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");

        getAndShowAllReservationsForDate();
    }

    public void AddReservationFromRoomsIntent(View view) {
        Intent intent = new Intent(this, AddReservationActivity.class);
        intent.putExtra("EXTRA", originalRoom.getId());
        startActivity(intent);
    }
}
