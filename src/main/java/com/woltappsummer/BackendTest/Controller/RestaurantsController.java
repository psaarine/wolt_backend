package com.woltappsummer.BackendTest.Controller;

import com.woltappsummer.BackendTest.Model.Restaurant;
import com.woltappsummer.BackendTest.Service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class RestaurantsController {

    @Autowired
    RestaurantService service;

    @GetMapping("/discovery")
    public List<Restaurant> getRestaurants() throws IOException {
        List<Restaurant> db = service.getResponse();
        return db;
    };
}
