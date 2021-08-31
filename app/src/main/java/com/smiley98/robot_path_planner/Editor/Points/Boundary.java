package com.smiley98.robot_path_planner.Editor.Points;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.smiley98.robot_path_planner.Editor.Common.Type;
import com.smiley98.robot_path_planner.Editor.Containers.Polygon;
import com.smiley98.robot_path_planner.Editor.Interfaces.IPoint;

public class Boundary implements IPoint {
    public Boundary(AppCompatButton button) {
        mButton = button;
        setState(State.ADD);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng, GoogleMap map) {
        if (mState == State.ADD)
            mPolygon.add(latLng, map, mButton.getContext());
    }

    @Override
    public void onMarkerClick(@NonNull Marker marker) {
        setState(State.REMOVE);
        mPolygon.setSelected(marker);
    }

    @Override
    public void onButtonClick() {
        if (mState == State.REMOVE) {
            mPolygon.remove();

            if (mPolygon.size() == 0)
                setState(State.ADD);
        }
    }

    @Override
    public void onButtonLongClick() {
        setState(State.ADD);
    }

    private void setState(State state) {
        switch (state) {
            case ADD:
                mButton.setText("Add Boundary Point");
                break;

            case REMOVE:
                mButton.setText("Remove Boundary Point");
                break;
        }
        mState = state;
    }

    private final Polygon mPolygon = new Polygon(Type.BOUNDARY);
    private final AppCompatButton mButton;
    private State mState;

    private enum State {
        ADD,
        REMOVE
    }
}