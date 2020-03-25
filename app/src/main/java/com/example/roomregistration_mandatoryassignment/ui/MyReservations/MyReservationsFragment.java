package com.example.roomregistration_mandatoryassignment.ui.MyReservations;

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
import com.example.roomregistration_mandatoryassignment.Reservation;
import com.example.roomregistration_mandatoryassignment.ReservationService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyReservationsFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyReservationsViewModel myReservationsViewModel = ViewModelProviders.of(this).get(MyReservationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_reservations, container, false);
        recyclerView = root.findViewById(R.id.myReservationsRecyclerView);
        myReservationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        getAndShowAllReservationsForUser();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAndShowAllReservationsForUser();
    }

    private void getAndShowAllReservationsForUser() {
        ReservationService reservationService = ApiUtils.getReservationService();
        Call<List<Reservation>> getAllReservationsCall = reservationService.getReservationsByUserId("mort237d@edu.easj.dk");
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

    private void populateRecyclerView(List<Reservation> allReservations) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerViewSimpleAdapter adapter = new RecyclerViewSimpleAdapter(getActivity(), allReservations);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


}
