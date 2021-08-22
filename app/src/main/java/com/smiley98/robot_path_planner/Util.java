package com.smiley98.robot_path_planner;

import android.content.Context;
import android.widget.Toast;

public class Util {
    static void toastShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    static void toastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
