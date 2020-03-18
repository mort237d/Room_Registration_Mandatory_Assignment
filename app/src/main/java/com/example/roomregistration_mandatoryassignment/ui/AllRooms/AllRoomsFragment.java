package com.example.roomregistration_mandatoryassignment.ui.AllRooms;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class AllRoomsFragment extends Fragment {

    private AllRoomsViewModel allRoomsViewModel;
    private RecyclerView recyclerView;
    private View root;

    private final String TAG = "MYTAG";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        allRoomsViewModel = ViewModelProviders.of(this).get(AllRoomsViewModel.class);
        root = inflater.inflate(R.layout.fragment_all_rooms, container, false);
        recyclerView = root.findViewById(R.id.mainRecyclerView);
        allRoomsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
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

                    // Fill to test scroll [START]
                    allRooms.add(new Room());
                    allRooms.add(new Room());
                    allRooms.add(new Room());
                    allRooms.add(new Room());
                    allRooms.add(new Room());
                    allRooms.add(new Room());
                    allRooms.add(new Room());
                    allRooms.add(new Room());
                    // Fill to test scroll [END]

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
}
