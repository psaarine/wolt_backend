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
