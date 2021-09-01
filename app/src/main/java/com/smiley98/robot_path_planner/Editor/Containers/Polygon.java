package com.smiley98.robot_path_planner.Editor.Containers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolygonOptions;
import com.smiley98.robot_path_planner.Editor.Common.Type;
import com.smiley98.robot_path_planner.R;

import java.util.List;

public class Polygon {

    public Polygon(Type type) {
        mMarkers  = new Markers(type);
        mId = ++sId;
    }

    public void add(@NonNull LatLng latLng, GoogleMap map, Context context) {
        mMarkers.add(mId, latLng, map);
        if (mPolygon == null)
            mPolygon = map.addPolygon(options(context).add(latLng));
        else
            mPolygon.setPoints(mMarkers.points());
    }

    public void remove() {
        mMarkers.remove();
        List<LatLng> points = mMarkers.points();

        if (!points.isEmpty())
            mPolygon.setPoints(points);
        else {
            mPolygon.remove();
            mPolygon = null;
        }
    }

    public void clear() {
        if (mPolygon != null)
            mPolygon.remove();
        mPolygon = null;
        mMarkers.clear();
    }

    public void setSelected(@NonNull Marker marker) {
        mMarkers.setSelected(marker);
    }

    public int id() { return mId; }
    public int size() { return mMarkers.size(); }
    public List<LatLng> points() { return mMarkers.points(); }

    private com.google.android.gms.maps.model.Polygon mPolygon;
    private final Markers mMarkers;
    private final int mId;

    private static int sId = 0;

    private PolygonOptions options(Context context) {
        PolygonOptions result = new PolygonOptions();

        switch (mMarkers.type()) {
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
}
