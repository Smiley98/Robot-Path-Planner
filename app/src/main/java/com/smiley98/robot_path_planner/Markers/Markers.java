package com.smiley98.robot_path_planner.Markers;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

public class Markers {
    public void add(Icons.Type type, LatLng position, GoogleMap map) {
        Marker marker = map.addMarker(new MarkerOptions().position(position).icon(Icons.descriptor(type)).anchor(0.5f, 0.5f));
        marker.setTag(new Tag(type, ++mMarkerId));
        switch (type) {
            case WAY:
                mWays.put(mMarkerId, marker);
                break;

            case BOUNDARY:
                mBoundaries.put(mMarkerId, marker);
                break;

            case OBSTACLE:
                mObstacles.put(mMarkerId, marker);
                break;
        }
        //return marker;//Ideally marker isn't accessible externally.
    }

    void remove(Marker marker) {
        Tag tag = (Tag) marker.getTag();
        switch (tag.type()) {
            case WAY:
                mWays.remove(tag.id());
                break;

            case BOUNDARY:
                mBoundaries.remove(tag.id());
                break;

            case OBSTACLE:
                mObstacles.remove(tag.id());
                break;
        }
        marker.remove();
    }

    private Map<Integer, Marker> mWays = new HashMap<>();
    private Map<Integer, Marker> mBoundaries = new HashMap<>();
    private Map<Integer, Marker> mObstacles = new HashMap<>();

    private int mMarkerId;
}
