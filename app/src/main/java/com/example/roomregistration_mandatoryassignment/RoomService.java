package com.example.roomregistration_mandatoryassignment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RoomService {
    @GET("Rooms/{id}")
    Call<Room> getRoom(@Path("id") int id);

    @GET("Rooms")
    Call<List<Room>> getAllRooms();

    @GET("Rooms/free/{time}")
    Call<Room> getFreeRooms(@Path("time") int time);

    @GET("Rooms/name/{roomName}")
    Call<Room> getRoomByName(@Path("roomName") String roomName);
}
