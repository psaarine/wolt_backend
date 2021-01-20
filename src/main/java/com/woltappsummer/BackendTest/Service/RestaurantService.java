package com.woltappsummer.BackendTest.Service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {
    File input;


    public List<Restaurant> getRestaurants() throws IOException {
        Path fileName = Path.of(System.getProperty("user.dir") + "/" + "restaurants.json");
        String file = Files.readString(fileName);
        ArrayList<Restaurant> response = new ArrayList<Restaurant>();
        Restaurant[] restaurants = new Gson().fromJson(file, Restaurant[].class);
        List<Restaurant> asList = Arrays.asList(restaurants);
        return asList;
    }

    public List<Restaurant> getResponse() throws IOException {
        List<Restaurant> list = this.getRestaurants();
        JsonObject object = new JsonObject();

        ListContainer splitLists = this.splitList(list);


        return this.getPopular(splitLists.onlineList);
    }

    private List<Restaurant> getPopular(List<Restaurant> unsortedList){
        Collections.sort(unsortedList, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant restaurant, Restaurant t1) {
                return restaurant.getPopularity().compareTo(t1.getPopularity());
            }
        });
        Collections.reverse(unsortedList);

        return unsortedList;
    }

    private ListContainer splitList(List<Restaurant> rawList){
        ArrayList<Restaurant> onlineList = new ArrayList<Restaurant>();
        ArrayList<Restaurant> offlineList = new ArrayList<Restaurant>();

        for(Restaurant r: rawList){
            System.out.println(r.getName());
            if (r.getOnline()){
                onlineList.add(r);
            } else {
                offlineList.add(r);
            }
        }

        ListContainer cont = new ListContainer(onlineList, offlineList);

        return cont;
    }
    @AllArgsConstructor
    private class Section {
        String title;
        ArrayList<Restaurant> restaurants;
    }

    @AllArgsConstructor
    @Setter
    @Getter
    @ToString
    private class ListContainer {
        ArrayList<Restaurant> onlineList;
        ArrayList<Restaurant> offlineList;
    }
}
