package com.example.roomregistration_mandatoryassignment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleRoomActivity extends AppCompatActivity {

    public static final String ROOM = "ROOM";
    private static final String LOG_TAG = "MYROOMS";
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

        Log.d(LOG_TAG, originalRoom.toString());
        TextView headingView = findViewById(R.id.singleRoomHeadingTextview);
        headingView.setText("Room: " + originalRoom.getName());

        getAndShowAllReservationsForRoom();
    }

    public void backButtonClicked(View view) {
        Log.d(LOG_TAG, "backButtonClicked");
        finish();
    }

    private void getAndShowAllReservationsForRoom() {
        ReservationService reservationService = ApiUtils.getReservationService();
        Call<List<Reservation>> getAllRoomsCall = reservationService.getAllReservations();
        getAllRoomsCall.enqueue(new Callback<List<Reservation>>() {
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

    private void populateRecyclerView(List<Reservation> allRooms) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewSimpleAdapter adapter = new RecyclerViewSimpleAdapter(this, allRooms);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
