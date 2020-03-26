package com.example.roomregistration_mandatoryassignment.data;

import com.example.roomregistration_mandatoryassignment.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            LoggedInUser fakeUser = new LoggedInUser(java.util.UUID.randomUUID().toString(),username.substring(0, username.length() - "@edu.easj.dk".length()));
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
    }
}
