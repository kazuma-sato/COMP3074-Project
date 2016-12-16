package com.comp3074.kazumasato.project;

// COMP3064  Group Assignment
// Due: Dec 12, 2016
// Due Extention: Thursday, December 15th, 2016
// Instructor: Ilir Dema
// Kazuma Sato 100 948 212 kazuma.sato@georgebrown.ca

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.util.ArrayList;
import com.comp3074.kazumasato.project.models.Flight;
import com.comp3074.kazumasato.project.models.User;


public class ViewFlightsActivity extends AppCompatActivity {
    private Intent intent;
    private ArrayList<Flight> flights;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_flights);
        this.intent = getIntent();
        this.flights = (ArrayList<Flight>) this.intent.getSerializableExtra("flightsKey");
        this.user = (User) this.intent.getSerializableExtra("userKey");
        int i = 1;
        String results = "";
        for (Flight flight : this.flights) {
            results += i + ". " + flight;
            i += 1;
        }
        TextView flightInfo = (TextView) findViewById(R.id.flight_info);
        flightInfo.setText(results);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_staple, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_home) {
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("userKey", this.user);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
