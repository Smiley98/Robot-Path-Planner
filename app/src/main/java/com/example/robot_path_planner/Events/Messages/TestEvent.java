package com.example.robot_path_planner.Events.Messages;

import com.example.robot_path_planner.Events.GenericEvent;

public class TestEvent extends GenericEvent<Integer> {
    public TestEvent(int data) {
        super(data);
    }
}
