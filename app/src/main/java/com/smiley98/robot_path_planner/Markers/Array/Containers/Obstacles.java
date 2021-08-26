package com.smiley98.robot_path_planner.Markers.Array.Containers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.smiley98.robot_path_planner.Markers.IMarkers;
import com.smiley98.robot_path_planner.Markers.Type;

import java.util.ArrayList;

public class Obstacles implements IMarkerOperations {

    @Override
    public Type onMapClick(@NonNull LatLng latLng, GoogleMap map, Context context) {

    }

    @Override
    public Type onMarkerClick(@NonNull Marker marker) {

    }

    @Override
    public Type onMarkerButtonClick(Type type) {

    }

    @Override
    public Type onMarkerButtonLongClick(Type type) {

    }

    private ArrayList<ArrayList<Marker>> mObstacles = new ArrayList<>();
}
