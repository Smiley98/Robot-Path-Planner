package com.smiley98.robot_path_planner.Markers;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LegacyMarkerFactory {
    public Marker create(Type type, LatLng position, GoogleMap map) {
        Marker marker = map.addMarker(new MarkerOptions().position(position).icon(Icons.normal(type)).anchor(0.5f, 0.5f));
        assert marker != null;
        marker.setTag(new Tag(type, ++mId));
        return marker;
    }
    
    private long mId = 0L;
}
