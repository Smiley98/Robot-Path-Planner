package com.smiley98.robot_path_planner.Markers.Array.Containers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

//Identical to IMarkers, but omits type parameter since each marker container corresponds to a unique type.
public interface IMarkerOperations {
    void onMapClick(@NonNull LatLng latLng, GoogleMap map, Context context);
    void onMarkerClick(@NonNull Marker marker);
    void onMarkerButtonClick();
    void onMarkerButtonLongClick();
}
