package com.woltappsummer.BackendTest.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
public class Restaurant {
    private String blurhash;
    private Location location;
    private String name;
    private Date launch_date;
    private Boolean online;
    private Float popularity;
}
