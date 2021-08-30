package com.smiley98.robot_path_planner.Editor.Points;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.smiley98.robot_path_planner.Editor.Containers.LineContainer;
import com.smiley98.robot_path_planner.Editor.Containers.MarkerContainer;
import com.smiley98.robot_path_planner.Editor.Common.Icons;
import com.smiley98.robot_path_planner.Editor.Common.State;
import com.smiley98.robot_path_planner.Editor.Common.Type;
import com.smiley98.robot_path_planner.Editor.IPoint;

public class Obstacles implements IPoint {
    public Obstacles(AppCompatButton button) {
        mButton = button;
        setState(State.ADD);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng, GoogleMap map) {
        if (mState == State.ADD) {
            mMarkers.add(latLng, map);
            mLines.add(mMarkers.points(), map, mButton.getContext());
        }
    }

    @Override
    public void onMarkerClick(@NonNull Marker marker) {
        setState(State.REMOVE);
        if (mMarkers.selected() != null)
            mMarkers.selected().setIcon(Icons.normal(Type.OBSTACLE));
        mMarkers.setSelected(marker);
    }

    @Override
    public void onButtonClick() {
        if (mState == State.REMOVE && mMarkers.size() > 0) {
            if (mMarkers.selected() == null)
                mMarkers.setSelected(mMarkers.get(mMarkers.size() - 1));
            mMarkers.remove(mMarkers.selected());
            mLines.update(mMarkers.points());

            if (mMarkers.size() == 0)
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
                mButton.setText("Add Obstacle Point");
                break;

            case REMOVE:
                mButton.setText("Remove Obstacle Point");
                break;
        }
        mState = state;
    }

    private final MarkerContainer mMarkers = new MarkerContainer(Type.OBSTACLE);
    private final LineContainer mLines = new LineContainer(Type.OBSTACLE);
    private final AppCompatButton mButton;
    private State mState;
}
