package com.smiley98.robot_path_planner.Markers.Array.Containers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.smiley98.robot_path_planner.Markers.State;

import java.util.ArrayList;

public class WayMarkers implements IMarkerOperations {
    public WayMarkers(AppCompatButton button) {
        mButton = button;
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng, GoogleMap map, Context context) {
        
    }

    @Override
    public void onMarkerClick(@NonNull Marker marker) {
        setState(State.REMOVE);
    }

    @Override
    public void onMarkerButtonClick() {

    }

    @Override
    public void onMarkerButtonLongClick() {
        setState(State.ADD);
    }

    private void setState(State state) {
        switch (state) {
            case ADD:
                break;

            case REMOVE:
                break;
        }
        mState = state;
    }

    private AppCompatButton mButton;
    private State mState = State.ADD;
    private ArrayList<Marker> mMarkers = new ArrayList<>();
    private Integer mSelected = null;
}
