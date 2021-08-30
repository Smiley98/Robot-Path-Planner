package com.smiley98.robot_path_planner.Markers.Common;

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
        sNormal[Type.WAY.ordinal()] = draw(context, R.drawable.marker_way);
        sNormal[Type.BOUNDARY.ordinal()] = draw(context, R.drawable.marker_boundary);
        sNormal[Type.OBSTACLE.ordinal()] = draw(context, R.drawable.marker_obstacle);

        sSelected[Type.WAY.ordinal()] = draw(context, R.drawable.marker_way_selected);
        sSelected[Type.BOUNDARY.ordinal()] = draw(context, R.drawable.marker_boundary_selected);
        sSelected[Type.OBSTACLE.ordinal()] = draw(context, R.drawable.marker_obstacle_selected);
    }

    public static BitmapDescriptor normal(Type type) { return sNormal[type.ordinal()]; }
    public static BitmapDescriptor selected(Type type) { return sSelected[type.ordinal()]; }

    private static final BitmapDescriptor[] sNormal = new BitmapDescriptor[Type.values().length];
    private static final BitmapDescriptor[] sSelected = new BitmapDescriptor[Type.values().length];

    private static BitmapDescriptor draw(Context context, int id) {
        Drawable icon = ContextCompat.getDrawable(context, id);
        icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(icon.getIntrinsicWidth(), icon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        icon.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
