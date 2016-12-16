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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import java.util.ArrayList;
import com.comp3074.kazumasato.project.models.Flight;
import com.comp3074.kazumasato.project.models.Itinerary;
import com.comp3074.kazumasato.project.models.User;
import com.comp3074.kazumasato.project.db.FlightDatabase;
import com.comp3074.kazumasato.project.db.UserDatabase;


public class ViewItinerariesActivity extends AppCompatActivity {

    private Intent intent;
    private User user;
    private User client;
    private boolean isAdmin = false;
    private UserDatabase userDatabase;
    private FlightDatabase flightDatabase;
    private ArrayList<Itinerary> itineraries;
    private ArrayList<CheckBox> cboxes;
    private RelativeLayout myLayout;
    private Intent newIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.flightDatabase = new FlightDatabase(this);
        this.userDatabase = new UserDatabase(this);
        newIntent = new Intent(this, SearchActivity.class);
        this.intent = getIntent();
        this.user = (User) this.intent.getSerializableExtra("userKey");
        this.user = this.userDatabase.getUser(this.user.getEmail());
        if (intent.hasExtra("clientKey")) {
            this.isAdmin = true;
            this.client = (User) this.intent.getSerializableExtra("clientKey");
            this.client = this.userDatabase.getUser(this.client.getEmail());
            this.itineraries = this.client.getItineraryListAsList();
        } else {
            itineraries = user.getItineraryListAsList();
        }
        final ScrollView sv = new ScrollView(this);

        myLayout = new RelativeLayout(this);
        myLayout.setBackgroundResource(R.drawable.activitybg);
        sv.addView(myLayout);
        Spinner spinner = new Spinner(this);
        spinner.setId(0);
        ArrayList<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Sort");
        spinnerArray.add("by Cost");
        spinnerArray.add("by Time");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        RelativeLayout.LayoutParams spinnerParams =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        spinnerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        myLayout.addView(spinner, spinnerParams);
        cboxes = new ArrayList<>();
        for (int i = 0; i < itineraries.size(); i++) {
            CheckBox cbox = new CheckBox(this);
            cbox.setText(itineraries.get(i).toString());
            cbox.setId(i + 1);
            cboxes.add(cbox);
            RelativeLayout.LayoutParams buttonParams =
                    new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            if (i == 0) {
                buttonParams.addRule(RelativeLayout.BELOW, spinner.getId());
                buttonParams.setMargins(0, 50, 0, 0);
            }
            if (i != 0) {
                buttonParams.addRule(RelativeLayout.BELOW, cboxes.get(i - 1).getId());
                buttonParams.setMargins(0, 50, 0, 0);
            }
            myLayout.addView(cbox, buttonParams);

        }
        Button button = new Button(this);
        button.setText("Book Itineraries");
        button.setId(cboxes.size() + 1);
        RelativeLayout.LayoutParams buttonParams =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.addRule(RelativeLayout.BELOW, cboxes.get(cboxes.size() - 1).getId());
        buttonParams.setMargins(0, 50, 0, 0);
        myLayout.addView(button, buttonParams);

        setContentView(sv);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position,
                                       long myID) {
                String item = parentView.getItemAtPosition(position).toString();
                if (item == "by Cost") {
                    if (isAdmin) {
                        client.sortByCost();
                        itineraries = client.getItineraryListAsList();
                    } else {
                        user.sortByCost();
                        itineraries = user.getItineraryListAsList();
                    }

                    updateDisplay(itineraries);
                } else if (item == "by Time") {
                    if (isAdmin) {
                        client.sortByTime();
                        itineraries = client.getItineraryListAsList();
                    } else {
                        user.sortByTime();
                        itineraries = user.getItineraryListAsList();
                    }

                    updateDisplay(itineraries);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                for (CheckBox cbox : cboxes) {
                    if (cbox.isChecked()) {
                        if (isAdmin) {
                            client.bookItinerary(itineraries.get(count));
                        } else {
                            user.bookItinerary(itineraries.get(count));
                        }
                    }
                    count += 1;
                }
                flightDatabase.save();
                userDatabase.save();
                newIntent.putExtra("userKey", user);
                if (isAdmin) {
                    newIntent.putExtra("clientKey", client);
                }
                startActivity(newIntent);
            }
        });
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

    
    public void updateDisplay(ArrayList<Itinerary> itineraries) {
        int count = 0;
        for (CheckBox cbox : cboxes) {
            myLayout.removeView(cbox);
            cbox.setText(itineraries.get(count).toString());
            myLayout.addView(cbox);
            count += 1;
        }
    }
}
