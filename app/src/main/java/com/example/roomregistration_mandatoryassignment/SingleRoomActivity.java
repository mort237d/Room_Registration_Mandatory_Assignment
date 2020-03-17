package com.example.roomregistration_mandatoryassignment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SingleRoomActivity extends AppCompatActivity {

    public static final String ROOM = "ROOM";
    private static final String LOG_TAG = "MYROOMS";
    private Room originalRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_room);

        Intent intent = getIntent();
        originalRoom = (Room) intent.getSerializableExtra(ROOM);

        Log.d(LOG_TAG, originalRoom.toString());
        TextView headingView = findViewById(R.id.singleRoomHeadingTextview);
        headingView.setText("Room: " + originalRoom.getName());
    }

    public void backButtonClicked(View view) {
        Log.d(LOG_TAG, "backButtonClicked");
        finish();
    }
}
