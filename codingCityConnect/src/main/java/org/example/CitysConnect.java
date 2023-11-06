package org.example;
import java.util.*;

public class CitysConnect {

    // key : (originCityName_DestinationCityName)
    // distance :


    // key : originCity


    public CitysConnect(String city, Map<String, Integer> connectedCities) {
        this.city = city;
        this.connectedCities = connectedCities;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private String city;
    private Map<String , Integer> connectedCities;  //connectedcity , distance

    public Map<String, Integer> getConnectedCities() {
        return connectedCities;
    }

    public void setConnectedCities(Map<String, Integer> connectedCities) {
        this.connectedCities = connectedCities;
    }




}
