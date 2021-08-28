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
            add(latLng, map);
    }

    @Override
    public void onMarkerClick(@NonNull Marker marker) {
        setState(State.REMOVE);
        if (mSelected != null)
            mSelected.setIcon(Icons.normal(Type.WAY));
        setSelected(marker);
    }

    //Remove selected marker if it exists.
    @Override
    public void onMarkerButtonClick() {
        if (mState == State.REMOVE && mSelected != null)
            remove(mMarkers.indexOf(mSelected));
    }

    //Reset state to ADD.
    @Override
    public void onMarkerButtonLongClick() {
        setState(State.ADD);
    }

    //If the selected marker exists, revert its icon and set selected to its successor.
    //Otherwise append to back and assign to selected.
    private void add(LatLng latlng, GoogleMap map) {
        Marker marker = MarkerFactory.create(Type.WAY, latlng, map);
        if (mSelected == null)
            mMarkers.add(marker);
        else {
            mSelected.setIcon(Icons.normal(Type.WAY));
            mMarkers.add(mMarkers.indexOf(mSelected), marker);
        }
        setSelected(marker);
    }

    //If the selected marker is being removed, assign the previous marker to selected.
    private void remove(int index) {
        if (mMarkers.get(index).equals(mSelected))
            setSelected(mMarkers.size() > 1 ? mMarkers.get(index - 1) : null);
        mMarkers.remove(index);
    }

    private void setSelected(@Nullable Marker marker) {
        mSelected = marker;
        if (mSelected != null)
            mSelected.setIcon(Icons.selected(Type.WAY));
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

    private final AppCompatButton mButton;
    private final ArrayList<Marker> mMarkers = new ArrayList<>();
    private Marker mSelected = null;
    private State mState;
}
