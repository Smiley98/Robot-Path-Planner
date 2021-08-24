package com.smiley98.robot_path_planner.Markers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.smiley98.robot_path_planner.MapsActivity;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Markers extends MarkersBase {
    public Markers(AppCompatButton[] buttons) {
        mButtons = buttons;

        for (int i = 0; i < Type.values().length; i++)
            mMarkers[i] = new TreeMap<>();

        setState(State.ADD, Type.OBSTACLE);
        setState(State.ADD, Type.BOUNDARY);
        setState(State.ADD, Type.WAY);
    }

    public void onMapClick(@NonNull LatLng latLng, GoogleMap map, Context context) {
        if (mStates[mAddType.ordinal()] == State.ADD)
            mSelectedKeys[mAddType.ordinal()] = add(mMarkers[mAddType.ordinal()], mAddType, latLng, map);

        //if (mTest == null)
        //    mTest = map.addPolygon(new PolygonOptions().strokeColor(context.getColor(R.color.red)).fillColor(context.getColor(R.color.red_50)).add(latLng));
        //else
        //    addPoint(mTest, latLng);
    }

    //Set mState to remove and reassign selected on marker click.
    public void onMarkerClick(@NonNull Marker marker) {
        Tag tag = ((Tag) marker.getTag());
        mSelectedKeys[tag.mType.ordinal()] = tag.mId;
        setState(State.REMOVE, tag.mType);
    }

    //Remove marker (if non-null) and State == REMOVE, or set mAddType to ADD on button click.
    public void onMarkerButtonClick(Type type) {
        if (mStates[type.ordinal()] == State.REMOVE) {
            NavigableMap<Long, Marker> map = mMarkers[type.ordinal()];
            Long selectedKey = mSelectedKeys[type.ordinal()];

            if (!map.isEmpty()) {
                if (selectedKey == null)
                    selectedKey = map.lastKey();
                mSelectedKeys[type.ordinal()] = remove(map.get(selectedKey), map);
            }
            if (map.isEmpty())
                setState(State.ADD, type);
        }
        else
            mAddType = type;
    }

    //Reset mState to ADD on button long click.
    public void onMarkerButtonLongClick(Type type) {
        setState(State.ADD, type);
    }

    private void setState(State state, Type type) {
        mButtons[type.ordinal()].setText((state == State.ADD ? "Add " : "Remove ") + type.toString().toLowerCase() + " point");
        mStates[type.ordinal()] = state;
        if (state == State.ADD)
            mAddType = type;
    }

    private void addPoint(Polygon polygon, LatLng latLng) {
        List<LatLng> points = polygon.getPoints();
        if (points.size() < 2)
            points.add(latLng);
        else
            points.add(points.size() - 1, latLng);
        polygon.setPoints(points);
    }

    private final AppCompatButton[] mButtons;
    private final NavigableMap<Long, Marker>[] mMarkers = new TreeMap[Type.values().length];
    private final Long[] mSelectedKeys = new Long[Type.values().length];
    //private Polygon mTest;

    private final State[] mStates = new State[Type.values().length];
    private Type mAddType = Type.WAY;

    private enum State {
        ADD,
        REMOVE
    }

    private static final String TAG = MapsActivity.class.getSimpleName();
}