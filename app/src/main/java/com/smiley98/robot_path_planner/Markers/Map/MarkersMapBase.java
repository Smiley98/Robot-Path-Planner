package com.smiley98.robot_path_planner.Markers.Map;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.smiley98.robot_path_planner.Markers.MarkerFactory;
import com.smiley98.robot_path_planner.Markers.Tag;
import com.smiley98.robot_path_planner.Markers.Type;

import java.util.Map;
import java.util.NavigableMap;

/*DEPRECATED*/
//This class serves as a reference.
//Too simplistic. WAY, BOUNDARY and OBSTACLE are too different to have common add & remove operations.
public class MarkersMapBase {

    //Returns the key of the added marker.
    protected static Long add(NavigableMap<Long, Marker> markers, Type type, LatLng position, GoogleMap map) {
        Marker marker = sFactory.create(type, position, map);
        Long id = ((Tag) marker.getTag()).id();
        markers.put(id, marker);
        return id;
    }

    //Returns the key of the marker previous to the removed marker.
    protected static Long remove(@NonNull Marker marker, NavigableMap<Long, Marker> markers) {
        Tag tag = (Tag) marker.getTag();
        assert tag != null;
        Map.Entry<Long, Marker> previous = markers.lowerEntry(tag.id());
        markers.remove(tag.id());
        marker.remove();
        return previous == null ? null : previous.getKey();
    }

    private static final MarkerFactory sFactory = new MarkerFactory();
}
