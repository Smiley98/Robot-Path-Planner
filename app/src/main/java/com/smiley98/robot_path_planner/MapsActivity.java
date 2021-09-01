package com.smiley98.robot_path_planner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;
import com.smiley98.robot_path_planner.Events.Bus;
import com.smiley98.robot_path_planner.Events.GenericEvent;
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
    private GoogleMap mMap;
    private Editor mEditor;
    private RecyclerView mPathNamesView;
    private AppCompatTextView mTxtCurrentPath;
    private String mCurrentPath = "";
    private boolean mDeletePath = false;

    private AppCompatTextView mTxtEnterPath;
    private AppCompatEditText mEdtEnterPath;
    private AppCompatButton mBtnPathOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bus.register(this);

        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSave.setOnClickListener(this);
        binding.btnLoad.setOnClickListener(this);
        FileUtils.init(this, binding.btnSave, binding.btnLoad);
        binding.btnCreate.setOnClickListener(this);
        binding.btnDelete.setOnClickListener(this);

        mTxtCurrentPath = binding.txtCurrentPath;
        mPathNamesView = binding.rcvPaths;
        mPathNamesView.setVisibility(View.INVISIBLE);
        mPathNamesView.setLayoutManager(new LinearLayoutManager(this));
        mPathNamesView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        updatePathNames(FileUtils.root().list());

        mTxtEnterPath = binding.txtEnterPath;
        mEdtEnterPath = binding.edtEnterPath;
        mBtnPathOk = binding.btnPathOk;
        mBtnPathOk.setOnClickListener(this);
        for (View view : pathViews())
            view.setVisibility(View.INVISIBLE);

        ArrayList<AppCompatButton> pointButtons = new ArrayList<>();
        pointButtons.add(binding.btnWay);
        pointButtons.add(binding.btnBoundary);
        pointButtons.add(binding.btnObstacle);
        for (View view : pointButtons) {
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
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
            case R.id.btnCreate:
                mDeletePath = false;
                show(pathViews(), true);
                show(mPathNamesView, false);
                break;

            case R.id.btnDelete:
                mDeletePath = true;
                show(pathViews(), false);
                show(mPathNamesView, true);
                break;

            case R.id.btnPathOk:
                //Verify entered path isn't a duplicate.
                String[] pathNames = FileUtils.root().list();
                String pathText = mEdtEnterPath.getText().toString() + ".path";
                if (pathNames != null) {
                    for (String path : pathNames) {
                        if (path.equals(pathText)) {
                            mEdtEnterPath.setError(path + " already exists");
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
                        show(mPathNamesView, false);
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
                    show(mPathNamesView, true);
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
        return false;
    }

    private ArrayList<View> pathViews() {
        ArrayList<View> views = new ArrayList<>();
        views.add(mTxtEnterPath);
        views.add(mEdtEnterPath);
        views.add(mBtnPathOk);
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
        mPathNamesView.setAdapter(new PathNameView(pathNames != null ? Arrays.asList(pathNames) : new ArrayList<>()));
    }

    private void setCurrentPath(String path) {
        mCurrentPath = path;
        mTxtCurrentPath.setText("Current Path: " + mCurrentPath);
    }

    public class PathNameView extends RecyclerView.Adapter<PathNameView.ViewHolder> {
        private final List<String> mPathNames;

        PathNameView(List<String> pathNames) {
            mPathNames = pathNames;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.path_name_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTextView.setText(mPathNames.get(position));
        }

        @Override
        public int getItemCount() {
            return mPathNames.size();
        }

            public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                TextView mTextView;

                ViewHolder(View itemView) {
                    super(itemView);
                    mTextView = itemView.findViewById(R.id.txtPathName);
                    itemView.setOnClickListener(this);
                }

                @Override
                public void onClick(View view) {
                    Bus.post(new ItemClickEvent(mTextView.getText().toString()));
                }
            }
    }

    //Installed EventBus for a single use because I have a personal vendetta against RecyclerView's implementation.
    //(And also because this is a portfolio piece so I wanted to showcase abstractions and generics for no actual reason.)
    public static class ItemClickEvent extends GenericEvent<String> {
        public ItemClickEvent(String data) {
            super(data);
        }
    }

    @Subscribe
    public void onItemClick(ItemClickEvent event) {
        Toast.makeText(this, (mDeletePath ? "Deleted " : "Loaded ") + event.data(), Toast.LENGTH_SHORT).show();
        if (mDeletePath) {
            if (event.data().equals(mCurrentPath))
                setCurrentPath("");
            deletePath(event.data());
        } else {
            setCurrentPath(event.data());
            if (!mEditor.load(mCurrentPath, mMap))
                onPathError(PathOperation.LOAD, mCurrentPath);
        }
    }

    private void onPathError(PathOperation operation, String path) {
        mEditor.clear();
        deletePath(path);
        if (mCurrentPath.equals(path))
            setCurrentPath("");

        new AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage("Failed to " + operation.toString().toLowerCase() + " " + path + ". " + path + " will now be deleted.")
            .setPositiveButton("Ok", null)
            .show();
    }

    private void deletePath(String path) {
        FileUtils.create(path).delete();
        updatePathNames(FileUtils.root().list());
    }

    private enum PathOperation {
        SAVE,
        LOAD
    }
}