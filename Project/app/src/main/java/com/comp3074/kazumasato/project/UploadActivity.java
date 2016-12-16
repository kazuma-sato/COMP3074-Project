package com.comp3074.kazumasato.project;

// COMP3064  Group Assignment
// Due: Dec 12, 2016
// Due Extention: Thursday, December 15th, 2016
// Instructor: Ilir Dema
// Kazuma Sato 100 948 212 kazuma.sato@georgebrown.ca

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import com.comp3074.kazumasato.project.models.Admin;
import com.comp3074.kazumasato.project.models.User;
import com.comp3074.kazumasato.project.db.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class UploadActivity extends AppCompatActivity {
    private User user;
    private UserDatabase userDatabase;
    private FlightDatabase flightDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Intent intent = getIntent();

        this.userDatabase = new UserDatabase(this);
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

    
    private boolean isValidClientFile(File file) {
        try {
            Scanner sc = new Scanner(file);
            String line = sc.nextLine();
            String[] info = line.split(",");
            return info.length == 6;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    private boolean isValidFlightFile(File file) {
        try {
            Scanner sc = new Scanner(file);
            String line = sc.nextLine();
            String[] info = line.split(",");
            return info.length == 8;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public void upload(View view) {
        RadioButton radClients = (RadioButton) findViewById(R.id.upload_clients);
        RadioButton radFlights = (RadioButton) findViewById(R.id.upload_flights);
        TextView resText = (TextView) findViewById(R.id.upload_result);
        EditText nameEdit = (EditText) findViewById(R.id.file_name);
        String fileName = nameEdit.getText().toString();
        File userData = this.getApplicationContext().getDir("uploads", Context.MODE_PRIVATE);
        File uploadFile = new File(userData, fileName);
        if (radClients.isChecked()) {
            if (uploadFile.exists()) {
                if (!isValidClientFile(uploadFile)) {
                    resText.setText("The file contents are not in a valid format.");
                } else {
                    resText.setText(Admin.uploadClients(uploadFile));
                }
            } else {
                resText.setText("The file was not found.");
            }
        } else if (radFlights.isChecked()) {
            if (uploadFile.exists()) {
                if (!isValidFlightFile(uploadFile)) {
                    resText.setText("The file contents are not in a valid format.");
                } else {
                    resText.setText(Admin.uploadFlights(uploadFile));
                }
            } else {
                resText.setText("The file was not found.");
            }
        } else {
            resText.setText("Please pick an upload option.");
        }
    }
}
