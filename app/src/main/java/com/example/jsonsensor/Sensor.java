package com.example.jsonsensor;

public class Sensor {

    private Integer humidity;
    private Position position;
    private Double luminosity;
    private Double proximity;
    private Integer temperature;

    public Sensor(Integer humidity, Position position, Double luminosity, Double proximity, Integer temperature) {
        this.humidity = humidity;
        this.position = position;
        this.luminosity = luminosity;
        this.proximity = proximity;
        this.temperature = temperature;
    }

}
