package com.example.robot_path_planner.Events;

import org.greenrobot.eventbus.EventBus;

public class Bus {
    public static void init() {

    }

    public static void exit() {

    }

    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void post(Event event) {
        EventBus.getDefault().post(event);
    }
}
