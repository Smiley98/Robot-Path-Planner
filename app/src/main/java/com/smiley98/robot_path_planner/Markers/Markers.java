package com.smiley98.robot_path_planner.Markers;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.smiley98.robot_path_planner.Markers.Common.Type;
import com.smiley98.robot_path_planner.Markers.Containers.BoundaryMarkers;
import com.smiley98.robot_path_planner.Markers.Containers.Obstacles;
import com.smiley98.robot_path_planner.Markers.Containers.WayMarkers;
import com.smiley98.robot_path_planner.Markers.Interfaces.IMarkerContainers;
import com.smiley98.robot_path_planner.Markers.Interfaces.IMarkers;

import java.util.Objects;

public class Markers implements IMarkers {

    public Markers(AppCompatButton[] markerButtons) {
        mMarkers[Type.WAY.ordinal()] = new WayMarkers(markerButtons[Type.WAY.ordinal()]);
        mMarkers[Type.BOUNDARY.ordinal()] = new BoundaryMarkers(markerButtons[Type.BOUNDARY.ordinal()]);
        mMarkers[Type.OBSTACLE.ordinal()] = new Obstacles(markerButtons[Type.OBSTACLE.ordinal()]);
    }

    //Add
    @Override
    public void onMapClick(@NonNull LatLng latLng, GoogleMap map) {
        mMarkers[mType.ordinal()].onMapClick(latLng, map);
    }

    //Set
    @Override
    public void onMarkerClick(@NonNull Marker marker) {
        Type type = (Type) Objects.requireNonNull(marker.getTag());
        mMarkers[type.ordinal()].onMarkerClick(marker);
        mType = type;
    }

    //Remove
    @Override
    public void onMarkerButtonClick(Type type) {
        mMarkers[type.ordinal()].onMarkerButtonClick();
        mType = type;
    }

    //Reset
    @Override
    public void onMarkerButtonLongClick(Type type) {
        mMarkers[type.ordinal()].onMarkerButtonLongClick();
        mType = type;
    }

    private final IMarkerContainers[] mMarkers = new IMarkerContainers[Type.values().length];
    private Type mType = Type.WAY;
}
