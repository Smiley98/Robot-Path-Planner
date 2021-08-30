package com.smiley98.robot_path_planner.Editor.Containers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smiley98.robot_path_planner.Editor.Common.Icons;
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
        mSelected = marker;
        if (mSelected != null)
            mSelected.setIcon(Icons.selected(mType));
    }

    public void add(LatLng latlng, GoogleMap map) {
        Marker marker = Objects.requireNonNull(map.addMarker(new MarkerOptions().position(latlng).icon(Icons.normal(mType)).anchor(0.5f, 0.5f)));
        marker.setTag(mType);

        if (mSelected == null)
            mMarkers.add(marker);
        else if (mSelected.equals(mMarkers.get(mMarkers.size() - 1))) {
            mSelected.setIcon(Icons.normal(mType));
            mMarkers.add(marker);
        } else {
            mSelected.setIcon(Icons.normal(mType));
            mMarkers.add(mMarkers.indexOf(mSelected) + 1, marker);
        }

        setSelected(marker);
    }

    public void remove(@NonNull Marker marker) {
        if (marker.equals(mSelected))
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

    private final ArrayList<Marker> mMarkers = new ArrayList<>();
    private Marker mSelected = null;
    private final Type mType;
    private static final String TAG = Markers.class.getSimpleName();
}