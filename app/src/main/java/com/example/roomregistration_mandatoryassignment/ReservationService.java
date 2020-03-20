package com.example.roomregistration_mandatoryassignment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReservationService{
    @GET("Reservations")
    Call<List<Reservation>> getAllReservations();

    @POST("Reservations")
    @FormUrlEncoded
    Call<Reservation> postReservation(@Field("Id") int id, @Field("FromTime") int fromTime,
                                      @Field("ToTime") int toTime, @Field("UserId") String userId,
                                      @Field("Purpose") String purpose, @Field("RoomId") int roomId);

    @GET("Reservations/{id}")
    Call<Reservation> getReservation(@Path("id") int id);

    @DELETE("Reservations/{id}")
    Call<Void> deleteReservation(@Path("id") int id);

    @GET("Reservations/room/{roomId}")
    Call<List<Reservation>> getReservationsByRoomId(@Path("roomId") int roomId);

    @GET("Reservations/user/{userId}/{roomId}")
    Call<List<Reservation>> getReservationsByUserIdAndRoomId(@Path("userId") String userId,
                                                       @Path("roomId") int roomId);

    @GET("Reservations/room/{roomId}/{fromTime}/{toTime}")
    Call<List<Reservation>> getReservationsByUserIdAndFromTimeToTime(@Path("roomId") int roomId,
                                                               @Path("fromTime") int fromTime,
                                                               @Path("toTime") int toTime);

    @GET("Reservations/room/{roomId}/{fromTime}")
    Call<List<Reservation>> getReservationsByRoomIdAndFromTime(@Path("roomId") int roomId,
                                                         @Path("fromTime") int fromTime);

    @GET("Reservations/user/{userId}")
    Call<List<Reservation>> getReservationsByUserId(@Path("userId") String userId);

    @GET("Reservations/user/{userId}/{fromTime}")
    Call<List<Reservation>> getReservationsByUserIdAndFromTime(@Path("userId") String userId,
                                                         @Path("fromTime") int fromTime);
}
