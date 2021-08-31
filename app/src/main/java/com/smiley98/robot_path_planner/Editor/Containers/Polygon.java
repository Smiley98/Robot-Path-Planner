package com.smiley98.robot_path_planner.Editor.Containers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolygonOptions;
import com.smiley98.robot_path_planner.Editor.Common.Tag;
import com.smiley98.robot_path_planner.Editor.Common.Type;
import com.smiley98.robot_path_planner.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Polygon {

    public Polygon(Type type) {
        mMarkers  = new Markers(type);
    }

    public void add(@NonNull LatLng latLng, GoogleMap map, Context context) {
        if (mPolygon == null) {
            mPolygon = map.addPolygon(options(context).add(latLng));
            mId = ++sId;
            sPolygons.put(mId, this);
            mMarkers.add(mId, latLng, map);
        } else {
            mMarkers.add(mId, latLng, map);
            mPolygon.setPoints(mMarkers.points());
        }
    }

    public void remove() {
        mMarkers.remove();
        List<LatLng> points = mMarkers.points();

        if (!points.isEmpty())
            mPolygon.setPoints(points);
        else {
            sPolygons.remove(mId);
            mPolygon.remove();
            mPolygon = null;
        }
    }

    public void setSelected(@NonNull Marker marker) {
        mMarkers.setSelected(marker);
    }

    public int id() { return mId; }
    public int size() { return mMarkers.size(); }

    public static Polygon find(@NonNull Marker marker) { return sPolygons.get(((Tag) Objects.requireNonNull(marker.getTag())).id()); }

    private final Markers mMarkers;
    private com.google.android.gms.maps.model.Polygon mPolygon;
    private int mId;

    private static int sId = 0;
    private static final Map<Integer, Polygon> sPolygons = new HashMap<>();

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
