package com.smiley98.robot_path_planner.Markers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public interface IMarkers {
    void onMapClick(@NonNull LatLng latLng, GoogleMap map, Context context);
    void onMarkerClick(@NonNull Marker marker);
    void onMarkerButtonClick(Type type);
    void onMarkerButtonLongClick(Type type);
}
