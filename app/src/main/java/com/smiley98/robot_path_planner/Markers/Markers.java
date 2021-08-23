package com.smiley98.robot_path_planner.Markers;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Markers {
    public Markers() {
        for (int i = 0; i < Type.values().length; i++) {
            mMarkers[i] = new TreeMap<>();
            mSelectedMarkers[i] = new WeakReference<>(null);
            mStates[i] = State.ADD;
        }
    }

    public void onMapClick(@NonNull LatLng latLng, GoogleMap map) {
        if (mStates[mAddType.ordinal()] == State.ADD)
            add(mAddType, latLng, map);
    }

    //Set mState to remove on marker click.
    public void onMarkerClick(@NonNull Marker marker, AppCompatButton[] buttons) {
        Tag tag = ((Tag) marker.getTag());
        setState(State.REMOVE, tag.type(), buttons);
        mSelectedMarkers[tag.type().ordinal()] = new WeakReference<>(marker);
    }

    //Remove marker (if non-null) and State == REMOVE, or set mAddType to ADD on button click.
    public void onMarkerButtonClick(AppCompatButton[] buttons, Type type) {
        if (mStates[type.ordinal()] == State.REMOVE) {

            //References corresponding to marker.
            NavigableMap<Long, Marker> map = mMarkers[type.ordinal()];
            Marker selected = mSelectedMarkers[type.ordinal()].get();

            //Remove the previous marker if it exists.
            if (selected != null)
                remove(selected, buttons);

            //Remove most recently added marker if there's no selected marker yet markers of said type still exist.
            else if (!map.isEmpty()) {
                for (Marker marker : map.descendingMap().values()) {
                    remove(marker, buttons);
                    break;
                }
            }
        }
        else
            mAddType = type;
    }

    //Reset mState to ADD on button long click.
    public void onMarkerButtonLongClick(AppCompatButton[] buttons, Type type) {
        setState(State.ADD, type, buttons);
    }

    private void add(Type type, LatLng position, GoogleMap map) {
        Marker marker = map.addMarker(new MarkerOptions().position(position).icon(Icons.descriptor(type)).anchor(0.5f, 0.5f));
        assert marker != null;
        marker.setTag(new Tag(type, ++mMarkerId));
        mMarkers[type.ordinal()].put(mMarkerId, marker);
        mSelectedMarkers[type.ordinal()] = new WeakReference<>(marker);
    }

    private void remove(@NonNull Marker marker, AppCompatButton[] buttons) {
        Tag tag = (Tag) marker.getTag();
        assert tag != null;

        //References corresponding to marker, update selected to previous entry.
        NavigableMap<Long, Marker> map = mMarkers[tag.type().ordinal()];
        Map.Entry<Long, Marker> previous = map.lowerEntry(tag.id());
        mSelectedMarkers[tag.type().ordinal()] = new WeakReference<>(previous == null ? null : previous.getValue());

        //Do removal.
        map.remove(tag.id());
        marker.remove();

        //Set mState to ADD since there's nothing to remove,
        //and change mAddType to type because this is the most recent button press.
        if (map.isEmpty()) {
            setState(State.ADD, tag.type(), buttons);
            mAddType = tag.type();
        }
    }

    private void setState(State state, Type type, AppCompatButton[] buttons) {
        buttons[type.ordinal()].setText((state == State.ADD ? "Add " : "Remove ") + type.toString().toLowerCase() + " point");
        mStates[type.ordinal()] = state;
    }

    private final NavigableMap<Long, Marker>[] mMarkers = new TreeMap[Type.values().length];
    private final WeakReference<Marker>[] mSelectedMarkers = new WeakReference[Type.values().length];
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

    public static class Tag {
        public Tag(Type type, long id) {
            mType = type;
            mId = id;
        }

        public Type type() { return mType; }
        public long id() { return mId; }

        private final Type mType;
        private final long mId;
    }
}
