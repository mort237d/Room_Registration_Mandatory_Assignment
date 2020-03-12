package com.example.roomregistration_mandatoryassignment;

public class ApiUtils {
    private ApiUtils() {
    }

    private static final String BASE_URL = "http://anbo-restserviceproviderbooks.azurewebsites.net/Service1.svc/";

    public static RoomService getRoomService() {

        return RetrofitClient.getClient(BASE_URL).create(RoomService.class);
    }
}
