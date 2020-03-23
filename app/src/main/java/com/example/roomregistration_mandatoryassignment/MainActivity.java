package com.example.roomregistration_mandatoryassignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;

import static com.example.roomregistration_mandatoryassignment.ui.login.LoginActivity.mAuth;

public class MainActivity extends AppCompatActivity {

    private static final String EMAIL = "EMAIL";
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.book_room, R.id.my_reservations).setDrawerLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        currentUser = mAuth.getCurrentUser();
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
        email = email.substring(email.indexOf('@'));
        nav_header_title.setText(email);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void MenuItemSignOut(MenuItem item) {
        mAuth.signOut();
        finish();
    }

    public static FirebaseUser currentUser;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.signOut();
    }

    public void AddReservationIntent(View view) {
        Intent intent = new Intent(this, AddReservationActivity.class);
        startActivity(intent);
    }
}
