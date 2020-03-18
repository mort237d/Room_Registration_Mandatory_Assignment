package com.example.roomregistration_mandatoryassignment.ui.MyReservations;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyReservationsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyReservationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}