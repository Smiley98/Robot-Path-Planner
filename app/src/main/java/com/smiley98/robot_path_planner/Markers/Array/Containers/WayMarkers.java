package com.smiley98.robot_path_planner.Markers.Array.Containers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.smiley98.robot_path_planner.Markers.IMarkers;
import com.smiley98.robot_path_planner.Markers.Type;

import java.util.ArrayList;

public class WayMarkers implements IMarkers {
    public WayMarkers(AppCompatButton button) {
        mButton = button;
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng, GoogleMap map, Context context) {

    }

    @Override
    public void onMarkerClick(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerButtonClick(Type type) {

    }

    @Override
    public void onMarkerButtonLongClick(Type type) {

    }

    private AppCompatButton mButton;
    private ArrayList<Marker> mMarkers = new ArrayList<>();
}
