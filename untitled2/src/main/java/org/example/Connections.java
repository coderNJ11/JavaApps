package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Connections {

    List<CitysConnect> connection = new ArrayList<>();
    //Map(Name , Map)

    public Connections(List<CitysConnect> connection) {
        this.connection = connection;
    }


    public List<CitysConnect> getConnection() {
        return connection;
    }

    public void setConnection(List<CitysConnect> connection) {
        this.connection = connection;
    }

    public List<String> getConnectedCitiesTo(String cityName){

        List<String> connectedCitiesByCityName = connection.stream().filter(c ->  c.getCity().equalsIgnoreCase(cityName))
                .flatMap(c -> c.getConnectedCities().keySet().stream())
                .collect(Collectors.toList());

    }
}
