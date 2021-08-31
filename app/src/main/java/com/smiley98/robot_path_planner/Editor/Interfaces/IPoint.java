package com.smiley98.robot_path_planner.Editor.Interfaces;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public interface IPoint {
    void onMapClick(@NonNull LatLng latLng, GoogleMap map);
    void onMarkerClick(@NonNull Marker marker);
    void onButtonClick();
    void onButtonLongClick();
}
