package com.example.roomregistration_mandatoryassignment.ui.AllRooms;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AllRoomsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AllRoomsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}