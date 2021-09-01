package com.smiley98.robot_path_planner.Editor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.smiley98.robot_path_planner.FileUtils;

import java.util.Objects;

public class Editor {

    public Editor(AppCompatButton wayButton, AppCompatButton boundaryButton, AppCompatButton obstaclesButton) {
        mWay = new Way(wayButton);
        mBoundary = new Boundary(boundaryButton);
        mObstacles = new Obstacles(obstaclesButton);
    }

    public void save(String name, GoogleMap map) {
        if (!mWay.points().isEmpty()) {
            FileUtils.serialize(FileUtils.create(name), new EditorFile(mWay.points(), mBoundary.points(), mObstacles.points()));
            map.clear();
        } else
            new AlertDialog.Builder(mWay.context()).setTitle("Error").setMessage("Cannot save path without way points.").setPositiveButton("Ok", null).show();
    }

    public void load(String name, GoogleMap map) {
        EditorFile serial = FileUtils.deserialize(FileUtils.create(name));
        mWay.load(serial.wayPoints(), map);
        mBoundary.load(serial.boundaryPoints(), map);
        mObstacles.load(serial.obstacles(), map);
    }

    //Add
    public void onMapClick(@NonNull LatLng latLng, GoogleMap map) {
        point(mType).onMapClick(latLng, map);
    }

    //Set
    public void onMarkerClick(@NonNull Marker marker) {
        Type type = ((Tag) Objects.requireNonNull(marker.getTag())).type();
        point(type).onMarkerClick(marker);
        mType = type;
    }

    //Remove
    public void onPointButtonClick(Type type) {
        point(type).onButtonClick();
        mType = type;
    }

    //Reset
    public void onPointButtonLongClick(Type type) {
        point(type).onButtonLongClick();
        mType = type;
    }

    private IPoint point(Type type) {
        return new IPoint[] { mWay, mBoundary, mObstacles }[type.ordinal()];
    }

    private final Way mWay;
    private final Boundary mBoundary;
    private final Obstacles mObstacles;
    private Type mType = Type.WAY;
}
