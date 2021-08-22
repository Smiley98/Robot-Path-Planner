package com.smiley98.robot_path_planner.Events;

import com.smiley98.robot_path_planner.EventBusIndex;
import org.greenrobot.eventbus.EventBus;

public class Bus {
    public static void init() {
        EventBus.builder().addIndex(new EventBusIndex()).installDefaultEventBus();
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
