package com.smiley98.robot_path_planner.Markers;

public class Tag {
    public Tag(Type type, long id) {
        mType = type;
        mId = id;
    }

    public Type type() { return mType; }
    public long id() { return mId; }

    private final Type mType;
    private final long mId;
}
