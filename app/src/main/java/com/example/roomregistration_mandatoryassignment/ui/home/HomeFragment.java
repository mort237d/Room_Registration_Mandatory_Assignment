package com.example.roomregistration_mandatoryassignment.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomregistration_mandatoryassignment.ApiUtils;
import com.example.roomregistration_mandatoryassignment.R;
import com.example.roomregistration_mandatoryassignment.RecyclerViewSimpleAdapter;
import com.example.roomregistration_mandatoryassignment.Room;
import com.example.roomregistration_mandatoryassignment.RoomService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private View root;

    private final String TAG = "MYTAG";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        recyclerView = root.findViewById(R.id.mainRecyclerView);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        getAndShowAllRooms();

        return root;
    }

    private void getAndShowAllRooms() {
        RoomService roomService = ApiUtils.getRoomService();
        Call<List<Room>> getAllRoomsCall = roomService.getAllRooms();
        getAllRoomsCall.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful()) {
                    List<Room> allRooms = response.body();
                    Log.d(TAG, allRooms.toString());
                    populateRecyclerView(allRooms);
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

    private void populateRecyclerView(List<Room> allRooms) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerViewSimpleAdapter adapter = new RecyclerViewSimpleAdapter(getActivity(), allRooms);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void getAndShowData() {
        //EditText usernameView = findViewById(R.id.mainUsernameEditText);
        //String username = usernameView.getText().toString().trim();

        /*if (username.length() == 0) {
            usernameView.setError("No input");
            return;
        }*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://anbo-roomreservationv3.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RoomService roomService = retrofit.create(RoomService.class);

        Call<Room> roomCall = roomService.getRoom(1);
        roomCall.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                TextView messageView = root.findViewById(R.id.text_home);
                if (response.isSuccessful()) {
                    String message = response.message();
                    Room room = response.body();
                    Log.d(TAG, message + " " + room);
                    messageView.setText(room.getName());
                } else {
                    if (response.code() == 404) messageView.setText("No such room: " + response.code() + " : " + response.message());
                    else messageView.setText(String.format("Not working %d %s", response.code(), response.message()));
                }
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) { // network problems
                Log.e(TAG, t.getMessage());
            }
        });
    }
}
