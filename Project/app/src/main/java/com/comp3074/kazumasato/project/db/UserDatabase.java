package com.comp3074.kazumasato.project.db;

// COMP3064  Group Assignment
// Due: Dec 12, 2016
// Due Extention: Thursday, December 15th, 2016
// Instructor: Ilir Dema
// Kazuma Sato 100 948 212 kazuma.sato@georgebrown.ca

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.comp3074.kazumasato.project.models.*;


public class UserDatabase implements Database<User>, Serializable {

    private ArrayList<User> users;
    private Gson gson = new Gson();
    private Type userType = new TypeToken<ArrayList<User>>() {}.getType();
    private static Context context;

    private File getUsersFile() {

        File userData = context.getApplicationContext().getDir("databases", Context.MODE_PRIVATE);
        File usersFile = new File(userData, "users.json");
        return usersFile;
    }

    public UserDatabase() {

        this(context);
    }

    
    public UserDatabase(Context context) {

        UserDatabase.context = context;
        File usersFile = this.getUsersFile();

        if (!usersFile.exists()) {
            this.users = new ArrayList<>();
            this.save();
        } else {
            this.load();
        }
    }

    
    private int findUserIndex(User user) {

        int index = 0;

        if (this.users.isEmpty()) {
            index = -1;
        } else {
            for (User currUser : this.users) {
                if (user.getEmail().equals(currUser.getEmail())) {
                    break;
                } else {
                    index += 1;
                }
            }
        }

        if (index >= this.users.size()) {
            index = -1;
            return index;
        } else {
            return index;
        }
    }

    
    @Override
    public void addItem(User user) {

        int index = findUserIndex(user);

        if (index < 0) {
            this.users.add(user);
        } else {
            if (index < this.users.size()) {
                this.users.set(index, user);
            }
        }
    }

    
    @Override
    public boolean removeItem(User user) {

        int index = findUserIndex(user);

        if (index < 0) {
            return false;
        } else {
            this.users.remove(index);
            return true;
        }
    }

    
    @Override
    public User getItem(int index) {

        if (index < 0 || index >= this.users.size()) {
            return null;
        } else {
            return this.users.get(index);
        }
    }

    
    public User getUser(String email) {

        User user = null;

        for (User currUser : this.users) {
            if (email.equals(currUser.getEmail())) {
                user = currUser;
                return user;
            }
        }

        return null;
    }

    
    public ArrayList<User> getAllClients() {

        ArrayList<User> clients = new ArrayList<>();

        for (User user : this.users) {
            if (!user.isAdmin()) {
                clients.add(user);
            }
        }
        return clients;
    }

    
    public ArrayList<User> getAllAdmins() {

        ArrayList<User> admins = new ArrayList<>();

        for (User user : this.users) {
            if (user.isAdmin()) {
                admins.add(user);
            }
        }
        return admins;
    }

    
    @Override
    public boolean contains(String email) {

        boolean result = false;

        for (User user : this.users) {
            if (email.equals(user.getEmail())) {
                result = true;
                break;
            }
        }
        return result;
    }

    
    @Override
    public int size() {

        return this.users.size();
    }

    
    @Override
    public void save() {

        try {
            File usersFile = this.getUsersFile();

            FileWriter writer = new FileWriter(usersFile);
            gson.toJson(this.users, this.userType, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    @Override
    public void load() {

        try {
            File usersFile = this.getUsersFile();
            BufferedReader reader = new BufferedReader(new FileReader(usersFile));
            this.users = gson.fromJson(reader, userType);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public boolean isEmpty() {

        return this.users.isEmpty();
    }

    
    @Override
    public String toString() {
        
        String message = "[User Database]\n";
        for (User user : this.users) {
            message += user.toString() + "\n";
        }
        return message;
    }
}
