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
import android.view.View;
import android.widget.TextView;
import com.comp3074.kazumasato.project.models.Itinerary;
import com.comp3074.kazumasato.project.models.User;
import com.comp3074.kazumasato.project.db.FlightDatabase;
import com.comp3074.kazumasato.project.db.UserDatabase;


public class DetailedItineraryActivity extends AppCompatActivity {

    private Intent intent;
    private User user;
    private User clientUser;
    private UserDatabase userDatabase;
    private FlightDatabase flightDatabase;
    private Itinerary currBookedItin;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_detailed_itinerary);
        this.intent = getIntent();
        this.user = (User) this.intent.getSerializableExtra("userKey");
        this.flightDatabase = new FlightDatabase(this);
        this.userDatabase = new UserDatabase(this);
        this.user = this.userDatabase.getUser(this.user.getEmail());
        isAdmin = this.intent.hasExtra("clientKey");
        if (isAdmin) {
            this.clientUser = (User) this.intent.getSerializableExtra("clientKey");
            this.clientUser = this.userDatabase.getUser(this.clientUser.getEmail());
        }
        this.currBookedItin = (Itinerary) this.intent.getSerializableExtra("bookedItinKey");
        TextView originText = (TextView) findViewById(R.id.origin);
        originText.setText(this.currBookedItin.getStartOrigin());
        TextView destinationText = (TextView) findViewById(R.id.destination);
        destinationText.setText(this.currBookedItin.getFinalDestination());
        TextView totalTimeText = (TextView) findViewById(R.id.total_time);
        totalTimeText.setText(String.valueOf(this.currBookedItin.totalTravelTime()));
        TextView departureDateText = (TextView) findViewById(R.id.departure_date);
        departureDateText.setText(this.currBookedItin.getFirstDepartureDateTime());
        TextView arrivalDateText = (TextView) findViewById(R.id.arrival_date);
        arrivalDateText.setText(this.currBookedItin.getLastArrivalDateTime());
        TextView totalCostText = (TextView) findViewById(R.id.total_cost);
        totalCostText.setText(String.valueOf(this.currBookedItin.getTotalCost()));
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

    
    public void removeItinerary(View view) {
        Intent newIntent = new Intent(this, ViewBookedItineraryActivity.class);
        newIntent.putExtra("userKey", this.user);
        if (isAdmin) {
            this.clientUser.cancelItinerary(currBookedItin);
            this.userDatabase.save();
            newIntent.putExtra("clientKey", this.clientUser);
        } else {
            this.user.cancelItinerary(currBookedItin);
            this.userDatabase.save();
            this.flightDatabase.save();
        }
        startActivity(newIntent);
    }
}
