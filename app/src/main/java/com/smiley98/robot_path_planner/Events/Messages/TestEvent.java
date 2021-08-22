package com.smiley98.robot_path_planner.Events.Messages;

import com.smiley98.robot_path_planner.Events.GenericEvent;

public class TestEvent extends GenericEvent<Integer> {
    public TestEvent(int data) {
        super(data);
    }
}
