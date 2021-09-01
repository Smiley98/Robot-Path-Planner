package com.smiley98.robot_path_planner.Editor.Points;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.smiley98.robot_path_planner.Editor.Common.SerialPoint;
import com.smiley98.robot_path_planner.Editor.Containers.Lines;
import com.smiley98.robot_path_planner.Editor.Containers.Markers;
import com.smiley98.robot_path_planner.Editor.Common.Type;
import com.smiley98.robot_path_planner.Editor.Interfaces.IPoint;

import java.util.ArrayList;

public class Way implements IPoint {
    public Way(AppCompatButton button) {
        mButton = button;
        setState(State.ADD);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng, GoogleMap map) {
        if (mState == State.ADD)
            add(latLng, map);
    }

    @Override
    public void onMarkerClick(@NonNull Marker marker) {
        setState(State.REMOVE);
        mMarkers.setSelected(marker);
    }

    @Override
    public void onButtonClick() {
        if (mState == State.REMOVE && mMarkers.size() > 0) {
            mMarkers.remove();
            mLines.onMarkerRemoved(mMarkers.points());

            if (mMarkers.size() == 0)
                setState(State.ADD);
        }
    }

    @Override
    public void onButtonLongClick() {
        setState(State.ADD);
    }

    public void load(ArrayList<SerialPoint> points, GoogleMap map) {
        clear();
        for (SerialPoint point : points)
            add(point.latLng(), map);
    }

    public void clear() {
        mMarkers.clear();
        mLines.clear();
    }

    public ArrayList<SerialPoint> points() {
        ArrayList<SerialPoint> result = new ArrayList<>();
        for (LatLng latLng : mMarkers.points())
            result.add(new SerialPoint(latLng));
        return result;
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

    private void add(@NonNull LatLng latLng, GoogleMap map) {
        mMarkers.add(latLng, map);
        mLines.onMarkerAdded(mMarkers.points(), map, mButton.getContext());
    }

    private final Markers mMarkers = new Markers(Type.WAY);
    private final Lines mLines = new Lines(Type.WAY);
    private final AppCompatButton mButton;
    private State mState;

    private enum State {
        ADD,
        REMOVE
    }
}
