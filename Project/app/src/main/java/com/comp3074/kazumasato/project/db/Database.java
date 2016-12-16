package com.comp3074.kazumasato.project.db;

// COMP3064  Group Assignment
// Due: Dec 12, 2016
// Due Extention: Thursday, December 15th, 2016
// Instructor: Ilir Dema
// Kazuma Sato 100 948 212 kazuma.sato@georgebrown.ca

public interface Database<T> {
    
    void addItem(T item);
    boolean removeItem(T item);
    T getItem(int index);
    boolean contains(String identifier);
    int size();
    void save();
    void load();
}
