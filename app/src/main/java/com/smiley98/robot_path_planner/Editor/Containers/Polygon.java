package com.smiley98.robot_path_planner.Editor.Containers;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.smiley98.robot_path_planner.Editor.Common.Type;
import com.smiley98.robot_path_planner.Editor.Interfaces.IMarkerResponder;
import com.smiley98.robot_path_planner.R;

import java.util.List;

public class Polygon implements IMarkerResponder {
    public Polygon(Type type) {
        mType = type;
    }

    @Override
    public void onMarkerAdded(List<LatLng> points, GoogleMap map, Context context) {
        if (mPolygon == null)
            mPolygon = map.addPolygon(options(context).addAll(points));
        else
            mPolygon.setPoints(points);
    }

    @Override
    public void onMarkerRemoved(List<LatLng> points) {
        if (points.isEmpty()) {
            mPolygon.remove();
            mPolygon = null;
        } else
            mPolygon.setPoints(points);
    }

    private PolygonOptions options(Context context) {
        PolygonOptions result = new PolygonOptions();
        switch (mType) {
            case WAY:
                result.fillColor(context.getColor(R.color.cyan_50));
                result.strokeColor(context.getColor(R.color.cyan));
                break;

            case BOUNDARY:
                result.fillColor(context.getColor(R.color.red_50));
                result.strokeColor(context.getColor(R.color.red));
                break;

            case OBSTACLE:
                result.fillColor(context.getColor(R.color.green_50));
                result.strokeColor(context.getColor(R.color.green));
                break;
        }

        result.strokeWidth(1.0f);
        result.clickable(false);
        return result;
    }

    private com.google.android.gms.maps.model.Polygon mPolygon = null;
    private final Type mType;
}
