package com.smiley98.robot_path_planner.Editor.Points;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.smiley98.robot_path_planner.Editor.Common.SerialPoint;
import com.smiley98.robot_path_planner.Editor.Common.Tag;
import com.smiley98.robot_path_planner.Editor.Common.Type;
import com.smiley98.robot_path_planner.Editor.Containers.Polygon;
import com.smiley98.robot_path_planner.Editor.Interfaces.IPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Obstacles implements IPoint {
    public Obstacles(AppCompatButton button) {
        mButton = button;
        setState(State.NEW);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng, GoogleMap map) {
        add(latLng, map);
    }

    @Override
    public void onMarkerClick(@NonNull Marker marker) {
        setState(State.REMOVE);
        mSelectedPolygon = Objects.requireNonNull(mPolygons.get(((Tag) Objects.requireNonNull(marker.getTag())).id()));
        mSelectedPolygon.setSelected(marker);
    }

    @Override
    public void onButtonClick() {
        if (mState == State.REMOVE) {
            if (mSelectedPolygon != null) {
                mSelectedPolygon.remove();
                if (mSelectedPolygon.size() == 0) {
                    mPolygons.remove(mSelectedPolygon.id());
                    setState(State.NEW);
                }
            }
        } else if (mState == State.EDIT)
            setState(State.NEW);
    }

    @Override
    public void onButtonLongClick() {
        if (mSelectedPolygon != null)
            setState(State.EDIT);
        else
            setState(State.NEW);
    }

    public void load(ArrayList<ArrayList<SerialPoint>> polygons, GoogleMap map) {
        for (Polygon geometry : mPolygons.values())
            geometry.clear();

        for (ArrayList<SerialPoint> polygon : polygons) {
            for (SerialPoint point : polygon)
                add(point.latLng(), map);
            setState(State.NEW);
        }
    }

    public ArrayList<ArrayList<SerialPoint>> points() {
        ArrayList<ArrayList<SerialPoint>> result = new ArrayList<>();
        for (Polygon polygon : mPolygons.values()) {
            ArrayList<SerialPoint> polygonPoints = new ArrayList<>();
            for (LatLng latLng : polygon.points())
                polygonPoints.add(new SerialPoint(latLng));
            result.add(polygonPoints);
        }
        return result;
    }

    private void setState(State state) {
        switch (state) {
            case NEW:
                mButton.setText("Begin Obstacle");
                mSelectedPolygon = null;
                break;

            case EDIT:
                mButton.setText("Finish Obstacle");
                break;

            case REMOVE:
                mButton.setText("Remove Obstacle Point");
                break;
        }
        mState = state;
    }

    private void add(@NonNull LatLng latLng, GoogleMap map) {
        if (mState == State.NEW) {
            mSelectedPolygon = new Polygon(Type.OBSTACLE);
            mPolygons.put(mSelectedPolygon.id(), mSelectedPolygon);
            setState(State.EDIT);
        }

        if (mState == State.EDIT)
            mSelectedPolygon.add(latLng, map, mButton.getContext());
    }

    private final AppCompatButton mButton;
    private final Map<Integer, Polygon> mPolygons = new HashMap<>();
    private Polygon mSelectedPolygon = null;
    private State mState;

    private enum State {
        NEW,
        EDIT,
        REMOVE
    }
}
