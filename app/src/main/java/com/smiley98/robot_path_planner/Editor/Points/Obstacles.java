package com.smiley98.robot_path_planner.Editor.Points;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.smiley98.robot_path_planner.Editor.Common.Type;
import com.smiley98.robot_path_planner.Editor.Containers.Polygon;
import com.smiley98.robot_path_planner.Editor.Interfaces.IPoint;

import java.util.HashMap;
import java.util.Map;

public class Obstacles implements IPoint {
    public Obstacles(AppCompatButton button) {
        mButton = button;
        setState(State.ADD);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng, GoogleMap map) {
        if (mState == State.ADD) {
            if (mSelectedPolygon == null)
                mSelectedPolygon = new Polygon(Type.OBSTACLE);

            mSelectedPolygon.add(latLng, map, mButton.getContext());

            if (!mPolygons.containsKey(mSelectedPolygon.id()))
                mPolygons.put(mSelectedPolygon.id(), mSelectedPolygon);
        }
    }

    @Override
    public void onMarkerClick(@NonNull Marker marker) {
        setState(State.REMOVE);
        Polygon.find(marker).setSelected(marker);
    }

    @Override
    public void onButtonClick() {
        if (mState == State.REMOVE && mSelectedPolygon != null) {
            mSelectedPolygon.remove();

            if (mSelectedPolygon.size() == 0) {
                mPolygons.remove(mSelectedPolygon.id());
                mSelectedPolygon = null;
            }
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

    private final AppCompatButton mButton;
    private final Map<Integer, Polygon> mPolygons = new HashMap<>();
    private Polygon mSelectedPolygon = null;
    private State mState;

    private enum State {
        ADD,
        REMOVE
    }
}
