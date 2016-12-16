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
import com.comp3074.kazumasato.project.models.User;
import com.comp3074.kazumasato.project.db.UserDatabase;


public class MainMenuActivity extends AppCompatActivity {
    private User user;
    private UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Intent intent = getIntent();
        this.userDatabase = new UserDatabase(this);
        this.user = (User) intent.getSerializableExtra("userKey");
        this.user = this.userDatabase.getUser(this.user.getEmail());
        String status = "";
        TextView statusText = (TextView) findViewById(R.id.user_status);
        status = String.format("%s, %s", this.user.getLastName(), this.user.getFirstName());
        if (this.user.isAdmin()) {
            status = "Admin: " + status;
        }
        statusText.setText(status);
        if (this.user.isAdmin()) {
            TextView uploadText = (TextView) findViewById(R.id.upload_main_text);
            TextView editText = (TextView) findViewById(R.id.edit_main_text);
            Button uploadBtn = (Button) findViewById(R.id.upload_main);
            Button editBtn = (Button) findViewById(R.id.edit_main);
            uploadText.setVisibility(View.VISIBLE);
            editText.setVisibility(View.VISIBLE);
            uploadBtn.setVisibility(View.VISIBLE);
            editBtn.setVisibility(View.VISIBLE);
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

    
    public void search(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("userKey", this.user);
        startActivity(intent);
    }

    
    public void profile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userKey", this.user);
        startActivity(intent);
    }

    
    public void upload(View view) {
        Intent intent = new Intent(this, UploadActivity.class);
        intent.putExtra("userKey", this.user);
        startActivity(intent);
    }

    
    public void editInfo(View view) {
        Intent intent = new Intent(this, EditInfoActivity.class);
        intent.putExtra("userKey", this.user);
        startActivity(intent);
    }
}
