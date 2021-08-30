package com.smiley98.robot_path_planner.Editor.Interfaces;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface IMarkerResponder {
    void onMarkerAdded(List<LatLng> points, GoogleMap map, Context context);
    void onMarkerRemoved(List<LatLng> points);
}
