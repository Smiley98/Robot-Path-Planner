package com.smiley98.robot_path_planner.Editor.Containers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.smiley98.robot_path_planner.Editor.Common.Type;
import com.smiley98.robot_path_planner.R;

import java.util.List;

public class Polygon extends Markers {

    public Polygon(Type type) {
        super(type);
        mId = ++sId;
    }

    public void add(@NonNull LatLng latLng, GoogleMap map, Context context) {
        super.add(mId, latLng, map);
        if (mPolygon == null)
            mPolygon = map.addPolygon(options(context).add(latLng));
        else
            mPolygon.setPoints(points());
    }

    public void remove() {
        super.remove();
        List<LatLng> points = points();

        if (!points.isEmpty())
            mPolygon.setPoints(points);
        else {
            mPolygon.remove();
            mPolygon = null;
        }
    }

    public void clear() {
        super.clear();
        if (mPolygon != null)
            mPolygon.remove();
        mPolygon = null;
    }

    public int id() { return mId; }

    private com.google.android.gms.maps.model.Polygon mPolygon;
    private final int mId;

    private static int sId = 0;

    private PolygonOptions options(Context context) {
        PolygonOptions result = new PolygonOptions();

        switch (type()) {
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
