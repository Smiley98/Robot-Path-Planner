package com.smiley98.robot_path_planner.Markers;

public class Tag {
    public Tag(Icons.Type type, int id) {
        mType = type;
        mId = id;
    }

    public Icons.Type type() { return mType; }
    public int id() { return mId; }

    private final Icons.Type mType;
    private final int mId;
}
