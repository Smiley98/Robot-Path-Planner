package com.example.robot_path_planner;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.robot_path_planner.Events.Bus;
import com.example.robot_path_planner.Events.Messages.TestEvent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.robot_path_planner.databinding.ActivityMapsBinding;

import org.greenrobot.eventbus.Subscribe;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private AppCompatButton mBtnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bus.register(this);

        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bus.post(new TestEvent(5));

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
        Log.i(TAG, event.data().toString());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}