package com.woltappsummer.BackendTest.Functions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.woltappsummer.BackendTest.Enviroments.TestEnviroments;
import com.woltappsummer.BackendTest.Model.Location;
import com.woltappsummer.BackendTest.Model.Restaurant;
import com.woltappsummer.BackendTest.Service.RestaurantService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TestDistanceCalculator {

    RestaurantService service = new RestaurantService();
    TestEnviroments enviroments = new TestEnviroments();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void testDistanceCalculator(){

        /*

        Testing my applications most difficult part which is without a doubt calculating the distance.
        First set of coordinates that I know are close than 1.5km and then set of coordinates that I know
        are further than that

         */

        assert(service.calculateDistance(60.30530928303577, 25.415076652143444,
                60.30458645800774, 25.439109243673208) < 1.5);
        assert(service.calculateDistance(60.4560609146588, 25.298990645981103,
                60.45855783604002, 25.29435578904322) < 1.5);
        assert(service.calculateDistance(60.49582482678631, 25.280059615509384,
                60.50577800045317, 25.296582022186094) < 1.5);

        // These are further away

        assert(service.calculateDistance(60.49580369153146, 25.280059615509384,
                60.507616150386184, 25.300444402967663) > 1.5);

        assert(service.calculateDistance(60.30518172683981, 25.415162482827483,
                60.303565971921955, 25.473699009339256) > 1.5);
        assert(service.calculateDistance(60.30530928303577, 25.415076652143444,
                60.31559186186125, 25.439280951780955) > 1.5);
    }

    @Test
    public void testValidation(){
        Location l;

        l = new Location(0.0, 0.0);

        assertTrue(service.validateDistance(l, 0.0f, 0.0f));
        assertFalse(service.validateDistance(l, 0.0f, 1.0f));

    }

    @Test
    public void testPopular(){
        /*

        Tests that getPopular method returns maximum of 10 items and uses both lists accordingly

         */

        // Tests that function slices list accordingly

        List<Restaurant> restaurants = this.enviroments.getRestaurantList(15, true);
        List<Restaurant> restaurantsFalse = this.enviroments.getRestaurantList(15, false);
        List<Restaurant> testRestaurants = service.getPopular(restaurants, restaurantsFalse);

        assertEquals(testRestaurants.size(), 10);

        // Tests that function uses both lists if necessary

        restaurants = this.enviroments.getRestaurantList(7, true);
        restaurantsFalse = this.enviroments.getRestaurantList(7, false);
        testRestaurants = service.getPopular(restaurants, restaurantsFalse);

        assertEquals(testRestaurants.size(), 10);

        // Tests that function returns less than 10 restaurants if there is not enough restaurants.

        restaurants = this.enviroments.getRestaurantList(3, true);
        restaurantsFalse = this.enviroments.getRestaurantList(3, false);
        testRestaurants = service.getPopular(restaurants, restaurantsFalse);

        assertEquals(testRestaurants.size(), 6);
    }

    @Test
    public void testArrangeClosest(){

        /*

        Tests that RestaurantService arrangeClosest method works as intended.

         */
        ArrayList<Restaurant> arrayList = new ArrayList<Restaurant>();
        Restaurant[] restaurants = gson.fromJson(enviroments.positionTestingEnvironment(), Restaurant[].class);
        Collections.addAll(arrayList, restaurants);
        assertEquals(arrayList.size(), 97);

        // Asserts that all the small locations are scattered within the list

        assert(arrayList.get(20).getLocation().getLatitude() < 0.01);
        assert(arrayList.get(30).getLocation().getLatitude() < 0.01);
        assert(arrayList.get(40).getLocation().getLatitude() < 0.01);
        assert(arrayList.get(50).getLocation().getLatitude() < 0.01);

        arrayList = (ArrayList<Restaurant>) service.arrangeClosest(arrayList, 0.0f, 0.0f);

        // Asserts that all the small distances have vanished from standard positions

        assert(arrayList.get(20).getLocation().getLatitude() == 0.01);
        assert(arrayList.get(30).getLocation().getLatitude() == 0.01);
        assert(arrayList.get(40).getLocation().getLatitude() == 0.01);
        assert(arrayList.get(50).getLocation().getLatitude() == 0.01);

        // Asserts that all the small positions are now at the top of the list

        assert(arrayList.get(0).getLocation().getLatitude() == 0.0);
        assert(arrayList.get(1).getLocation().getLatitude() == 0.001);
        assert(arrayList.get(2).getLocation().getLatitude() == 0.002);
        assert(arrayList.get(3).getLocation().getLatitude() == 0.003);

    }

    @Test
    public void testGetNearby(){

        /*

        Tests that getNearby method works correctly

         */
        ArrayList<Restaurant> arrayList = new ArrayList<Restaurant>();
        Restaurant[] restaurants = gson.fromJson(enviroments.positionTestingEnvironment(), Restaurant[].class);
        Collections.addAll(arrayList, restaurants);

        // Asserts that all the small locations are scattered within the list

        assert(arrayList.get(20).getLocation().getLatitude() < 0.01);
        assert(arrayList.get(30).getLocation().getLatitude() < 0.01);
        assert(arrayList.get(40).getLocation().getLatitude() < 0.01);
        assert(arrayList.get(50).getLocation().getLatitude() < 0.01);

        List<Restaurant> splicedList = service.getNearby(arrayList, arrayList, 0.0f, 0.0f);
        assertEquals(10, splicedList.size());

        assert(splicedList.get(0).getLocation().getLatitude() == 0.0);
        assert(splicedList.get(1).getLocation().getLatitude() == 0.001);
        assert(splicedList.get(2).getLocation().getLatitude() == 0.002);
        assert(splicedList.get(3).getLocation().getLatitude() == 0.003);

        List<Restaurant> shortList = splicedList.subList(0, 7);
        List<Restaurant> shortDistantList = arrayList.subList(31, 38);


        splicedList  = service.getNearby(shortList, shortDistantList, 0.0f, 0.0f);

        assert(splicedList.get(0).getLocation().getLatitude() == 0.0);
        assert(splicedList.get(1).getLocation().getLatitude() == 0.001);
        assert(splicedList.get(2).getLocation().getLatitude() == 0.002);
        assert(splicedList.get(3).getLocation().getLatitude() == 0.003);

        assertEquals(10, splicedList.size());

        List<Restaurant> superShortList = splicedList.subList(0, 3);
        List<Restaurant> superShortDistantList = splicedList.subList(0, 3);

        splicedList = service.getNearby(superShortList, superShortDistantList, 0.0f, 0.0f);

        assertEquals(6, splicedList.size());
        assert(splicedList.get(0).getLocation().getLatitude() == 0.0);
        assert(splicedList.get(1).getLocation().getLatitude() == 0.001);
        assert(splicedList.get(2).getLocation().getLatitude() == 0.002);

    }

    @Test
    public void testSplitLists(){

        /*

        Tests that Restaurant service split list method works as expected

         */

        List<Restaurant> rawList = new ArrayList<Restaurant>();
        int i;
        Restaurant r;
        for (i = 0; i < 20; i++){
            r = new Restaurant(
                    "testhash",
                    new Location(0.0, 0.0),
                    "test-restaurant",
                    new Date(System.currentTimeMillis()),
                    this.returnBoolean(i),
                    0.1f
            );
            rawList.add(r);
        }

        RestaurantService.ListContainer cont = service.splitList(rawList);

        List<Restaurant> onlineList = cont.getOnlineList();
        List<Restaurant> offlineList = cont.getOfflineList();

        for (Restaurant onlineR: onlineList) {
            assertTrue(onlineR.getOnline());
        }

        for (Restaurant offlineR: offlineList) {
            assertFalse(offlineR.getOnline());
        }



    }

    @Test
    public void testRemoveDistant(){

        /*

         Tests removeDistant method of restaurant service. Also test generate date method.

         */

        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        List<Date> dates = new ArrayList<Date>();
        Restaurant r;
        int i;
        Date date;

        for (i = 0; i < 30; i++){
            date = enviroments.returnDate(i);
            dates.add(date);

            r = new Restaurant(
                    "hash",
                    new Location(0.0, 0.0),
                    "test",
                    date,
                    true,
                    0.3f
            );
            restaurants.add(r);
        }


        restaurants = service.removeOld(restaurants);
        for (Restaurant rest: restaurants) {
            assert (service.returnAsDays(rest.getLaunch_date()) > -122);
        }
    }


    private boolean returnBoolean(int i){
        if (i % 2 == 0){
            return true;
        } else {
            return false;
        }
    }
}
