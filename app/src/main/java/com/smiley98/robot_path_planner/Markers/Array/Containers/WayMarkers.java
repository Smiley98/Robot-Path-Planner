package com.smiley98.robot_path_planner.Markers.Array.Containers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.smiley98.robot_path_planner.Markers.Icons;
import com.smiley98.robot_path_planner.Markers.MarkerFactory;
import com.smiley98.robot_path_planner.Markers.State;
import com.smiley98.robot_path_planner.Markers.Type;

import java.util.ArrayList;

public class WayMarkers implements IMarkerOperations {
    public WayMarkers(AppCompatButton button) {
        mButton = button;
        setState(State.ADD);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng, GoogleMap map, Context context) {
        if (mState == State.ADD)
            mMarkers.add(latLng, map);
    }

    @Override
    public void onMarkerClick(@NonNull Marker marker) {
        setState(State.REMOVE);
        if (mMarkers.selected() != null)
            mMarkers.selected().setIcon(Icons.normal(Type.WAY));
        mMarkers.setSelected(marker);
    }

    @Override
    public void onMarkerButtonClick() {
        if (mState == State.REMOVE) {
            if (mMarkers.selected() == null && mMarkers.size() > 0)
                mMarkers.setSelected(mMarkers.get(mMarkers.size() - 1));
            mMarkers.remove(mMarkers.selected());
        }
    }

    @Override
    public void onMarkerButtonLongClick() {
        setState(State.ADD);
    }

    private void setState(State state) {
        switch (state) {
            case ADD:
                mButton.setText("Add Way Point");
                break;

            case REMOVE:
                mButton.setText("Remove Way Point");
                break;
        }
        mState = state;
    }

    private final MarkerContainer mMarkers = new MarkerContainer(Type.WAY);
    private final AppCompatButton mButton;
    private State mState;
}
