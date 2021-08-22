package com.smiley98.robot_path_planner;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Icon;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.smiley98.robot_path_planner.Markers.Icons;
import com.smiley98.robot_path_planner.Markers.Markers;
import com.smiley98.robot_path_planner.Markers.Tag;
import com.smiley98.robot_path_planner.databinding.ActivityMapsBinding;

import org.greenrobot.eventbus.Subscribe;

public class MapsActivity extends FragmentActivity implements
    View.OnClickListener,
    OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {
    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private final Markers mMarkers = new Markers();

    private AppCompatButton mBtnWay;
    private AppCompatButton mBtnBoundary;
    private AppCompatButton mBtnObstacle;
    private Markers.Type mMarkerType = Markers.Type.WAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bus.register(this);

        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mBtnWay = binding.btnWay;
        mBtnBoundary = binding.btnBoundary;
        mBtnObstacle = binding.btnObstacle;

        mBtnWay.setOnClickListener(this);
        mBtnBoundary.setOnClickListener(this);
        mBtnObstacle.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentByTag("fragment_maps");
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onDestroy() {
        Bus.unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onTest(TestEvent event) {
        Util.toastShort(this, "Testing " + event.data());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        Icons.init(this);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.6426, -79.3846), 18.0f));
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);

        mMap = map;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnWay:
                mMarkerType = Markers.Type.WAY;
                break;

            case R.id.btnBoundary:
                mMarkerType = Markers.Type.BOUNDARY;
                break;

            case R.id.btnObstacle:
                mMarkerType = Markers.Type.OBSTACLE;
                break;
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Util.toastShort(this, "Marker " + ((Tag) marker.getTag()).id() + " clicked!");
        return false;
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        mMarkers.add(mMarkerType, latLng, mMap);
    }
}