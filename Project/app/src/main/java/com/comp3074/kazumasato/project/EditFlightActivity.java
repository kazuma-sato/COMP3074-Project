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
import android.widget.EditText;
import com.comp3074.kazumasato.project.models.Flight;
import com.comp3074.kazumasato.project.models.User;
import com.comp3074.kazumasato.project.db.FlightDatabase;


public class EditFlightActivity extends AppCompatActivity {
    private Flight flight;
    private FlightDatabase flightDatabase;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flight);
        Intent intent = getIntent();
        this.flightDatabase = new FlightDatabase(this);
        this.flight = (Flight) intent.getSerializableExtra("flightKey");
        this.user = (User) intent.getSerializableExtra("userKey");
        this.flight = this.flightDatabase.getFlight(this.flight.getFlightnum());
        EditText newText = (EditText) findViewById(R.id.edit_flight_number);
        newText.setText(this.flight.getFlightnum());
        newText = (EditText) findViewById(R.id.edit_departure_date_time);
        newText.setText(this.flight.getDepartureDateTime());
        newText = (EditText) findViewById(R.id.edit_arrival_date_time);
        newText.setText(this.flight.getArrivalDateTime());
        newText = (EditText) findViewById(R.id.edit_airline_name);
        newText.setText(this.flight.getAirline());
        newText = (EditText) findViewById(R.id.edit_origin);
        newText.setText(this.flight.getOrigin());
        newText = (EditText) findViewById(R.id.edit_destination);
        newText.setText(this.flight.getDestination());
        newText = (EditText) findViewById(R.id.edit_cost);
        newText.setText(String.valueOf((this.flight.getCost())));
        newText = (EditText) findViewById(R.id.edit_travel_time);
        newText.setText(String.valueOf(this.flight.getTraveltime()));
        newText = (EditText) findViewById(R.id.edit_seats_available);
        newText.setText(String.valueOf(this.flight.getNumSeats()));
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

    
    public void changeFlightInfo(View view) {
        EditText editInfo = (EditText) findViewById(R.id.edit_flight_number);
        String newInfo = editInfo.getText().toString();
        if (!newInfo.isEmpty()) {
            this.flight.setFlightnum(newInfo);
        }
        editInfo = (EditText) findViewById(R.id.edit_departure_date_time);
        newInfo = editInfo.getText().toString();
        if (!newInfo.isEmpty()) {
            this.flight.setDepartureDateTime(newInfo);
        }
        editInfo = (EditText) findViewById(R.id.edit_arrival_date_time);
        newInfo = editInfo.getText().toString();
        if (!newInfo.isEmpty()) {
            this.flight.setArrivalDateTime(newInfo);
        }
        editInfo = (EditText) findViewById(R.id.edit_airline_name);
        newInfo = editInfo.getText().toString();
        if (!newInfo.isEmpty()) {
            this.flight.setAirline(newInfo);
        }
        editInfo = (EditText) findViewById(R.id.edit_origin);
        newInfo = editInfo.getText().toString();
        if (!newInfo.isEmpty()) {
            this.flight.setOrigin(newInfo);
        }
        editInfo = (EditText) findViewById(R.id.edit_destination);
        newInfo = editInfo.getText().toString();
        if (!newInfo.isEmpty()) {
            this.flight.setDestination(newInfo);
        }
        editInfo = (EditText) findViewById(R.id.edit_cost);
        newInfo = editInfo.getText().toString();
        if (!newInfo.isEmpty()) {
            this.flight.setCost(Double.valueOf(newInfo));
        }
        editInfo = (EditText) findViewById(R.id.edit_travel_time);
        newInfo = editInfo.getText().toString();
        if (!newInfo.isEmpty()) {
            this.flight.setTraveltime(Double.parseDouble(newInfo));
        }
        editInfo = (EditText) findViewById(R.id.edit_seats_available);
        newInfo = editInfo.getText().toString();
        if (!newInfo.isEmpty()) {
            this.flight.setNumSeats(Integer.parseInt(newInfo));
        }

        this.flightDatabase.save();

        Intent newIntent = new Intent(this, EditInfoActivity.class);
        newIntent.putExtra("userKey", this.user);
        startActivity(newIntent);
    }
}
