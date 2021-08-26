package com.smiley98.robot_path_planner.Markers.Array;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.smiley98.robot_path_planner.Markers.Array.Containers.BoundaryMarkers;
import com.smiley98.robot_path_planner.Markers.Array.Containers.Obstacles;
import com.smiley98.robot_path_planner.Markers.Array.Containers.WayMarkers;
import com.smiley98.robot_path_planner.Markers.IMarkers;
import com.smiley98.robot_path_planner.Markers.Tag;
import com.smiley98.robot_path_planner.Markers.Type;

import java.util.Objects;

public class MarkersArrayImplementation implements IMarkers {

    public MarkersArrayImplementation(AppCompatButton[] markerButtons) {
        mMarkers[Type.WAY.ordinal()] = new WayMarkers(markerButtons[Type.WAY.ordinal()]);
        mMarkers[Type.BOUNDARY.ordinal()] = new BoundaryMarkers(markerButtons[Type.BOUNDARY.ordinal()]);
        mMarkers[Type.OBSTACLE.ordinal()] = new Obstacles(markerButtons[Type.OBSTACLE.ordinal()]);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng, GoogleMap map, Context context) {
        mMarkers[mType.ordinal()].onMapClick(latLng, map, context);
    }

    @Override
    public void onMarkerClick(@NonNull Marker marker) {
        mType = ((Tag) Objects.requireNonNull(marker.getTag())).type();
        mMarkers[mType.ordinal()].onMarkerClick(marker);
    }

    @Override
    public void onMarkerButtonClick(Type type) {
        mMarkers[type.ordinal()].onMarkerButtonClick(type);
        mType = type;
    }

    @Override
    public void onMarkerButtonLongClick(Type type) {
        mMarkers[type.ordinal()].onMarkerButtonLongClick(type);
        mType = type;
    }

    private final IMarkers[] mMarkers = new IMarkers[Type.values().length];
    private Type mType = Type.WAY;
}
