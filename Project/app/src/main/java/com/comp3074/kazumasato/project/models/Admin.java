package com.comp3074.kazumasato.project.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Admin extends User {

    public Admin(String lastName, String firstName, String email, String password, String address,
                 String creditCardNumber, String expiryDate) {
        super(lastName, firstName, email, password, address, creditCardNumber, expiryDate, true);
    }

    public static String uploadClients(File file) {

        try {
            String uploaded = "Uploaded the following Clients:\n\n";
            Scanner sc = new Scanner(file);
            String client;
            String[] clientInfo;

            while (sc.hasNext()) {
                client = sc.nextLine();
                clientInfo = client.split(",");
                Client newClient = new Client(clientInfo[0], clientInfo[1], clientInfo[2],
                        clientInfo[3], clientInfo[4], clientInfo[5], clientInfo[6]);
                Admin.userDatabase.addItem(newClient);
                Admin.userDatabase.save();
                uploaded += newClient.toString() + "\n";
            }
            sc.close();
            return uploaded;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String uploadAdmins(File file) {

        try {
            String uploaded = "Uploaded the following Admins:\n\n";
            Scanner sc = new Scanner(file);
            String admin;
            String[] adminInfo;

            while (sc.hasNext()) {
                admin = sc.nextLine();
                adminInfo = admin.split(",");
                Admin newAdmin = new Admin(adminInfo[0], adminInfo[1], adminInfo[2],
                        adminInfo[3], adminInfo[4], adminInfo[5], adminInfo[6]);
                Admin.userDatabase.addItem(newAdmin);
                Admin.userDatabase.save();
                uploaded += newAdmin.toString() + "\n";
            }
            sc.close();
            return uploaded;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String uploadFlights(File file) {

        try {
            String uploaded = "Uploaded the following Flights:\n\n";
            Scanner sc = new Scanner(file);
            String flight;
            String[] flightInfo;

            while (sc.hasNext()) {
                flight = sc.nextLine();
                flightInfo = flight.split(",");
                Flight newFlight = new Flight(flightInfo[0], flightInfo[1], flightInfo[2],
                        flightInfo[3], flightInfo[4],
                        flightInfo[5], Double.parseDouble(flightInfo[6]),
                        Integer.parseInt(flightInfo[7]));
                Admin.flightDatabase.addItem(newFlight);
                Admin.flightDatabase.save();
                uploaded += newFlight.toString();
            }
            sc.close();
            return uploaded;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void removeClient(Client client) {

        Admin.userDatabase.removeItem(client);
        Admin.userDatabase.save();
    }

    public static void removeAdmin(Admin admin) {

        Admin.userDatabase.removeItem(admin);
        Admin.userDatabase.save();
    }

    public static Client getClient(String email) {

        return (Client) Admin.userDatabase.getUser(email);
    }

    public static Admin getAdmin(String email) {
        
        return (Admin) Admin.userDatabase.getUser(email);

    }
}
