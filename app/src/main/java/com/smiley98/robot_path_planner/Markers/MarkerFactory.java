package com.smiley98.robot_path_planner.Markers;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MarkerFactory {
    public static Marker create(Type type, LatLng position, GoogleMap map) {
        Marker marker = map.addMarker(new MarkerOptions().position(position).icon(Icons.descriptor(type)).anchor(0.5f, 0.5f));
        assert marker != null;
        marker.setTag(new Tag(type, ++sId));
        return marker;
    }
    
    private static long sId = 0L;
}
