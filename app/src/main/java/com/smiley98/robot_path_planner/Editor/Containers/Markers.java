package com.smiley98.robot_path_planner.Editor.Containers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smiley98.robot_path_planner.Editor.Common.Icons;
import com.smiley98.robot_path_planner.Editor.Common.Tag;
import com.smiley98.robot_path_planner.Editor.Common.Type;

import java.util.ArrayList;
import java.util.Objects;

public class Markers {
    public Markers(Type type) { mType = type; }

    public Type type() { return mType; }
    public Marker selected() { return mSelected; }
    public Marker get(int index) { return mMarkers.get(index); }
    public int size() { return mMarkers.size(); }

    public void setSelected(@Nullable Marker marker) {
        //Revert previously selected icon.
        if (mSelected != null)
            mSelected.setIcon(Icons.normal(mType));

        mSelected = marker;

        //Update newly selected icon.
        if (mSelected != null)
            mSelected.setIcon(Icons.selected(mType));
    }

    public void add(LatLng latlng, GoogleMap map) {
        Marker marker = Objects.requireNonNull(map.addMarker(new MarkerOptions().position(latlng).icon(Icons.normal(mType)).anchor(0.5f, 0.5f)));
        marker.setTag(new Tag(mType));

        //Append to back if no selected marker, or if selected marker is last marker.
        if (mSelected == null || mSelected.equals(mMarkers.get(mMarkers.size() - 1)))
            mMarkers.add(marker);
        else
            mMarkers.add(mMarkers.indexOf(mSelected) + 1, marker);

        setSelected(marker);
    }

    public void add(int id, LatLng latlng, GoogleMap map) {
        add(latlng, map);
        mSelected.setTag(new Tag(mType, id));
    }

    //Remove selected if non-null, otherwise remove back marker.
    public void remove() {
        if (!mMarkers.isEmpty()) {
            if (mSelected == null )
                setSelected(mMarkers.get(mMarkers.size() - 1));
            remove(mSelected);
        }
    }

    public void clear() {
        mSelected = null;
        for (Marker marker : mMarkers)
            marker.remove();
        mMarkers.clear();
    }

    private void remove(@NonNull Marker marker) {
        setSelected(previous(marker));
        mMarkers.remove(marker);
        marker.remove();
    }

    public ArrayList<LatLng> points() {
        ArrayList<LatLng> result = new ArrayList<>();
        for (Marker marker : mMarkers)
            result.add(marker.getPosition());
        return result;
    }

    private Marker previous(@Nullable Marker marker) {
        int index = mMarkers.indexOf(marker);
        if (index == -1 || mMarkers.size() <= 1) return null;
        return index == 0 ? mMarkers.get(mMarkers.size() - 1) : mMarkers.get(index - 1);
    }

    private final Type mType;
    private final ArrayList<Marker> mMarkers = new ArrayList<>();
    private Marker mSelected = null;
}