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
import com.google.android.gms.maps.model.MarkerOptions;
import com.smiley98.robot_path_planner.Markers.Icons;
import com.smiley98.robot_path_planner.databinding.ActivityMapsBinding;

import org.greenrobot.eventbus.Subscribe;

public class MapsActivity extends FragmentActivity implements
    View.OnClickListener,
    OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private AppCompatButton mBtnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bus.register(this);

        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mBtnTest = binding.btnTest;
        mBtnTest.setOnClickListener(this);

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
        mMap.setOnMarkerClickListener(this);

        LatLng sydney = new LatLng(43.6426, -79.3846);
        mMap.addMarker(new MarkerOptions()
            .position(sydney)
            .icon(Icons.descriptor(Icons.Type.WAY))
            .anchor(0.5f, 0.5f)
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18.0f));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnTest:
                Bus.post(new TestEvent(123));
                break;
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Util.toastShort(this, "Marker clicked!");
        return false;
    }
}