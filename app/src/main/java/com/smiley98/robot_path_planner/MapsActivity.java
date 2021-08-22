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
    private Icons.Type mMarkerType = Icons.Type.WAY;

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
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Icons.init(this);

        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);

        LatLng defaultLocation = new LatLng(43.6426, -79.3846);
        /*mMap.addMarker(new MarkerOptions()
            .position(defaultLocation)
            .icon(Icons.descriptor(Icons.Type.WAY))
            .anchor(0.5f, 0.5f)
        );*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 18.0f));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnWay:
                mMarkerType = Icons.Type.WAY;
                break;

            case R.id.btnBoundary:
                mMarkerType = Icons.Type.BOUNDARY;
                break;

            case R.id.btnObstacle:
                mMarkerType = Icons.Type.OBSTACLE;
                break;
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Util.toastShort(this, "Marker clicked!");
        return false;
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        mMarkers.add(mMarkerType, latLng, mMap);
    }
}