package com.smiley98.robot_path_planner.Markers.Array;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class ArrayMarkerContainer {
    
    private ArrayList<Marker> mWayMarkers = new ArrayList<>();
    private ArrayList<Marker> mBoundaryMarkers = new ArrayList<>();
    private ArrayList<ArrayList<Marker>> mObstacleMarkers = new ArrayList<>();
}
