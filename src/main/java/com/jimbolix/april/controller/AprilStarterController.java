package com.jimbolix.april.controller;

import com.jimbolix.aprilspringbootstarter.domain.Weather;
import com.jimbolix.aprilspringbootstarter.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @ClassName AprilStarterController
 * @Author liruihui
 * @date 2020.05.24 18:11
 */
@RestController
@RequestMapping("/starter")
public class AprilStarterController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/get")
    public Weather getWeather(){
        return weatherService.getWeather();
    }
}
