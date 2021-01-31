package com.woltappsummer.BackendTest.Controller;


import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.woltappsummer.BackendTest.Enviroments.TestEnviroments;
import com.woltappsummer.BackendTest.Model.Location;
import com.woltappsummer.BackendTest.Model.Restaurant;
import com.woltappsummer.BackendTest.Service.RestaurantService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

@AutoConfigureMockMvc
@SpringBootTest(properties = {
        "my-properties.filename=testfile.json"
})
public class BasicTesting {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RestaurantService service;

    private final String filepath = System.getProperty("user.dir") + "/" + "testfile.json";
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    TestEnviroments env = new TestEnviroments();
    private final String url = "http://localhost:8080/discovery?lat=00.000&lon=00.000";

    @BeforeEach
    public void setUp() throws Exception {
        File file = new File(this.filepath);
        file.createNewFile();
        try {
            FileWriter writer = new FileWriter(filepath);
            writer.write(env.basicTestingEnvironment());
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @AfterEach
    public void tearDown() {
        File file = new File(this.filepath);
        file.delete();
    }

    @Test
    public void testTests(){

        // asserts that autowire works

        assertNotEquals(this.mockMvc, null);
        assertNotEquals(this.service, null);
    }

    @Test
    public void testEndpoint() throws Exception{

        MvcResult result = this.mockMvc.perform(get(this.url)).andReturn();
        String asString = result.getResponse().getContentAsString();
        RestaurantService.ResponseObject obj = gson.fromJson(asString, RestaurantService.ResponseObject.class);
        int i;

        List<RestaurantService.Section> sections = obj.getSections();
        assertEquals(sections.size(), 3);

        RestaurantService.Section popular = sections.get(0);
        RestaurantService.Section newRestaurants = sections.get(1);
        RestaurantService.Section nearby = sections.get(2);


        assertEquals("Popular Restaurants", popular.getTitle());
        assertEquals("New Restaurants", newRestaurants.getTitle());
        assertEquals("Nearby Restaurants", nearby.getTitle());

        List<Restaurant> popularRestaurantsList = popular.getRestaurants();
        List<Restaurant> newRestaurantsList = newRestaurants.getRestaurants();
        List<Restaurant> nearbyRestaurantsList = nearby.getRestaurants();


        /*

        This section tests that Popular Restaurants -section conforms to the criteria defined here:

        https://github.com/woltapp/summer2021-internship

        Asserts that:

        1. All locations are within 1.5km of point 0.0

        2. All three dummies are found by the sorting mechanism

         */


        /*

        This section tests popular restaurants section.
        Asserts that all 3 popular dummies are found by the algorithms

         */

        Location location;
        Restaurant restaurant;
        Restaurant restaurant_comp;
        int change;

        // Asserts that no restaurants further than 1.5 km get a pass
        for (Restaurant r: popularRestaurantsList) {
            location = r.getLocation();
            double distance = service.calculateDistance(0.0, 0.0, location.getLatitude(), location.getLongitude());

            assert(distance < 1.5);
        }

        // Asserts that tree main restaurants are at the top of the list, greatest popularity first
        for (i = 0; i < 7; i++) {
            restaurant = popularRestaurantsList.get(i);
            restaurant_comp = popularRestaurantsList.get(i + 1);

            change = restaurant.getPopularity().compareTo(restaurant_comp.getPopularity());
            assert(change >= 0);
        }

        // there are only 9 online retaurants so last one is supposed to be offline
        restaurant = popularRestaurantsList.get(9);
        assertFalse(restaurant.getOnline());
        assertEquals(10, popularRestaurantsList.size());

        /*

        This part tests new restaurants section and asserts that three most recent restaurants are at the top

         */

        // Asserts that no restaurants further than 1.5 km get a pass
        for (Restaurant r: newRestaurantsList) {
            location = r.getLocation();
            double distance = service.calculateDistance(0.0, 0.0, location.getLatitude(), location.getLongitude());
            assert(distance < 1.5);
        }

        for (i = 0; i < 2; i++) {

            // asserts that 3 first items are in right order.

            restaurant = newRestaurantsList.get(i);
            restaurant_comp = newRestaurantsList.get(i + 1);
            change = restaurant.getLaunch_date().compareTo(restaurant_comp.getLaunch_date());
            assert(change >= 0);
        }

        for (i = 0; i < 3; i++) {
            restaurant = newRestaurantsList.get(i);
            assertTrue(restaurant.getName().contains("new"));
        }

        for (i = 3; i < 9; i++) {

            // Asserts that program cuts off old dates and uses offline material as intended

            restaurant = newRestaurantsList.get(i);
            assertFalse(restaurant.getOnline());
        }

        restaurant = newRestaurantsList.get(9);
        assertFalse(restaurant.getOnline());
        assertEquals(10, newRestaurantsList.size());

        for (Restaurant r: nearbyRestaurantsList) {
            location = r.getLocation();
            double distance = service.calculateDistance(0.0, 0.0, location.getLatitude(), location.getLongitude());
            assert(distance < 1.5);
        }

        for (i = 0; i < 7; i++){
            /*

            asserts that three new restaurants ones are in right order

             */

            restaurant = nearbyRestaurantsList.get(i);
            restaurant_comp = nearbyRestaurantsList.get(i + 1);

            double distance1 = service.calculateDistance(0.0,
                    0.0,
                    restaurant.getLocation().getLatitude(),
                    restaurant.getLocation().getLongitude()
            );

            double distance2 = service.calculateDistance(0.0,
                    0.0,
                    restaurant_comp.getLocation().getLatitude(),
                    restaurant_comp.getLocation().getLongitude()
            );
            change = Double.compare(distance1, distance2);
            assert(change < 1); // asserts that items are in a descending order.
        }


    }
}
