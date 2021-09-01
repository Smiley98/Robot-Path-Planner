package com.smiley98.robot_path_planner.Editor;

import com.smiley98.robot_path_planner.Editor.Common.SerialPoint;

import java.io.Serializable;
import java.util.ArrayList;

public class EditorFile implements Serializable {
    public EditorFile(ArrayList<SerialPoint> wayPoints, ArrayList<SerialPoint> boundaryPoints, ArrayList<ArrayList<SerialPoint>> obstacles) {
        mWayPoints = wayPoints;
        mBoundaryPoints = boundaryPoints;
        mObstacles = obstacles;
    }

    public ArrayList<SerialPoint> wayPoints() { return mWayPoints; }
    public ArrayList<SerialPoint> boundaryPoints() { return mBoundaryPoints; }
    public ArrayList<ArrayList<SerialPoint>> obstacles() { return mObstacles; }

    private final ArrayList<SerialPoint> mWayPoints;
    private final ArrayList<SerialPoint> mBoundaryPoints;
    private final ArrayList<ArrayList<SerialPoint>> mObstacles;
}
