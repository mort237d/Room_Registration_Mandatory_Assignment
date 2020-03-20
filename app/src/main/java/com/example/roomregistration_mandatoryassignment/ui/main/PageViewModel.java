package com.example.roomregistration_mandatoryassignment.ui.main;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.roomregistration_mandatoryassignment.ApiUtils;
import com.example.roomregistration_mandatoryassignment.Reservation;
import com.example.roomregistration_mandatoryassignment.ReservationService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PageViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return "Hello world from section: " + input;
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }

    private LiveData<List<Reservation>> mList = Transformations.map(mIndex, new Function<Integer, List<Reservation>>() {
        @Override
        public List<Reservation> apply(Integer input) {
            if (input == 1){
                //TODO list unbooked
                List<Reservation> list = new ArrayList<Reservation>();
                Reservation reservation = new Reservation();
                reservation.setId(1);
                reservation.setRoomId(1);
                reservation.setFromTime(123);
                reservation.setToTime(1234);
                reservation.setPurpose("hejsa1");
                list.add(reservation);
                return list;
            }
            else{
                return getAndShowAllReservationsForRoom();
            }
        }
    });

    public LiveData<List<Reservation>> getList() {
        return mList;
    }

    List<Reservation> allReservations;

    private List<Reservation> getAndShowAllReservationsForRoom() {
        allReservations = new ArrayList<Reservation>();
        ReservationService reservationService = ApiUtils.getReservationService();
        //Call<List<Reservation>> getAllReservationsCall = reservationService.getReservationsByRoomId(originalRoom.getId());
        Call<List<Reservation>> getAllReservationsCall = reservationService.getReservationsByRoomId(1);
        getAllReservationsCall.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful()) {
                    allReservations = response.body();
                    Log.d(TAG, "onResponse: " + allReservations);
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

        Log.d(TAG, "getAndShowAllReservationsForRoom: " + allReservations);

        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setRoomId(1);
        reservation.setFromTime(123);
        reservation.setToTime(1234);
        reservation.setPurpose("hejsa2");
        allReservations.add(reservation);

        return allReservations;
    }

}