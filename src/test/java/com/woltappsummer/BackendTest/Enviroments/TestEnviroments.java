package com.woltappsummer.BackendTest.Enviroments;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.woltappsummer.BackendTest.Model.Location;
import com.woltappsummer.BackendTest.Model.Restaurant;

import java.util.*;

public class TestEnviroments {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public String basicTestingEnvironment(){
        /*

        This is the basic testing environment used in testing my application.
        Environment is designed to showcase some of the main aspects of this application

        - Shows that my application cuts off all those locations that are further than 1.5km
        from the location.

        - Shows that online restaurants are prioritised over offline.

        - Popular restaurants shows most popular restaurants first

        - New Restaurants shows newest first AND cuts off all restaurants older than 4 months

        - Nearby Restaurants shows closest first


         */

        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        int i;
        Restaurant restaurant;

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
                    returnDate(120),
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
                    returnDate(300),
                    true,
                    0.3f
            );
            restaurants.add(40 + i * 3, restaurant);
        }


        return gson.toJson(restaurants);
    }

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

    public Date returnDate(int i) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, i * -10);
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
