package com.comp3074.kazumasato.project;

// COMP3064  Group Assignment
// Due: Dec 12, 2016
// Due Extention: Thursday, December 15th, 2016
// Instructor: Ilir Dema
// Kazuma Sato 100 948 212 kazuma.sato@georgebrown.ca

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import com.comp3074.kazumasato.project.models.Flight;
import com.comp3074.kazumasato.project.models.Itinerary;
import com.comp3074.kazumasato.project.models.User;
import com.comp3074.kazumasato.project.db.UserDatabase;


public class SearchActivity extends AppCompatActivity {
    private Intent intent;
    private User user;
    private User client;
    private boolean isAdmin = false;
    private UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.userDatabase = new UserDatabase(this);
        this.intent = getIntent();
        this.user = (User) this.intent.getSerializableExtra("userKey");
        this.user = this.userDatabase.getUser(this.user.getEmail());

        if (this.intent.hasExtra("clientKey")) {
            this.isAdmin = true;
            this.client = (User) intent.getSerializableExtra("clientKey");
            this.client = this.userDatabase.getUser(this.client.getEmail());
            Button profile = (Button) findViewById(R.id.profile_search);
            profile.setVisibility(View.VISIBLE);
        }

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

    
    public void searchFlights(View view) {
        EditText input = (EditText) findViewById(R.id.edit_departure_date);
        String departure_date = input.getText().toString();
        input = (EditText) findViewById(R.id.edit_origin);
        String origin = input.getText().toString();
        input = (EditText) findViewById(R.id.edit_destination);
        String destination = input.getText().toString();

        TextView resText = (TextView) findViewById(R.id.invalid);
        if (this.isValidDate(departure_date) && !departure_date.isEmpty() && !origin.isEmpty()
                && !destination.isEmpty()) {
            ArrayList<Flight> flights = User.searchFlights(origin, destination, departure_date);
            if (flights.isEmpty()) {
                resText.setText("No flights were found according to your search preferences.");
            } else {
                Intent newIntent = new Intent(this, ViewFlightsActivity.class);
                newIntent.putExtra("flightsKey", flights);
                newIntent.putExtra("userKey", this.user);
                startActivity(newIntent);
            }
        } else {
            resText.setText("You must enter all the required information or date given is invalid.");
        }
    }

    
    public void searchItineraries(View view) {
        boolean result;
        Intent newIntent = new Intent(this, ViewItinerariesActivity.class);
        if (isAdmin) {
            result = this.searchItin(this.client);
            newIntent.putExtra("clientKey", this.client);
        } else {
            result = this.searchItin(this.user);
        }

        if (result) {
            newIntent.putExtra("userKey", this.user);
            startActivity(newIntent);
        }
    }

    
    private boolean searchItin(User user) {
        EditText input = (EditText) findViewById(R.id.edit_departure_date);
        String departure_date = input.getText().toString();
        input = (EditText) findViewById(R.id.edit_origin);
        String origin = input.getText().toString();
        input = (EditText) findViewById(R.id.edit_destination);
        String destination = input.getText().toString();
        TextView resText = (TextView) findViewById(R.id.invalid);
        if (this.isValidDate(departure_date) && !departure_date.isEmpty() && !origin.isEmpty()
                && !destination.isEmpty()) {
            user.clearItineraries();
            user.getItineraries(departure_date, origin, destination);
            if (!user.getItineraryListAsList().isEmpty()) {
                this.userDatabase.save();
                return true;
            } else {
                resText.setText("No itineraries were found according to your search preferences.");
                return false;
            }
        } else {
            resText.setText("You must enter all the required information or date given is invalid.");
            return false;
        }
    }

    
    public void profile(View view) {
        Intent newIntent = new Intent(this, ProfileActivity.class);
        newIntent.putExtra("userKey", this.user);
        newIntent.putExtra("clientKey", this.client);
        startActivity(newIntent);
    }

    
    private boolean isValidDate(String date) {
        if (date.length() != 10) {
            return false;
        } else {
            String validChars = "1234567890";
            for (int i = 0; i < date.length(); i++) {
                if (i == 4 || i == 7) {
                    if (date.charAt(i) != '-') {
                        return false;
                    }
                } else {
                    if (!validChars.contains(Character.toString(date.charAt(i)))) {
                        return false;
                    }
                }
            }
        }
        int month = Integer.valueOf(date.split("-")[1]);
        int day = Integer.valueOf(date.split("-")[2]);
        if (month > 12 || month <= 0) {
            return false;
        }
        return !(day > 30 || day <= 0);
    }
}
