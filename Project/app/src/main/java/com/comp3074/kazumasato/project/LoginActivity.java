package com.comp3074.kazumasato.project;

// COMP3064  Group Assignment
// Due: Dec 12, 2016
// Due Extention: Thursday, December 15th, 2016
// Instructor: Ilir Dema
// Kazuma Sato 100 948 212 kazuma.sato@georgebrown.ca

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.comp3074.kazumasato.project.db.FlightDatabase;
import com.comp3074.kazumasato.project.db.UserDatabase;
import com.comp3074.kazumasato.project.models.Admin;
import com.comp3074.kazumasato.project.models.User;

public class LoginActivity extends AppCompatActivity {
    private FlightDatabase flightDatabase;
    private UserDatabase userDatabase;
    private int lineNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.flightDatabase = new FlightDatabase(this);
        this.userDatabase = new UserDatabase(this);
        this.addAdmins();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void authenticate(View view) {
        EditText userText = (EditText) findViewById(R.id.username);
        EditText passText = (EditText) findViewById(R.id.password);
        String email = userText.getText().toString();
        String password = passText.getText().toString();

        if (this.userDatabase.contains(email)) {
            User user = this.userDatabase.getUser(email);
            if(password.compareTo(user.getPassword()) == 0) {
                Intent intent = new Intent(this, MainMenuActivity.class);
                intent.putExtra("userKey", user);
                startActivity(intent);
            }
        } else {
            TextView resText = (TextView) findViewById(R.id.login_result);
            resText.setText("Username or password does not exist!");
        }
    }

    
    private boolean addAdmins() {

        boolean result = false;

        if (this.userDatabase.isEmpty()) {

            User adminUser = new User("admin","admin,","admin@admin.com","password","","","",true);
            User user = new User("user","user,","user@user.com","password","","","",false);
            this.userDatabase.addItem(adminUser);
            this.userDatabase.addItem(user);
            this.userDatabase.load();
            this.userDatabase.save();
            result = true;
        } else {
            result = false;
        }
        return result;
    }
}
