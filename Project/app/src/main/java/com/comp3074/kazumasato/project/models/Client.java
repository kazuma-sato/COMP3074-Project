package com.comp3074.kazumasato.project.models;


public class Client extends User {

    public Client(String lastName, String firstName, String email, String password, String address,
                  String creditCardNumber, String expiryDate) {
        super(lastName, firstName, email, password, address, creditCardNumber, expiryDate, false);
    }
}
