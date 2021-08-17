package com.example.robot_path_planner.Events;

public class GenericEvent<T> extends Event {
    public GenericEvent(T data) {
        mData = data;
    }

    public T data() {
        return mData;
    }

    private final T mData;

}
