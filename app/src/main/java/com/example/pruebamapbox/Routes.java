package com.example.pruebamapbox;


import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;

public class Routes {

    static DirectionsRoute currentDirectionsRoute;

    public static void setCurrentDirectionsRoute(DirectionsRoute dir){
        currentDirectionsRoute = dir;
    }

    public static DirectionsRoute getCurrentDirectionsRoute(){
        return currentDirectionsRoute;
    }

    public static List<Point> getRoute(int index) {
        List<Point> stops = new ArrayList<>();
        if(index == 1){
            //Huerta de San Vicente
            stops.add(Point.fromLngLat(-3.609268, 37.170675));
            //Preguntamos si quiere ir a la catedral
            stops.add(Point.fromLngLat( -3.600633, 37.176316));
            //Centro García Lorca
            stops.add(Point.fromLngLat(-3.600693, 37.176633));
            //Preguntar si quiere ir al monasterio san jerónimo
            stops.add(Point.fromLngLat(-3.602936, 37.179895));
            //Monumento a federico
            stops.add(Point.fromLngLat(-3.602994, 37.183474));
        }
        return stops;
    }

    public static Point getParada(String lugar){
        switch (lugar){
            case "Catedral":
                return Point.fromLngLat(-3.599658, 37.176144);
            case "Triunfo":
                return  Point.fromLngLat(-3.601929, 37.183936);
            case "Monasterio":
                return Point.fromLngLat(-3.603970, 37.180203);
            case "Reyes católicos":
                return Point.fromLngLat(-3.597477, 37.175648);
            default:
                return null;
        }
    }
}
