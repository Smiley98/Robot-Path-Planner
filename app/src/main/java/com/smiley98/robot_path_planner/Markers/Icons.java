package com.smiley98.robot_path_planner.Markers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.smiley98.robot_path_planner.R;

public class Icons {

    public static void init(Context context) {
        sWay = draw(context, R.drawable.marker_way);
        sBoundary = draw(context, R.drawable.marker_boundary);
        sObstacle = draw(context, R.drawable.marker_obstacle);
    }

    public static BitmapDescriptor descriptor(Type type) {
        switch (type) {
            case WAY:
                return sWay;

            case BOUNDARY:
                return sBoundary;

            case OBSTACLE:
                return sObstacle;
        }
        return null;
    }

    private static BitmapDescriptor sWay;
    private static BitmapDescriptor sBoundary;
    private static BitmapDescriptor sObstacle;

    private static BitmapDescriptor draw(Context context, int id) {
        Drawable icon = ContextCompat.getDrawable(context, id);
        icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(icon.getIntrinsicWidth(), icon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        icon.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
