package com.woltappsummer.BackendTest.DummyData;


import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.woltappsummer.BackendTest.Model.Location;
import com.woltappsummer.BackendTest.Model.Restaurant;

public class DummyDataGenerator {

    private  final String filename;
    private final Random random = new Random();
    private final Date date = new Date();
    private final int minYear = 2010;

    public DummyDataGenerator(String filename) {
        this.filename = filename;
    }

    public void generateDummyJson(){
        try {
            FileWriter writer = new FileWriter(this.filename);
            int i;
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Restaurant restaurant;
            List list = new ArrayList();


            for(i = 0; i < 100; i++){
                restaurant = new Restaurant(
                        "hash-" + i,
                        this.generateDummyLocation(i),
                        "restaurant-" + i,
                        generateDummyDate(i),
                        returnBoolean(i),
                        returnPopularity(i)
                );
                list.add(restaurant);
            }
            writer.write(gson.toJson(list));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Dummy Generated");
    }

    public void generateAdvancedDummy(){
        try {
            FileWriter writer = new FileWriter(this.filename);
            int i;
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Restaurant restaurant;
            List list = new ArrayList();


            List<Restaurant> restaurants = new ArrayList<Restaurant>();

            for(i = 0; i < 40; i++){

            /*

            These will be cut off by the 1.5 km limit.
            These are designed so that they would be PERFECT if it wasnt for the limit.

             */

                restaurant = new Restaurant(
                        "testhash",
                        new Location(0.01, 0.01),
                        "TestRestaurant-far-mismatch-" + i,
                        new Date(System.currentTimeMillis()),
                        true,
                        0.999f
                );
                restaurants.add(restaurant);
            }

            for(i = 0; i < 40; i++){

            /*

            These will be taken in by the 1.5 km limit.
            These are designed so that they are PERFECT but only offline

             */

                restaurant = new Restaurant(
                        "testhash",
                        new Location(0.001, 0.001),
                        "TestRestaurant-close-offline-" + i,
                        new Date(System.currentTimeMillis()),
                        false,
                        0.999f
                );
                restaurants.add(restaurant);
            }

            for (i = 0; i < 3; i++){

            /*

            These are tree most popular restaurants that are supposed to be shown in the beginning of
            "Popular Restaurants" -section. They are scattered between positions 20 and 40

             */

                double distance = (double) i / 1000;

                restaurant = new Restaurant(
                        "testhash",
                        new Location(0.001 + distance, 0.001 + distance),
                        "TestRestaurant-match-popular-" + i,
                        returnDate(20),
                        true,
                        0.9f + (float) distance
                );
                restaurants.add(20 + i * 3, restaurant);


            }

            for (i = 0; i < 3; i++){

            /*

            These are tree most recent restaurants that are supposed to be shown in the beginning of
            "New Restaurants" -section. They are scattered between positions 0 and 20

             */

                double distance = (double) i / 1000;

                restaurant = new Restaurant(
                        "testhash",
                        new Location(0.002 + distance, 0.002 + distance),
                        "TestRestaurant-match-new-" + i,
                        returnDate(i),
                        true,
                        0.3f
                );
                restaurants.add(40 + i * 3, restaurant);


            }

            for (i = 0; i < 3; i++){

            /*

            These tree are there to be in the beginning of the
            nearest restaurants -list

             */
                double distance = (double) i / 10000;

                restaurant = new Restaurant(
                        "testhash",
                        new Location(0.0001 + distance, 0.0001 + distance),
                        "TestRestaurant-match-near" + i,
                        returnDate(30),
                        true,
                        0.3f
                );
                restaurants.add(40 + i * 3, restaurant);
            }
            writer.write(gson.toJson(restaurants));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Date returnDate(int i) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR, i * (-24));
        return calendar.getTime();
    }

    private Location generateDummyLocation(int i){
        /*

        Generates dummy distance the point 0.0.
        Designed so that it will cut off some of the "restaurants" in the middle of the sea.

         */
        DecimalFormat formatter = new DecimalFormat("0.00000");
        double latitude = 0.0;
        double longitude = Math.round(0.0002 * i * 10000.0 ) / 10000.0;
        return new Location(latitude, longitude);
    }

    private Date generateDummyDate(int i){
        Calendar c = new GregorianCalendar();
        int days = (int) Math.round(2.4 * i);
        c.add(Calendar.DAY_OF_MONTH, days);
        return c.getTime();
    }

    private boolean returnBoolean(int i){
        int jaannos = i % 2;
        if (jaannos == 0) {
            return true;
        } else {
            return false;
        }
    }

    private float returnPopularity(int i){
        return (float) ((float) i * 0.01);
    }
}
