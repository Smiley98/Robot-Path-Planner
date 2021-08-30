package com.smiley98.robot_path_planner.Editor.Points;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.smiley98.robot_path_planner.Editor.Containers.Lines;
import com.smiley98.robot_path_planner.Editor.Containers.Markers;
import com.smiley98.robot_path_planner.Editor.Common.Icons;
import com.smiley98.robot_path_planner.Editor.Common.State;
import com.smiley98.robot_path_planner.Editor.Common.Type;
import com.smiley98.robot_path_planner.Editor.IPoint;

public class Way implements IPoint {
    public Way(AppCompatButton button) {
        mButton = button;
        setState(State.ADD);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng, GoogleMap map) {
        if (mState == State.ADD) {
            mMarkers.add(latLng, map);
            mLines.onMarkerAdded(mMarkers.points(), map, mButton.getContext());
        }
    }

    @Override
    public void onMarkerClick(@NonNull Marker marker) {
        setState(State.REMOVE);
        if (mMarkers.selected() != null)
            mMarkers.selected().setIcon(Icons.normal(Type.WAY));
        mMarkers.setSelected(marker);
    }

    @Override
    public void onButtonClick() {
        if (mState == State.REMOVE && mMarkers.size() > 0) {
            if (mMarkers.selected() == null )
                mMarkers.setSelected(mMarkers.get(mMarkers.size() - 1));
            mMarkers.remove(mMarkers.selected());
            mLines.onMarkerRemoved(mMarkers.points());

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
                mButton.setText("Add Way Point");
                break;

            case REMOVE:
                mButton.setText("Remove Way Point");
                break;
        }
        mState = state;
    }

    private final Markers mMarkers = new Markers(Type.WAY);
    private final Lines mLines = new Lines(Type.WAY);
    private final AppCompatButton mButton;
    private State mState;
}
