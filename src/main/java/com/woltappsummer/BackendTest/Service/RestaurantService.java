package com.woltappsummer.BackendTest.Service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.woltappsummer.BackendTest.Model.Location;
import com.woltappsummer.BackendTest.Model.Restaurant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class RestaurantService {
    File input;

    @Value("${my-properties.filename}")
    private String filename;


    public ResponseObject getResponse(float lat, float lon) throws IOException {

        /*

        This function takes in latitude and longitude coordinates and returns Response object that represents
        response.

         */
        List<Restaurant> list = this.getRestaurants(lat, lon);
        ResponseObject resp = new ResponseObject();
        ListContainer splitLists = this.splitList(list);

        List<Restaurant> testRestaurants = new ArrayList<Restaurant>();
        List<Restaurant> nearbyRestaurants = this.getNearby(
                splitLists.onlineList,
                splitLists.offlineList,
                lat,
                lon
                );
        nearbyRestaurants = new ArrayList<Restaurant>(nearbyRestaurants);

        Section popular = new Section("Popular Restaurants", this.getPopular(splitLists.onlineList, splitLists.offlineList));
        Section newRestaurants = new Section("New Restaurants", testRestaurants);
        Section nearby = new Section("Nearby Restaurants", nearbyRestaurants);

        resp.addSection(popular);
        resp.addSection(newRestaurants);
        resp.addSection(nearby);

        return resp;
    }


    public List<Restaurant> getRestaurants(
            float lat,
            float lon
    ) throws IOException {

        /*

        This function takes in raw List of restaurants from the restaurants.json file and
        removes all restaurants that arent within 1.5k of given coordinates.

         */
        Path fileName = Path.of(System.getProperty("user.dir") + "/" + this.filename);
        String file = Files.readString(fileName);
        ArrayList<Restaurant> response = new ArrayList<Restaurant>();
        Restaurant[] restaurants = new Gson().fromJson(file, Restaurant[].class);
        List<Restaurant> asList = Arrays.asList(restaurants);
        for(Restaurant r: asList){
            if(this.validateDistance(r.getLocation(), lat, lon)){
                response.add(r);
            }
        }
        return response;
    }

    public List<Restaurant> getPopular(List<Restaurant> unsortedList, List<Restaurant> unsortedOfflineList){
        List<Restaurant> respList = new ArrayList<Restaurant>();
        Collections.sort(unsortedList, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant restaurant, Restaurant t1) {
                return restaurant.getPopularity().compareTo(t1.getPopularity());
            }
        });
        Collections.reverse(unsortedList);

        if(unsortedList.size() > 10){
            respList = unsortedList.subList(0, 10);
        } else {
            respList = unsortedList.subList(0, unsortedList.size());
            Collections.sort(unsortedOfflineList, new Comparator<Restaurant>() {
                @Override
                public int compare(Restaurant restaurant, Restaurant t1) {
                    return restaurant.getPopularity().compareTo(t1.getPopularity());
                }
            });
            Collections.reverse(unsortedOfflineList);
            respList.addAll(unsortedOfflineList);
            if(respList.size() > 10){
                respList = respList.subList(0, 10);
            }
        }


        return respList;
    }

    public List<Restaurant> getNearby(List<Restaurant> unsortedList, List<Restaurant> unsortedOfflineList, float lat, float lon){


        List<Restaurant> arranged = arrangeClosest(unsortedList, lat, lon);

        if (arranged.size() > 10){
            arranged = arranged.subList(0, 10);
        } else {
            arranged = arranged.subList(0, arranged.size());
            List<Restaurant> arrangedOfflineList = arrangeClosest(unsortedOfflineList, lat, lon);
            arranged.addAll(arrangedOfflineList);
            if (arranged.size() > 10) {
                arranged = arranged.subList(0, 10);
            }
        }

        return arranged;
    }

    public List<Restaurant> getNewest(List<Restaurant> unsortedList, List<Restaurant> unsortedOfflineList) {
        List<Restaurant> arranged = new ArrayList<Restaurant>();

        Collections.sort(unsortedList, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant restaurant, Restaurant t1) {
                return restaurant.getLaunch_date().compareTo(t1.getLaunch_date());
            }
        });

        return unsortedList;
    }


    private ListContainer splitList(List<Restaurant> rawList){
        ArrayList<Restaurant> onlineList = new ArrayList<Restaurant>();
        ArrayList<Restaurant> offlineList = new ArrayList<Restaurant>();

        for(Restaurant r: rawList){
            if (r.getOnline()){
                onlineList.add(r);
            } else {
                offlineList.add(r);
            }
        }

        ListContainer cont = new ListContainer(onlineList, offlineList);

        return cont;
    }


    public boolean validateDistance(Location location, float lat, float lon){

        /*

        Takes location and coordinates and returns true if location is within 1.5km
        from coordinates. Returns false otherwise.

         */

        double distance = calculateDistance(location.getLatitude(), location.getLongitude(), lat, lon);

        if (distance > 1.5) {

            return false;
        }

        return true;
    }

    public List<Restaurant> arrangeClosest(List<Restaurant> list, float lat, float lon){

        Collections.sort(list, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant restaurant, Restaurant t1) {
                double d1 = calculateDistance(lat, lon, restaurant.getLocation().getLatitude(), restaurant.getLocation().getLongitude());
                double d2 = calculateDistance(lat, lon, t1.getLocation().getLatitude(), t1.getLocation().getLongitude());

                if (d1 > d2){
                    return -1;
                }
                return 1;
            }
        });
        Collections.reverse(list);

        return list;
    }

    public double calculateDistance(double leveys1, double pituus1, double leveys2, double pituus2){

        /*

        Source of this function is https://gist.github.com/vananth22/888ed9a22105670e7a4092bdcf0d72e4
        done by vananth22

         */

        final int R = 6371;
        double latDistance = toRad(leveys2 - leveys1);
        double lonDistance = toRad(pituus2 - pituus1);

        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRad(leveys1)) * Math.cos(toRad(leveys2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        Double distance = R * c;

        return distance;
    }

    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public class Section {
        String title;
        List<Restaurant> restaurants;
    }

    @AllArgsConstructor
    @Setter
    @Getter
    @NoArgsConstructor
    public class ResponseObject {
        private List<Section> sections = new ArrayList<>();

        private void addSection(Section section){
            this.sections.add(section);
        }
    }

    @AllArgsConstructor
    @Setter
    @Getter
    @ToString
    public class ListContainer {
        ArrayList<Restaurant> onlineList;
        ArrayList<Restaurant> offlineList;
    }


}
