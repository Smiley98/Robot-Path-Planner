package com.smiley98.robot_path_planner;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;
import com.smiley98.robot_path_planner.Events.Bus;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements
    View.OnClickListener, View.OnLongClickListener,
    OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {
    private ActivityMapsBinding mBinding;
    private GoogleMap mMap;
    private Editor mEditor;
    private String mCurrentPath = "";
    private boolean mDeletePath = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bus.register(this);

        mBinding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        requestExternalStorage();

        mBinding.btnSave.setOnClickListener(this);
        mBinding.btnLoad.setOnClickListener(this);
        mBinding.btnCreate.setOnClickListener(this);
        mBinding.btnDelete.setOnClickListener(this);

        mBinding.rcvPaths.setVisibility(View.INVISIBLE);
        mBinding.rcvPaths.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rcvPaths.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mBinding.btnPathOk.setOnClickListener(this);
        for (View view : pathViews())
            view.setVisibility(View.INVISIBLE);

        ArrayList<AppCompatButton> pointButtons = new ArrayList<>();
        pointButtons.add(mBinding.btnWay);
        pointButtons.add(mBinding.btnBoundary);
        pointButtons.add(mBinding.btnObstacle);
        for (View view : pointButtons) {
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        //Accessing buttons as array reduces legibility in Editor. Only made array to shorten listener setters.
        mEditor = new Editor(mBinding.btnWay, mBinding.btnBoundary, mBinding.btnObstacle);
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
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCreate:
                mDeletePath = false;
                show(pathViews(), true);
                show(mBinding.rcvPaths, false);
                break;

            case R.id.btnDelete:
                mDeletePath = true;
                show(pathViews(), false);
                show(mBinding.rcvPaths, true);
                break;

            case R.id.btnPathOk:
                //Verify entered path isn't a duplicate.
                String[] pathNames = FileUtils.root().list();
                String pathText = mBinding.edtEnterPath.getText().toString() + ".path";
                if (pathNames != null) {
                    for (String path : pathNames) {
                        if (path.equals(pathText)) {
                            mBinding.edtEnterPath.setError(path + " already exists");
                            return;
                        }
                    }
                }

                //Attempt to save previous path.
                if (!mCurrentPath.isEmpty() && !mEditor.save(mCurrentPath))
                    onPathError(PathOperation.SAVE, mCurrentPath);

                //Create path, select it, and notify user.
                setCurrentPath(pathText);
                FileUtils.create(pathText);
                updatePathNames(FileUtils.root().list());
                show(pathViews(), false);
                Toast.makeText(this, "Created " + pathText, Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnSave:
                if (mCurrentPath.isEmpty()) {
                    new AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage("No path selected. Please create or load a path.")
                        .setPositiveButton("Ok", null)
                        .show();
                } else {
                    if (!mEditor.isEmpty()) {
                        show(mBinding.rcvPaths, false);
                        show(pathViews(), false);
                        mEditor.save(mCurrentPath);
                        Toast.makeText(this, "Saved " + mCurrentPath, Toast.LENGTH_SHORT).show();
                    } else
                        new androidx.appcompat.app.AlertDialog.Builder(this)
                            .setTitle("Error")
                            .setMessage("Cannot save path without way points.")
                            .setPositiveButton("Ok", null)
                            .show();
                }
                break;

            case R.id.btnLoad:
                pathNames = FileUtils.root().list();
                if (pathNames != null && pathNames.length != 0)
                    show(mBinding.rcvPaths, true);
                else
                    new AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage("No paths exist. Please create a path.")
                        .setPositiveButton("Ok", null)
                        .show();
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
        return true;
    }

    @Subscribe
    public void onItemClick(PathNameView.ItemClickEvent event) {
        Toast.makeText(this, (mDeletePath ? "Deleted " : "Loaded ") + event.data(), Toast.LENGTH_SHORT).show();
        if (mDeletePath)
            deletePath(event.data());
        else {
            setCurrentPath(event.data());
            if (!mEditor.load(mCurrentPath, mMap))
                onPathError(PathOperation.LOAD, mCurrentPath);
        }
    }

    private ArrayList<View> pathViews() {
        ArrayList<View> views = new ArrayList<>();
        views.add(mBinding.txtEnterPath);
        views.add(mBinding.edtEnterPath);
        views.add(mBinding.btnPathOk);
        return views;
    }

    private void show(List<View> views, boolean show) {
        for (View view : views)
            show(view, show);
    }

    private void show(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private void updatePathNames(@Nullable String[] pathNames) {
        mBinding.rcvPaths.setAdapter(new PathNameView(pathNames != null ? Arrays.asList(pathNames) : new ArrayList<>()));
    }

    private void setCurrentPath(String path) {
        mCurrentPath = path;
        mBinding.txtCurrentPath.setText("Current Path: " + mCurrentPath);
    }

    private void onPathError(PathOperation operation, String path) {
        deletePath(path);
        new AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage("Failed to " + operation.toString().toLowerCase() + " " + path + ". " + path + " has been deleted.")
            .setPositiveButton("Ok", null)
            .show();
    }

    private void deletePath(String path) {
        if (path.equals(mCurrentPath)) {
            setCurrentPath("");
            mEditor.clear();
        }

        FileUtils.create(path).delete();
        updatePathNames(FileUtils.root().list());
    }

    private enum PathOperation {
        SAVE,
        LOAD
    }

    private void requestExternalStorage() {
        ActivityResultLauncher<Intent> intent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            (ActivityResult result) -> {
                boolean granted = Environment.isExternalStorageManager();
                mBinding.btnSave.setEnabled(granted);
                mBinding.btnLoad.setEnabled(granted);
                mBinding.btnCreate.setEnabled(granted);
                mBinding.btnDelete.setEnabled(granted);

                if (granted)
                    onExternalStorageGranted();
                else {
                    new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("External storage access denied. Saving and loading has been disabled.")
                        .setPositiveButton("Ok", null)
                        .show();
                }
            }
        );

        if (!Environment.isExternalStorageManager())
            intent.launch(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));
        else
            onExternalStorageGranted();
    }

    private void onExternalStorageGranted() {
        FileUtils.root().mkdirs();
        updatePathNames(FileUtils.root().list());
    }
}