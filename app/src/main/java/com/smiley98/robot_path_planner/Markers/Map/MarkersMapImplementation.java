package com.smiley98.robot_path_planner.Markers.Map;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.smiley98.robot_path_planner.MapsActivity;
import com.smiley98.robot_path_planner.Markers.IMarkers;
import com.smiley98.robot_path_planner.Markers.Tag;
import com.smiley98.robot_path_planner.Markers.Type;
import com.smiley98.robot_path_planner.R;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

//This class serves as a reference for how to achieve basic operations (add & remove without complex cases).
//Learned the hard way that my editor should use an array-like container as elements need to be logically contiguous.
public class MarkersMapImplementation extends MarkersMapBase implements IMarkers {
    public MarkersMapImplementation(AppCompatButton[] buttons) {
        mButtons = buttons;

        for (int i = 0; i < Type.values().length; i++)
            mMarkers[i] = new TreeMap<>();

        setState(State.ADD, Type.OBSTACLE);
        setState(State.ADD, Type.BOUNDARY);
        setState(State.ADD, Type.WAY);
    }

    public void onMapClick(@NonNull LatLng latLng, GoogleMap map, Context context) {
        final int index = mAddType.ordinal();
        if (mStates[index] == State.ADD) {
            mSelectedKeys[index] = add(mMarkers[mAddType.ordinal()], mAddType, latLng, map);
            mLines[index] = addPoint(mLines[index], mAddType, latLng, map, context);
            //mPolygons[mAddType.ordinal()] = addPoint(mPolygons[mAddType.ordinal()], mAddType, latLng, map, context);
        }
    }

    //Set mState to remove and reassign selected on marker click.
    public void onMarkerClick(@NonNull Marker marker) {
        Tag tag = ((Tag) marker.getTag());
        mSelectedKeys[tag.type().ordinal()] = tag.id();
        setState(State.REMOVE, tag.type());
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

                if (mLines[type.ordinal()] != null) {
                    List<LatLng> points = new ArrayList<>();
                    for (Marker marker: map.values())
                        points.add(marker.getPosition());
                    mLines[type.ordinal()].setPoints(points);
                }
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

    private Polygon addPoint(@Nullable Polygon polygon, Type type, LatLng latLng, GoogleMap map, Context context) {
        if (polygon == null) {
            int fillColour = 0, strokeColour = 0;
            switch (type) {
                case BOUNDARY:
                    fillColour = context.getColor(R.color.red_50);
                    strokeColour = context.getColor(R.color.red);
                    break;

                case OBSTACLE:
                    fillColour = context.getColor(R.color.green_50);
                    strokeColour = context.getColor(R.color.green);
                    break;
            }
            return map.addPolygon(new PolygonOptions().fillColor(fillColour).strokeColor(strokeColour).add(latLng));
        }

        List<LatLng> points = polygon.getPoints();
        if (points.size() < 2)
            points.add(latLng);
        else
            points.add(points.size() - 1, latLng);
        polygon.setPoints(points);
        return polygon;
    }

    private Polyline addPoint(@Nullable Polyline polyline, Type type, LatLng latLng, GoogleMap map, Context context) {
        if (polyline == null) {
            int colour = 0;
            switch (type) {
                case WAY:
                    colour = context.getColor(R.color.blue);
                    break;

                case BOUNDARY:
                    colour = context.getColor(R.color.red);
                    break;

                case OBSTACLE:
                    colour = context.getColor(R.color.green);
                    break;
            }
            return map.addPolyline(new PolylineOptions().color(colour).clickable(false).width(1.0f).add(latLng));
        }

        List<LatLng> points = polyline.getPoints();
        points.add(latLng);
        polyline.setPoints(points);
        return polyline;
    }

    private final AppCompatButton[] mButtons;
    private final Polyline[] mLines = new Polyline[Type.values().length];
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