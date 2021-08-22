package com.smiley98.robot_path_planner;

import android.app.Application;

import com.smiley98.robot_path_planner.Events.Bus;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Bus.init();
    }

    public void exit() {
        Bus.exit();
        System.exit(0);
    }
}
