package com.smiley98.robot_path_planner.Markers;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smiley98.robot_path_planner.MapsActivity;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Markers {
    public Markers(AppCompatButton[] buttons) {
        mButtons = buttons;

        for (int i = 0; i < Type.values().length; i++)
            mMarkers[i] = new TreeMap<>();

        setState(State.ADD, Type.OBSTACLE);
        setState(State.ADD, Type.BOUNDARY);
        setState(State.ADD, Type.WAY);
    }

    public void onMapClick(@NonNull LatLng latLng, GoogleMap map) {
        if (mStates[mAddType.ordinal()] == State.ADD)
            add(mAddType, latLng, map);
    }

    //Set mState to remove on marker click.
    public void onMarkerClick(@NonNull Marker marker) {
        Tag tag = ((Tag) marker.getTag());
        setState(State.REMOVE, tag.mType);
        mSelectedMarkers[tag.mType.ordinal()] = marker;
    }

    //Remove marker (if non-null) and State == REMOVE, or set mAddType to ADD on button click.
    public void onMarkerButtonClick(Type type) {
        if (mStates[type.ordinal()] == State.REMOVE) {

            //References corresponding to marker.
            NavigableMap<Long, Marker> map = mMarkers[type.ordinal()];
            Marker selected = mSelectedMarkers[type.ordinal()];

            //Remove the previous marker if it exists.
            if (selected != null)
                remove(selected);

            //Remove most recently added marker if there's no selected marker yet markers of said type still exist.
            else if (!map.isEmpty()) {
                for (Marker marker : map.descendingMap().values()) {
                    remove(marker);
                    break;
                }
            }
        }
        else
            mAddType = type;
    }

    //Reset mState to ADD on button long click.
    public void onMarkerButtonLongClick(Type type) {
        setState(State.ADD, type);
    }

    private void add(Type type, LatLng position, GoogleMap map) {
        Marker marker = map.addMarker(new MarkerOptions().position(position).icon(Icons.descriptor(type)).anchor(0.5f, 0.5f));
        assert marker != null;
        marker.setTag(new Tag(type, ++mMarkerId));
        mMarkers[type.ordinal()].put(mMarkerId, marker);
        mSelectedMarkers[type.ordinal()] = marker;
    }

    private void remove(@NonNull Marker marker) {
        Tag tag = (Tag) marker.getTag();
        assert tag != null;

        //References corresponding to marker, update selected to previous entry.
        NavigableMap<Long, Marker> map = mMarkers[tag.mType.ordinal()];
        Map.Entry<Long, Marker> previous = map.lowerEntry(tag.mId);
        mSelectedMarkers[tag.mType.ordinal()] = previous == null ? null : previous.getValue();

        //Do removal.
        map.remove(tag.mId);
        marker.remove();

        //Set mState to ADD since there's nothing to remove,
        //and change mAddType to type because this is the most recent button press.
        if (map.isEmpty()) {
            setState(State.ADD, tag.mType);
            mAddType = tag.mType;
        }
    }

    private void setState(State state, Type type) {
        mButtons[type.ordinal()].setText((state == State.ADD ? "Add " : "Remove ") + type.toString().toLowerCase() + " point");
        mStates[type.ordinal()] = state;
    }

    private final AppCompatButton[] mButtons;
    private final NavigableMap<Long, Marker>[] mMarkers = new TreeMap[Type.values().length];
    private final Marker[] mSelectedMarkers = new Marker[Type.values().length];

    private final State[] mStates = new State[Type.values().length];
    private Type mAddType = Type.WAY;
    private long mMarkerId = 0;

    public enum Type {
        WAY,
        BOUNDARY,
        OBSTACLE
    }

    private enum State {
        ADD,
        REMOVE
    }

    private static class Tag {
        public Tag(Type type, long id) {
            mType = type;
            mId = id;
        }

        private final Type mType;
        private final long mId;
    }

    private static final String TAG = MapsActivity.class.getSimpleName();
}
