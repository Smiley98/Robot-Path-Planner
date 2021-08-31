package com.smiley98.robot_path_planner.Editor;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.smiley98.robot_path_planner.Editor.Common.Tag;
import com.smiley98.robot_path_planner.Editor.Common.Type;
import com.smiley98.robot_path_planner.Editor.Interfaces.IPoint;
import com.smiley98.robot_path_planner.Editor.Points.Boundary;
import com.smiley98.robot_path_planner.Editor.Points.Obstacles;
import com.smiley98.robot_path_planner.Editor.Points.Way;

import java.util.Objects;

public class Editor {

    public Editor(AppCompatButton[] pointButtons) {
        mPoints[Type.WAY.ordinal()] = new Way(pointButtons[Type.WAY.ordinal()]);
        mPoints[Type.BOUNDARY.ordinal()] = new Boundary(pointButtons[Type.BOUNDARY.ordinal()]);
        mPoints[Type.OBSTACLE.ordinal()] = new Obstacles(pointButtons[Type.OBSTACLE.ordinal()]);
    }

    //Add
    public void onMapClick(@NonNull LatLng latLng, GoogleMap map) {
        mPoints[mType.ordinal()].onMapClick(latLng, map);
    }

    //Set
    public void onMarkerClick(@NonNull Marker marker) {
        Type type = ((Tag) Objects.requireNonNull(marker.getTag())).type();
        mPoints[type.ordinal()].onMarkerClick(marker);
        mType = type;
    }

    //Remove
    public void onPointButtonClick(Type type) {
        mPoints[type.ordinal()].onButtonClick();
        mType = type;
    }

    //Reset
    public void onPointButtonLongClick(Type type) {
        mPoints[type.ordinal()].onButtonLongClick();
        mType = type;
    }

    private final IPoint[] mPoints = new IPoint[Type.values().length];
    private Type mType = Type.WAY;
}
