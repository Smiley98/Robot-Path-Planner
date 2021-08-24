package com.smiley98.robot_path_planner.Markers;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;
import java.util.NavigableMap;

public class MarkersBase {

    //Returns the key of the added marker.
    protected static Long add(NavigableMap<Long, Marker> markers, Type type, LatLng position, GoogleMap map) {
        Marker marker = map.addMarker(new MarkerOptions().position(position).icon(Icons.descriptor(type)).anchor(0.5f, 0.5f));
        assert marker != null;
        marker.setTag(new Tag(type, ++sId));
        markers.put(sId, marker);
        return sId;
    }

    //Returns the key of the marker previous to the removed marker.
    protected static Long remove(@NonNull Marker marker, NavigableMap<Long, Marker> markers) {
        Tag tag = (Tag) marker.getTag();
        assert tag != null;
        Map.Entry<Long, Marker> previous = markers.lowerEntry(tag.mId);
        markers.remove(tag.mId);
        marker.remove();
        return previous == null ? null : previous.getKey();
    }

    protected static class Tag {
        public Tag(Type type, long id) {
            mType = type;
            mId = id;
        }

        protected final Type mType;
        protected final long mId;
    }

    public enum Type {
        WAY,
        BOUNDARY,
        OBSTACLE
    }

    private static long sId = 0L;
}
