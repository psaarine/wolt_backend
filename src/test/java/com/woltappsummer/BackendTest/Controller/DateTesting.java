package com.woltappsummer.BackendTest.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.woltappsummer.BackendTest.Enviroments.TestEnviroments;
import com.woltappsummer.BackendTest.Model.Restaurant;
import com.woltappsummer.BackendTest.Service.RestaurantService;
import org.apache.coyote.Response;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest(properties = {
        "my-properties.filename=testfile.json"
})
public class DateTesting {

    @Autowired
    MockMvc mvc;

    private String filepath = System.getProperty("user.dir") + "/" + "testfile.json";
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    TestEnviroments env = new TestEnviroments();
    RestaurantService service = new RestaurantService();
    private String url = "http://localhost:8080/discovery?lat=00.000&lon=00.000";

    @BeforeEach
    public void setUp() throws Exception {
        File file = new File(this.filepath);
        file.createNewFile();
        try {
            FileWriter writer = new FileWriter(filepath);
            writer.write(env.dateTestingEnvironment());
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
    public void testTesting() throws Exception{
        assertNotEquals(this.mvc, null);

        MvcResult result = this.mvc.perform(get(this.url)).andReturn();
        String asString = result.getResponse().getContentAsString();
        RestaurantService.ResponseObject restaurants = gson.fromJson(asString, RestaurantService.ResponseObject.class);

        List<RestaurantService.Section> sections = restaurants.getSections();

        assertEquals(3, sections.size());
    }

    @Test
    public void testEndpoint() throws Exception{

        MvcResult result = this.mvc.perform(get(this.url)).andReturn();
        String resultString = result.getResponse().getContentAsString();

        RestaurantService.ResponseObject object = gson.fromJson(resultString, RestaurantService.ResponseObject.class);

        List<RestaurantService.Section> sections = object.getSections();

        assertEquals(3, sections.size());

        RestaurantService.Section section = sections.get(1);
        assertEquals("New Restaurants", section.getTitle());
        List<Restaurant> restaurants = section.getRestaurants();

        int change = 0;
        Date date = restaurants.get(0).getLaunch_date();

        Iterator<Restaurant> iterator = restaurants.listIterator();

        while(iterator.hasNext()){
            Restaurant r = iterator.next();
            change = date.compareTo(r.getLaunch_date());

            assert(change >= 0); // Asserts that date is always higher.
            date = r.getLaunch_date();

        }


    }
}
