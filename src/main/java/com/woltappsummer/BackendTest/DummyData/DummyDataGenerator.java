package com.woltappsummer.BackendTest.DummyData;


import java.io.File;
import java.io.FileWriter;
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
                        this.generateDummyLocation(),
                        "restaurant-" + i,
                        generateDummyDate(),
                        this.random.nextBoolean(),
                        this.random.nextFloat()
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

    private Location generateDummyLocation(){
        /*

        Generates dummy distance from the center of Helsinki

         */

        double latitude = 60.166641;
        double longitude = 24.943537;
        return new Location(this.random.nextFloat(), this.random.nextFloat());
    }

    private Date generateDummyDate(){
        Calendar c = new GregorianCalendar();
        c.set(2010, 11, 30);
        return c.getTime();
    }
}
