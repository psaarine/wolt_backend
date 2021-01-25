package com.woltappsummer.BackendTest.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.woltappsummer.BackendTest.ApplicationProperties;
import com.woltappsummer.BackendTest.Enviroments.TestEnviroments;
import com.woltappsummer.BackendTest.Model.Restaurant;
import com.woltappsummer.BackendTest.Service.RestaurantService;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(properties = {
        "my-properties.filename=testfile.json"
})
class RestaurantsControllerTest {

    private String filepath = System.getProperty("user.dir") + "/" + "testfile.json";
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    TestEnviroments env = new TestEnviroments();
    Restaurant restaurant;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() throws Exception {
        File file = new File(this.filepath);
        file.createNewFile();
        try {
            FileWriter writer = new FileWriter(filepath);
            writer.write(env.positionDerivedEnvironment());
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
    public void testsPopularSection() throws Exception {
        MvcResult resp = this.mockMvc.perform(get("http://localhost:8080/discovery?lat=60.000&lon=60.000"))
                .andReturn();
        String responseString = resp.getResponse().getContentAsString();
        RestaurantService.ResponseObject obj = gson.fromJson(responseString, RestaurantService.ResponseObject.class);
        List<RestaurantService.Section> sections = obj.getSections();
        assertEquals(3, sections.size());

        RestaurantService.Section popular = sections.get(0);
        RestaurantService.Section newRests = sections.get(1);
        RestaurantService.Section nearby = sections.get(2);

        assertEquals(popular.getTitle(), "Popular Restaurants");
        assertEquals(newRests.getTitle(), "New Restaurants");
        assertEquals(nearby.getTitle(), "Nearby Restaurants");

        List<Restaurant> popularRestaurants = popular.getRestaurants();
        List<Restaurant> newRestaurants = popular.getRestaurants();
        List<Restaurant> nearbyRestaurants = popular.getRestaurants();

        assertEquals(7, popularRestaurants.size());
        //assertEquals(10, newRestaurants);
        assertEquals(7, nearbyRestaurants.size());
    }

    @Test
    public void testNullSection() throws  Exception {

        /*

        Tests that program does not fall even if there are no restaurants within radius

         */

        MvcResult result = this.mockMvc.perform(get("http://localhost:8080/discovery?lat=40.000&lon=40.000"))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

}