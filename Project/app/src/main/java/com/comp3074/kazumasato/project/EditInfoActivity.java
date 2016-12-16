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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.comp3074.kazumasato.project.models.Flight;
import com.comp3074.kazumasato.project.models.User;
import com.comp3074.kazumasato.project.db.FlightDatabase;
import com.comp3074.kazumasato.project.db.UserDatabase;

import java.util.ArrayList;


public class EditInfoActivity extends AppCompatActivity {
    private User user;
    private UserDatabase userDatabase;
    private FlightDatabase flightDatabase;
    private ArrayList<User> clients;
    private ArrayList<Flight> flights;
    private RadioButton[] radBtns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        this.userDatabase = new UserDatabase(this);
        this.flightDatabase = new FlightDatabase(this);
        this.clients = userDatabase.getAllClients();
        this.flights = flightDatabase.getAllFlights();
        Intent intent = getIntent();
        this.user = (User) intent.getSerializableExtra("userKey");
        this.user = this.userDatabase.getUser(this.user.getEmail());
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

    
    public void viewClients(View view) {
        TextView resText = (TextView) findViewById(R.id.edit_info_result);
        if (this.clients.isEmpty()) {
            resText.setText("No clients are available for editing.");
        } else {
            RadioGroup radGrp = (RadioGroup) findViewById(R.id.view_all);
            radGrp.removeAllViews();
            this.radBtns = new RadioButton[this.clients.size()];
            for (int i = 0; i < this.clients.size(); i++) {
                this.radBtns[i] = new RadioButton(this);
                this.radBtns[i].setId(i);
                this.radBtns[i].setText(this.clients.get(i).toString());
                radGrp.addView(this.radBtns[i]);
            }
            radGrp.clearCheck();
            radGrp.check(this.radBtns[0].getId());
            Button editBtn = (Button) findViewById(R.id.edit_info);
            editBtn.setEnabled(true);
        }
    }

    
    public void viewFlights(View view) {
        TextView resText = (TextView) findViewById(R.id.edit_info_result);
        if (this.flights.isEmpty()) {
            resText.setText("No flights are available for editing.");
        } else {
            RadioGroup radGrp = (RadioGroup) findViewById(R.id.view_all);
            radGrp.removeAllViews();
            this.radBtns = new RadioButton[this.flights.size()];
            for (int i = 0; i < this.flights.size(); i++) {
                this.radBtns[i] = new RadioButton(this);
                this.radBtns[i].setId(i);
                this.radBtns[i].setText(this.flights.get(i).toString());
                radGrp.addView(this.radBtns[i]);
            }
            radGrp.clearCheck();
            radGrp.check(this.radBtns[0].getId());
            Button editBtn = (Button) findViewById(R.id.edit_info);
            editBtn.setEnabled(true);
        }
    }

    
    public void editInfo(View view) {
        RadioGroup radGrp = (RadioGroup) findViewById(R.id.view_all);
        RadioButton radClients = (RadioButton) findViewById(R.id.view_clients);
        if (radClients.isChecked()) {
            Intent newIntent = new Intent(this, ProfileActivity.class);
            int selected = radGrp.getCheckedRadioButtonId();
            User client = this.clients.get(selected);
            newIntent.putExtra("clientKey", client);
            newIntent.putExtra("userKey", this.user);
            startActivity(newIntent);
        } else {
            Intent newIntent = new Intent(this, EditFlightActivity.class);
            int selected = radGrp.getCheckedRadioButtonId();
            Flight flight = this.flights.get(selected);
            newIntent.putExtra("flightKey", flight);
            newIntent.putExtra("userKey", this.user);
            startActivity(newIntent);
        }
    }
}
