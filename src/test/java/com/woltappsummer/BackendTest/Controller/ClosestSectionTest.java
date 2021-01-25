package com.woltappsummer.BackendTest.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.woltappsummer.BackendTest.Enviroments.TestEnviroments;
import com.woltappsummer.BackendTest.Model.Restaurant;
import com.woltappsummer.BackendTest.Service.RestaurantService;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

@AutoConfigureMockMvc
@SpringBootTest(properties = {
        "my-properties.filename=testfile.json"
})
public class ClosestSectionTest {

    private String filepath = System.getProperty("user.dir") + "/" + "testfile.json";
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    TestEnviroments env = new TestEnviroments();
    private String url = "http://localhost:8080/discovery?lat=00.000&lon=00.000";

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() throws Exception {
        File file = new File(this.filepath);
        file.createNewFile();
        try {
            FileWriter writer = new FileWriter(filepath);
            writer.write(env.positionTestingEnvironment());
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
    public void testTestSetup() throws  Exception {

        /* Tests that some basic testing tools work */

    assertNotEquals(null, this.mockMvc);

    String testingEnvironment = env.positionTestingEnvironment();
    Restaurant[] restaurants = gson.fromJson(testingEnvironment, Restaurant[].class);
    assertEquals(97, restaurants.length);

    }

    @Test
    @Ignore
    public void testPositions() throws Exception{
        MvcResult result = this.mockMvc.perform(get(this.url)).andReturn();
        String resultString = result.getResponse().getContentAsString();
        RestaurantService.ResponseObject obj = gson.fromJson(resultString, RestaurantService.ResponseObject.class);
        List<RestaurantService.Section> sections = obj.getSections();
        assertEquals(3, sections.size());

        RestaurantService.Section section = sections.get(2);
        assertEquals(section.getTitle(), "Nearby Restaurants");

        assert(section.getRestaurants().size() > 0);
        assert(section.getRestaurants().size() < 11);
    }
}
