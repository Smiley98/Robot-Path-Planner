package com.smiley98.robot_path_planner.Editor.Containers;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.smiley98.robot_path_planner.Editor.Common.Type;
import com.smiley98.robot_path_planner.R;

import java.util.List;

public class Lines {
    public Lines(Type type) {
        mType = type;
    }

    public void onMarkerAdded(List<LatLng> points, GoogleMap map, Context context) {
        if (mLine == null)
            mLine = map.addPolyline(options(context));
        mLine.setPoints(points);
    }

    public void onMarkerRemoved(List<LatLng> points) {
        mLine.setPoints(points);
    }

    private PolylineOptions options(Context context) {
        PolylineOptions result = new PolylineOptions();
        switch (mType) {
            case WAY:
                result.color(context.getColor(R.color.cyan));
                result.jointType(JointType.DEFAULT);
                break;

            case BOUNDARY:
                result.color(context.getColor(R.color.red));
                result.jointType(JointType.BEVEL);
                break;

            case OBSTACLE:
                result.color(context.getColor(R.color.green));
                result.jointType(JointType.ROUND);
                break;
        }

        result.width(1.0f);
        result.clickable(false);
        return result;
    }

    private Polyline mLine = null;
    private final Type mType;
}
