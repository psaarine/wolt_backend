package com.woltappsummer.BackendTest.Enviroments;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.woltappsummer.BackendTest.Model.Location;
import com.woltappsummer.BackendTest.Model.Restaurant;

import java.util.*;

public class TestEnviroments {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public String positionDerivedEnvironment(){

        /*

        This method returns string representing an environment in which there area 7 shops at coordinates 60, 60 and
        rest are at 0.0, 0.0

         */
        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        int i;
        Restaurant restaurant;

        for(i = 0; i < 7; i++){
            float popularity = (float) i/10;
            restaurant = new Restaurant(
                    "testi-close-" + i,
                    new Location(60.000, 60.000),
                    "testi-close-" + i,
                    new Date(System.currentTimeMillis()),
                    true,
                    popularity
            );
            restaurants.add(restaurant);
        }
        for(i = 7; i < 100; i++){
            restaurant = new Restaurant(
                    "testi-far-" + i,
                    new Location(00.000, 0.000),
                    "testi-far-" + i,
                    new Date(System.currentTimeMillis()),
                    true,
                    0.0f
            );
            restaurants.add(restaurant);
        }


        return gson.toJson(restaurants);
    }

    public String positionTestingEnvironment(){
        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        int i;
        Restaurant restaurant;

        for(i = 0; i < 90; i++){
            restaurant = new Restaurant(
                    "testhash",
                    new Location(0.01, 0.01),
                    "TestRestaurant-mismatch-" + i,
                    new Date(System.currentTimeMillis()),
                    true,
                    0.1f
            );
            restaurants.add(restaurant);
        }

        for(i = 0; i < 7; i++){
            double di = (double) i;
            double distance = di / 1000;
            restaurant = new Restaurant(
                    "testhash",
                    new Location(distance, distance),
                    "TestRestaurant-match-" + i,
                    new Date(System.currentTimeMillis()),
                    true,
                    0.1f
            );
            restaurants.add(i * 10, restaurant);
        }
        return gson.toJson(restaurants);
    }

    public String dateTestingEnvironment(){

        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        int i;
        Restaurant restaurant;

        for(i = 0; i < 80; i++){
            restaurant = new Restaurant(
                    "testhash",
                    new Location(0.001, 0.001),
                    "TestRestaurant-mismatch-" + i,
                    this.returnDate(300), // These dates will be cut off by function
                    true,
                    0.1f
            );
            restaurants.add(restaurant);
        }

        for(i = 0; i < 15; i++){

            restaurant = new Restaurant(
                    "testhash",
                    new Location(0.001, 0.001),
                    "TestRestaurant-match-" + i,
                   returnDate(i),
                    true,
                    0.1f
            );
            restaurants.add(i * 5, restaurant);
        }
        return gson.toJson(restaurants);
    }

    private Date returnDate(int i) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR, i * (-24));
        return calendar.getTime();
    }

    public List<Restaurant> getRestaurantList(int size, boolean isTrue){
        /*

        Returns list of dummy restaurants based on size and boolean

         */

        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        Restaurant restaurant;
        int i;

        for(i = 0; i < size; i++){
            restaurant = new Restaurant(
                    "dummyhash",
                    new Location(0.0, 0.0),
                    "Dummy restaurant",
                    new Date(System.currentTimeMillis()),
                    isTrue,
                    0.0f
                    );
            restaurants.add(restaurant);
        }
        return restaurants;
    }
}
