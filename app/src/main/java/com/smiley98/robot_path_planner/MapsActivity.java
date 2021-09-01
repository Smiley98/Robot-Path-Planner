package com.smiley98.robot_path_planner;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;
import com.smiley98.robot_path_planner.Events.Bus;
import com.smiley98.robot_path_planner.Events.Messages.TestEvent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.smiley98.robot_path_planner.Editor.Editor;
import com.smiley98.robot_path_planner.Editor.Common.Icons;
import com.smiley98.robot_path_planner.Editor.Common.Type;
import com.smiley98.robot_path_planner.databinding.ActivityMapsBinding;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements
    View.OnClickListener, View.OnLongClickListener,
    OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {
    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bus.register(this);

        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSave.setOnClickListener(this);
        binding.btnLoad.setOnClickListener(this);
        FileUtils.init(this, binding.btnSave, binding.btnLoad);

        /*
        //Create the storage directory if it does not exist
        File folder = new File(Environment.getExternalStorageDirectory() + "/SampleFolder");
        if (!folder.exists()) {
            if (!folder.mkdirs())
                Log.i(TAG, "failed to create directory");
        }//*/

        AppCompatButton[] pointButtons = new AppCompatButton[Type.values().length];
        pointButtons[Type.WAY.ordinal()] = binding.btnWay;
        pointButtons[Type.BOUNDARY.ordinal()] = binding.btnBoundary;
        pointButtons[Type.OBSTACLE.ordinal()] = binding.btnObstacle;
        for (int i = 0; i < Type.values().length; i++) {
            pointButtons[i].setOnClickListener(this);
            pointButtons[i].setOnLongClickListener(this);
        }

        //Accessing buttons as array reduces legibility in Editor. Only made array to shorten listener setters.
        mEditor = new Editor(binding.btnWay, binding.btnBoundary, binding.btnObstacle);
        SupportMapFragment mapFragment = (SupportMapFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag("fragment_maps"));
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

        //Shout-out to downtown Toronto :D
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.6426, -79.3846), 18.0f));
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);

        mMap = map;
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        mEditor.onMapClick(latLng, mMap);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        mEditor.onMarkerClick(marker);
        return false;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                mEditor.save("test.bin", mMap);
                break;

            case R.id.btnLoad:
                mEditor.load("test.bin", mMap);
                break;

            case R.id.btnWay:
                mEditor.onPointButtonClick(Type.WAY);
                break;

            case R.id.btnBoundary:
                mEditor.onPointButtonClick(Type.BOUNDARY);
                break;

            case R.id.btnObstacle:
                mEditor.onPointButtonClick(Type.OBSTACLE);
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.btnWay:
                mEditor.onPointButtonLongClick(Type.WAY);
                break;

            case R.id.btnBoundary:
                mEditor.onPointButtonLongClick(Type.BOUNDARY);
                break;

            case R.id.btnObstacle:
                mEditor.onPointButtonLongClick(Type.OBSTACLE);
                break;
        }
        return false;
    }

    @Subscribe
    public void onTest(TestEvent event) {
        Toast.makeText(this, "Testing " + event.data(), Toast.LENGTH_SHORT).show();
    }
}