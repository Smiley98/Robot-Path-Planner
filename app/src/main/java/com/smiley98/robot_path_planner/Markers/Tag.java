package com.smiley98.robot_path_planner.Markers;

public class Tag {
    public Tag(Markers.Type type, long id) {
        mType = type;
        mId = id;
    }

    public Markers.Type type() { return mType; }
    public long id() { return mId; }

    private final Markers.Type mType;
    private final long mId;
}
