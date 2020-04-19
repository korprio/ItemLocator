package com.example.itemlocator;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * This class is responsible for starting the application and finding which fragment to display based on which tab is clicked.
 */
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new HomeScreenFragment();
                    break;
                case R.id.navigation_search:
                    selectedFragment = new CreateProduct();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder,
                    selectedFragment).commit();

            return true;
        }
    };

    /**
     * This method is responsible for creating the main activity view and setting up the tabbed navigation bar.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder,
                new HomeScreenFragment()).commit();

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#d64035"));
        actionBar.setBackgroundDrawable(colorDrawable);


        //--------------------------------------------------------Notification Code--------------------------------------------------------//
        Intent intent = new Intent(MainActivity.this, FireNotification.class);
        PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 1000,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        //Schedule intent to run in the future
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                //Currently 10 seconds after app starts, would probable make it once a day or once a week, testing purposes keeping it short.
                System.currentTimeMillis() + 10000, pendingIntent);

        //--------------------------------------------------------End Notification Code-------------------------------------------------------//


        //--------------------------------------------------------GPS Permission Code--------------------------------------------------------//

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                Toast.makeText(this, "This app requires GPS functionality, finding your gps location will be used to pinpoint the location of the product in the store.",
                        Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1009);

                //TODO make asynchronous somehow


            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1009);
            }
        }
    }
}
