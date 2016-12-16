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
import android.widget.TextView;
import com.comp3074.kazumasato.project.models.User;
import com.comp3074.kazumasato.project.db.UserDatabase;
import java.util.ArrayList;


public class EditPersonalInfoActivity extends AppCompatActivity {
    private User user;
    private User client;
    private UserDatabase userDatabase;
    private ArrayList<User> userArr = new ArrayList<>();
    private int selected = 0;
    private boolean thereIsClient = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);
        this.userDatabase = new UserDatabase(this);
        Intent intent = getIntent();
        this.user = (User) intent.getSerializableExtra("userKey");
        this.user = this.userDatabase.getUser(this.user.getEmail());
        this.userArr.add(this.user);
        if (intent.hasExtra("clientKey")) {
            this.thereIsClient = true;
            this.selected = 1;
            this.client = (User) intent.getSerializableExtra("clientKey");
            this.client = this.userDatabase.getUser(this.client.getEmail());
            this.userArr.add(this.client);
            TextView resText = (TextView) findViewById(R.id.edit_personal_result);
            resText.setText(String.format("You are an Admin. You are about to edit %s %s personal info.",
                    this.client.getFirstName(), this.client.getLastName()));
        }
        EditText newText = (EditText) findViewById(R.id.edit_first_name);
        newText.setText(this.userArr.get(selected).getFirstName());
        newText = (EditText) findViewById(R.id.edit_last_name);
        newText.setText(this.userArr.get(selected).getLastName());
        newText = (EditText) findViewById(R.id.edit_email);
        newText.setText(this.userArr.get(selected).getEmail());
        newText = (EditText) findViewById(R.id.edit_address);
        newText.setText(this.userArr.get(selected).getAddress());
        newText = (EditText) findViewById(R.id.edit_credit_card_number);
        newText.setText(this.userArr.get(selected).getCreditCardNumber());
        newText = (EditText) findViewById(R.id.edit_expiry_date);
        newText.setText(this.userArr.get(selected).getExpiryDate());
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

    
    public void changeInfo(View view) {
        if (this.thereIsClient) {
            this.selected = 1;
        } else {
            this.selected = 0;
        }
        EditText editInfo = (EditText) findViewById(R.id.edit_first_name);
        String newInfo = editInfo.getText().toString();
        if (!newInfo.isEmpty()) {
            this.userArr.get(selected).setFirstName(newInfo);
        }
        editInfo = (EditText) findViewById(R.id.edit_last_name);
        newInfo = editInfo.getText().toString();
        if (!newInfo.isEmpty()) {
            this.userArr.get(selected).setLastName(newInfo);
        }
        editInfo = (EditText) findViewById(R.id.edit_email);
        newInfo = editInfo.getText().toString();
        if (!newInfo.isEmpty()) {
            this.userArr.get(selected).setEmail(newInfo);
        }
        editInfo = (EditText) findViewById(R.id.edit_address);
        newInfo = editInfo.getText().toString();
        if (!newInfo.isEmpty()) {
            this.userArr.get(selected).setAddress(newInfo);
        }
        editInfo = (EditText) findViewById(R.id.edit_credit_card_number);
        newInfo = editInfo.getText().toString();
        if (!newInfo.isEmpty()) {
            this.userArr.get(selected).setCreditCardNumber(newInfo);
        }
        editInfo = (EditText) findViewById(R.id.edit_expiry_date);
        newInfo = editInfo.getText().toString();
        if (!newInfo.isEmpty()) {
            this.userArr.get(selected).setExpiryDate(newInfo);
        }

        this.userDatabase.save();

        if (this.thereIsClient) {
            Intent intent = new Intent(this, EditInfoActivity.class);
            intent.putExtra("clientKey", this.client);
            intent.putExtra("userKey", this.user);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("userKey", this.user);
            startActivity(intent);
        }
    }
}
