package com.example.simple_cms.create;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simple_cms.R;
import com.example.simple_cms.create.utility.adapter.PointRecyclerAdapter;
import com.example.simple_cms.create.utility.connection.LGConnectionTest;
import com.example.simple_cms.create.utility.model.ActionController;
import com.example.simple_cms.create.utility.model.ActionIdentifier;
import com.example.simple_cms.create.utility.model.poi.POI;
import com.example.simple_cms.create.utility.model.shape.Point;
import com.example.simple_cms.create.utility.model.shape.Shape;
import com.example.simple_cms.dialog.CustomDialogUtility;
import com.example.simple_cms.utility.ConstantPrefs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is in charge of getting the information of shape action
 */
public class CreateStoryBoardActionShapeActivity extends AppCompatActivity {

    private static final String TAG_DEBUG = "CreateStoryBoardActionShapeActivity";

    private TextView connectionStatus, imageAvailable,
            locationName, locationNameTitle;

    private RecyclerView mRecyclerView;
    List<Point> points = new ArrayList<>();

    private Handler handler = new Handler();
    private POI poi;
    private boolean isSave = false;
    private int position = -1;
    private SwitchCompat switchCompatExtrude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_storyboard_action_shape);

        connectionStatus = findViewById(R.id.connection_status);
        imageAvailable = findViewById(R.id.image_available);
        locationName = findViewById(R.id.location_name);
        locationNameTitle = findViewById(R.id.location_name_title);

        mRecyclerView = findViewById(R.id.my_recycler_view);
        switchCompatExtrude = findViewById(R.id.switch_button);

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
            points = shape.getPoints();
            boolean isExtrude = shape.isExtrude();
            switchCompatExtrude.setChecked(isExtrude);
        }else {
            Point point = new Point();
            double temp = 0.0;
            point.setLongitude(temp);
            point.setLatitude(temp);
            point.setAltitude(temp);
            points.add(point);
            points.add(point);
        }

        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        loadConnectionStatus(sharedPreferences);

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
            Point point = new Point();
            double temp = 0.0;
            point.setLongitude(temp);
            point.setLatitude(temp);
            point.setAltitude(temp);
            points.add(point);
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

    /**
     * Test the action and the connection to the liquid galaxy
     */
    private void testConnection() {
        AtomicBoolean isConnected = new AtomicBoolean(false);
        LGConnectionTest.testPriorConnection(this, isConnected);
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        handler.postDelayed(() -> {
            if(isConnected.get()){
                Shape shape = new Shape().setPoi(poi).setPoints(points).setExtrude(switchCompatExtrude.isChecked());
                ActionController.getInstance().sendShape(shape, null);
            }else{
                connectionStatus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_status_connection_red));
            }
            loadConnectionStatus(sharedPreferences);
        }, 1200);
    }

    /**
     * Send the action of adding the shape
     */
    private void addShape() {
        Shape shape = new Shape().setPoi(poi).setPoints(points).setExtrude(switchCompatExtrude.isChecked());
        Intent returnInfoIntent = new Intent();
        returnInfoIntent.putExtra(ActionIdentifier.SHAPES_ACTIVITY.name(), shape);
        returnInfoIntent.putExtra(ActionIdentifier.IS_SAVE.name(), isSave);
        returnInfoIntent.putExtra(ActionIdentifier.POSITION.name(), position);
        setResult(Activity.RESULT_OK, returnInfoIntent);
        finish();
    }

    /**
     * Send the action of deleting the shape
     */
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

    /**
     * It re paints the recyclerview with the points
     */
    private void rePaintRecyclerView(){
        RecyclerView.Adapter mAdapter = new PointRecyclerAdapter(points);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.scrollToPosition(points.size() - 1);
    }

    /**
     * Initiate the recycleview
     */
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false) {
            @Override
            public boolean requestChildRectangleOnScreen(@NonNull RecyclerView parent, @NonNull View child, @NonNull Rect rect, boolean immediate, boolean focusedChildVisible) {

                if (((ViewGroup) child).getFocusedChild() instanceof EditText) {
                    return false;
                }

                return super.requestChildRectangleOnScreen(parent, child, rect, immediate, focusedChildVisible);
            }
        };
        mRecyclerView.setLayoutManager(linearLayoutManager);
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
