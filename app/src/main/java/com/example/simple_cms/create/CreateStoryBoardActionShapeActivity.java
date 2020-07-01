package com.example.simple_cms.create;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simple_cms.R;
import com.example.simple_cms.create.utility.adapter.PointRecyclerAdapter;
import com.example.simple_cms.create.utility.model.ActionIdentifier;
import com.example.simple_cms.create.utility.model.poi.POI;
import com.example.simple_cms.create.utility.model.shape.Point;
import com.example.simple_cms.create.utility.model.shape.Shape;
import com.example.simple_cms.dialog.CustomDialogUtility;
import com.example.simple_cms.utility.ConstantPrefs;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is in charge of getting the information of shape action
 */
public class CreateStoryBoardActionShapeActivity extends AppCompatActivity {

    private static final String TAG_DEBUG = "CreateStoryBoardActionShapeActivity";

    private TextView connectionStatus, imageAvailable,
            locationName, locationNameTitle;

    private RecyclerView mRecyclerView;
    ArrayList<Point> points = new ArrayList<>();
    private POI poi;
    private boolean isSave = false;
    private int position = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_storyboard_action_shape);

        connectionStatus = findViewById(R.id.connection_status);
        imageAvailable = findViewById(R.id.image_available);
        locationName = findViewById(R.id.location_name);
        locationNameTitle = findViewById(R.id.location_name_title);

        mRecyclerView = findViewById(R.id.my_recycler_view);

        Button buttTest = findViewById(R.id.butt_test);
        Button buttCancel = findViewById(R.id.butt_cancel);
        Button buttAdd = findViewById(R.id.butt_add);
        Button buttDelete = findViewById(R.id.butt_delete);
        Button buttAddPoint = findViewById(R.id.butt_add_point);
        Button buttDeletePoint = findViewById(R.id.butt_delete_point);
        Button buttDeletePoints = findViewById(R.id.butt_delete_points);

        Intent intent = getIntent();
        poi = intent.getParcelableExtra(ActionIdentifier.LOCATION_ACTIVITY.name());
        if (poi != null) {
            setTextView();
        }

        Shape shape = intent.getParcelableExtra(ActionIdentifier.SHAPES_ACTIVITY.name());
        if (shape != null) {
            position = intent.getIntExtra(ActionIdentifier.POSITION.name(), -1);
            isSave = true;
            buttAdd.setText(getResources().getString(R.string.button_save));
            buttDelete.setVisibility(View.VISIBLE);
            poi = shape.getPoi();
            setTextView();
        }

        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        loadConnectionStatus(sharedPreferences);

        points.add(new Point(0.0, 0.0, 0.0));
        points.add(new Point(0.0, 0.0, 0.0));
        initRecyclerView();

        buttCancel.setOnClickListener((view) ->
                finish()
        );

        buttTest.setOnClickListener((view) ->
                testConnection()
        );

        buttAdd.setOnClickListener((view) ->
                addShape()
        );

        buttDelete.setOnClickListener((view) ->
                deleteShape()
        );

        buttAddPoint.setOnClickListener((view) -> {
            points.add(new Point(0.0, 0.0, 0.0));
            rePaintRecyclerView();
        });

        buttDeletePoint.setOnClickListener((view) -> {
            if(points.size() > 2 ) {
                points.remove(points.size() - 1);
                rePaintRecyclerView();
            } else{
                CustomDialogUtility.showDialog(CreateStoryBoardActionShapeActivity.this,
                        getResources().getString(R.string.delete_point));
            }
        });

        buttDeletePoints.setOnClickListener( (view) -> {
            if(points.size() > 2){
                List<Point> initPoints = points.subList(0, 2);
                points = new ArrayList<>(initPoints);
                rePaintRecyclerView();
            } else{
                CustomDialogUtility.showDialog(CreateStoryBoardActionShapeActivity.this,
                        getResources().getString(R.string.delete_point));
            }
        });
    }

    private void testConnection() {

    }

    private void addShape() {
        Shape shape = new Shape().setPoi(poi);
        Intent returnInfoIntent = new Intent();
        returnInfoIntent.putExtra(ActionIdentifier.SHAPES_ACTIVITY.name(), shape);
        returnInfoIntent.putExtra(ActionIdentifier.IS_SAVE.name(), isSave);
        returnInfoIntent.putExtra(ActionIdentifier.POSITION.name(), position);
        setResult(Activity.RESULT_OK, returnInfoIntent);
        finish();
    }

    private void deleteShape() {
        Intent returnInfoIntent = new Intent();
        returnInfoIntent.putExtra(ActionIdentifier.POSITION.name(), position);
        returnInfoIntent.putExtra(ActionIdentifier.IS_DELETE.name(), true);
        setResult(Activity.RESULT_OK, returnInfoIntent);
        finish();
    }

    /**
     * Set the information of the oldHeading and oldTilt
     */
    private void setTextView() {
        locationName.setVisibility(View.VISIBLE);
        locationNameTitle.setVisibility(View.VISIBLE);
        locationNameTitle.setText(poi.getPoiLocation().getName());
    }



    /**
     * Set the conenction status on the view
     */
    private void loadConnectionStatus(SharedPreferences sharedPreferences) {
        boolean isConnected = sharedPreferences.getBoolean(ConstantPrefs.IS_CONNECTED.name(), false);
        if (isConnected) {
            connectionStatus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_status_connection_green));
            imageAvailable.setText(getResources().getString(R.string.image_available_on_screen));
        }
    }

    private void rePaintRecyclerView(){
        RecyclerView.Adapter mAdapter = new PointRecyclerAdapter(points);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Initiate the recycleview
     */
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.Adapter mAdapter = new PointRecyclerAdapter(points);
        mRecyclerView.setAdapter(mAdapter);
    }

}
