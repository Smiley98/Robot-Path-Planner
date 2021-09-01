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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

        mTxtCurrentPath = binding.txtCurrentPath;
        mPathNamesView = binding.rcvPaths;
        mPathNamesView.setVisibility(View.INVISIBLE);
        mPathNamesView.setLayoutManager(new LinearLayoutManager(this));
        mPathNamesView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        updatePathNames(FileUtils.root().list());

        mTxtEnterPath = binding.txtEnterPath;
        mEdtEnterPath = binding.edtEnterPath;
        mBtnPathOk = binding.btnPathOk;
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

    private ArrayList<View> pathViews() {
        ArrayList<View> views = new ArrayList<>();
        views.add(mTxtEnterPath);
        views.add(mEdtEnterPath);
        views.add(mBtnPathOk);
        return views;
    }

    private void updatePathNames(@Nullable String[] pathNames) {
        mPathNamesView.setAdapter(new PathNameView(pathNames != null ? Arrays.asList(pathNames) : new ArrayList<>()));
    }

    public class PathNameView extends RecyclerView.Adapter<PathNameView.ViewHolder> {
        private final List<String> mPathNames;
        //private ItemClickListener mClickListener;

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
                    //if (mClickListener != null)
                    //    mClickListener.onItemClick(view, getAdapterPosition());
                }
            }

        //Probably going to replace this with EventBus.
        /*String getItem(int id) {
            return mPathNames.get(id);
        }

        void setClickListener(ItemClickListener itemClickListener) {
            mClickListener = itemClickListener;
        }

        public interface ItemClickListener {
            void onItemClick(View view, int position);
        }*/
    }
}