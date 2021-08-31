package com.smiley98.robot_path_planner.Editor.Common;

public class Tag {
    public Tag(Type type) {
        mType = type;
        mId = -1;
    }

    public Tag(Type type, int id) {
        mType = type;
        mId = id;
    }

    public Type type() { return mType; }
    public int id() { return mId; }

    private final Type mType;
    private final int mId;
}
