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
import android.widget.TextView;
import com.comp3074.kazumasato.project.models.Admin;
import com.comp3074.kazumasato.project.models.Client;
import com.comp3074.kazumasato.project.models.User;
import com.comp3074.kazumasato.project.db.UserDatabase;


public class ProfileActivity extends AppCompatActivity {

    private User user;
    private User clientUser;
    private boolean isAdmin;
    private UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_profile);
        this.userDatabase = new UserDatabase(this);
        Intent intent = getIntent();
        this.user = (User) intent.getSerializableExtra("userKey");
        this.user = this.userDatabase.getUser(this.user.getEmail());
        this.isAdmin = intent.hasExtra("clientKey");
        if (isAdmin) {
            this.clientUser = (User) intent.getSerializableExtra("clientKey");
            this.clientUser = this.userDatabase.getUser(this.clientUser.getEmail());
            TextView userInfo = (TextView) findViewById(R.id.user_info);
            userInfo.setText(this.clientUser.toString());
            Button searchButton = (Button) findViewById(R.id.search_button);
            searchButton.setVisibility(View.VISIBLE);
            TextView resText = (TextView) findViewById(R.id.profile_result);
            resText.setText("You are an admin viewing a client's profile.");
        } else {
            TextView userInfo = (TextView) findViewById(R.id.user_info);
            userInfo.setText(this.user.toString());
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

    
    public void viewBookedItinerary(View view) {
        Intent newIntent = new Intent(this, ViewBookedItineraryActivity.class);
        newIntent.putExtra("userKey", this.user);
        if (isAdmin) {
            newIntent.putExtra("clientKey", this.clientUser);
        }
        startActivity(newIntent);
    }

    
    public void editPersonalInfo(View view) {
        Intent newIntent = new Intent(this, EditPersonalInfoActivity.class);
        newIntent.putExtra("userKey", this.user);
        if (isAdmin) {
            newIntent.putExtra("clientKey", this.clientUser);
        }
        startActivity(newIntent);
    }

    
    public void search(View view) {
        Intent newIntent = new Intent(this, SearchActivity.class);
        newIntent.putExtra("userKey", this.user);
        newIntent.putExtra("clientKey", this.clientUser);
        startActivity(newIntent);
    }
}
