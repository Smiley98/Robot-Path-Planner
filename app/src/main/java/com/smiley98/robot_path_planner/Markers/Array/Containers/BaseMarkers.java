package com.smiley98.robot_path_planner.Markers.Array.Containers;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.smiley98.robot_path_planner.Markers.MarkerFactory;
import com.smiley98.robot_path_planner.Markers.Type;

public class BaseMarkers {
    protected static void add(MarkerContainer markers, LatLng latlng, GoogleMap map) {
        Marker marker = MarkerFactory.create(markers.type(), latlng, map);
    }

    protected static void remove(MarkerContainer markers, int index) {
        
    }
}
