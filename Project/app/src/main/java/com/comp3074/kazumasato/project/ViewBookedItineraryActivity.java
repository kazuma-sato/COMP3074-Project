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
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import com.comp3074.kazumasato.project.models.Itinerary;
import com.comp3074.kazumasato.project.models.User;


public class ViewBookedItineraryActivity extends AppCompatActivity {

    private Intent intent;
    private User user;
    private User clientUser;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view_booked_itinerary);
        this.intent = getIntent();
        this.user = (User) this.intent.getSerializableExtra("userKey");
        isAdmin = this.intent.hasExtra("clientKey");
        if (isAdmin) {
            this.clientUser = (User) this.intent.getSerializableExtra("clientKey");
        }
        ArrayList<Itinerary> bookedItins = new ArrayList<>();
        if (isAdmin) {
            for (Itinerary strBookedItin : this.clientUser.getBookedItineraryListAsList()) {
                bookedItins.add(strBookedItin);
            }
        } else {
            for (Itinerary strBookedItin : this.user.getBookedItineraryListAsList()) {
                bookedItins.add(strBookedItin);
            }
        }
        ListAdapter bookAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookedItins);
        ListView bookItin = (ListView) findViewById(R.id.view_booked_itinerary);
        bookItin.setAdapter(bookAdapter);
        bookItin.setOnItemClickListener(onListClick);
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

    
    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
        private Itinerary currBookedItinerary;

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            System.out.println(parent.getItemAtPosition(position));
            this.currBookedItinerary = (Itinerary) parent.getItemAtPosition(position);
            Intent newIntent = new Intent(ViewBookedItineraryActivity.this,
                    DetailedItineraryActivity.class);
            newIntent.putExtra("userKey", user);
            newIntent.putExtra("bookedItinKey", currBookedItinerary);
            if (isAdmin) {
                newIntent.putExtra("clientKey", clientUser);
            }
            startActivity(newIntent);
        }
    };
}
