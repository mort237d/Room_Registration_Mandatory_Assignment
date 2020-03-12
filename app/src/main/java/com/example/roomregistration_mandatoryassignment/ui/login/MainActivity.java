package com.example.roomregistration_mandatoryassignment.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.roomregistration_mandatoryassignment.ApiUtils;
import com.example.roomregistration_mandatoryassignment.R;
import com.example.roomregistration_mandatoryassignment.Room;
import com.example.roomregistration_mandatoryassignment.RoomService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.roomregistration_mandatoryassignment.ui.login.LoginActivity.EMAIL;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private final String TAG = "MYTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();}
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_gallery).setDrawerLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        Intent intent = getIntent();
        TextView nav_header_subtitle = findViewById(R.id.nav_header_subtitle);
        String email = intent.getStringExtra(EMAIL);
        nav_header_subtitle.setText(email);

        TextView nav_header_title = findViewById(R.id.nav_header_title);
        email = email.substring(0, email.length() - "@edu.easj.dk".length());
        nav_header_title.setText(email);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void SettingsClick(MenuItem item) {
        Toast toast = new Toast(this);
        toast.makeText(this, "Settings!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //getAndShowData();
        getAndShowAllRooms();
    }

    private void getAndShowAllRooms() {//TODO Try this!!!
        RoomService roomService = ApiUtils.getRoomService();
        Call<List<Room>> getAllRoomsCall = roomService.getAllRooms();
        getAllRoomsCall.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful()) {
                    List<Room> allBooks = response.body();
                    Log.d(TAG, allBooks.toString());
                    //populateRecyclerView(allBooks);
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
                TextView messageView = findViewById(R.id.text_home);
                if (response.isSuccessful()) {
                    String message = response.message();
                    Room room = response.body();
                    Log.d(TAG, message + " " + room);
                    messageView.setText(room.getName());
                } else { // response code not 2xx
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
