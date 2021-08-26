package com.smiley98.robot_path_planner.Markers.Array;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.smiley98.robot_path_planner.Markers.IMarkers;
import com.smiley98.robot_path_planner.Markers.Type;

public class ArrayMarkerImplementation extends ArrayMarkerBase implements IMarkers {

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
}
