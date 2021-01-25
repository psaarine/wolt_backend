package com.woltappsummer.BackendTest.Controller;

import com.woltappsummer.BackendTest.Model.Restaurant;
import com.woltappsummer.BackendTest.Service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class RestaurantsController {

    @Autowired
    RestaurantService service;

    @GetMapping("/discovery")
    public RestaurantService.ResponseObject getRestaurants(
            @RequestParam(required = true) float lat,
            @RequestParam(required = true) float lon
    ) throws IOException {

        RestaurantService.ResponseObject response = service.getResponse(lat, lon);
        return response;
    };

}
