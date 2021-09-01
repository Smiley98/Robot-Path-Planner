package com.smiley98.robot_path_planner.Editor.Common;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

//Boilerplate class because Google Maps is too dumb to implement Serializable...
public class SerialPoint implements Serializable {
    public SerialPoint(double latitude, double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public SerialPoint(LatLng latLng) {
        mLatitude = latLng.latitude;
        mLongitude = latLng.longitude;
    }

    public LatLng latLng() { return new LatLng(mLatitude, mLongitude); }
    public double latitude() { return mLatitude; }
    public double longitude() { return mLongitude; }

    private final double mLatitude;
    private final double mLongitude;
}
