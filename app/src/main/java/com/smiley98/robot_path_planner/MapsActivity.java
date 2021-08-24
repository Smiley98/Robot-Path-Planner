package com.smiley98.robot_path_planner;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.model.Marker;
import com.smiley98.robot_path_planner.Events.Bus;
import com.smiley98.robot_path_planner.Events.Messages.TestEvent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.smiley98.robot_path_planner.Markers.Icons;
import com.smiley98.robot_path_planner.Markers.Markers;
import com.smiley98.robot_path_planner.Markers.Type;
import com.smiley98.robot_path_planner.databinding.ActivityMapsBinding;

import org.greenrobot.eventbus.Subscribe;

public class MapsActivity extends FragmentActivity implements
    View.OnClickListener, View.OnLongClickListener,
    OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {
    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private Markers mMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bus.register(this);

        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppCompatButton[] pointButtons = new AppCompatButton[Type.values().length];
        pointButtons[Type.WAY.ordinal()] = binding.btnWay;
        pointButtons[Type.BOUNDARY.ordinal()] = binding.btnBoundary;
        pointButtons[Type.OBSTACLE.ordinal()] = binding.btnObstacle;
        for (int i = 0; i < Type.values().length; i++) {
            pointButtons[i].setOnClickListener(this);
            pointButtons[i].setOnLongClickListener(this);
        }

        mMarkers = new Markers(pointButtons);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentByTag("fragment_maps");
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onDestroy() {
        Bus.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        Icons.init(this);
        onTest(new TestEvent(123));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.6426, -79.3846), 18.0f));
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);

        mMap = map;
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        mMarkers.onMapClick(latLng, mMap, this);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        mMarkers.onMarkerClick(marker);
        return false;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnWay:
                mMarkers.onMarkerButtonClick(Type.WAY);
                break;

            case R.id.btnBoundary:
                mMarkers.onMarkerButtonClick(Type.BOUNDARY);
                break;

            case R.id.btnObstacle:
                mMarkers.onMarkerButtonClick(Type.OBSTACLE);
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.btnWay:
                mMarkers.onMarkerButtonLongClick(Type.WAY);
                break;

            case R.id.btnBoundary:
                mMarkers.onMarkerButtonLongClick(Type.BOUNDARY);
                break;

            case R.id.btnObstacle:
                mMarkers.onMarkerButtonLongClick(Type.OBSTACLE);
                break;
        }
        return false;
    }

    @Subscribe
    public void onTest(TestEvent event) {
        Util.toastShort(this, "Testing " + event.data());
    }
}