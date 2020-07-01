package com.example.simple_cms.create;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simple_cms.R;
import com.example.simple_cms.create.utility.adapter.ActionRecyclerAdapter;
import com.example.simple_cms.create.utility.model.Action;
import com.example.simple_cms.create.utility.model.ActionIdentifier;
import com.example.simple_cms.create.utility.model.movement.Movement;
import com.example.simple_cms.create.utility.model.balloon.Balloon;
import com.example.simple_cms.create.utility.model.poi.POI;
import com.example.simple_cms.create.utility.model.shape.Shape;
import com.example.simple_cms.dialog.CustomDialogUtility;
import com.example.simple_cms.top_bar.TobBarActivity;
import com.example.simple_cms.utility.ConstantPrefs;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This activity is in charge of creating the storyboards with the respective different actions
 */
public class CreateStoryBoardActivity extends TobBarActivity implements
        ActionRecyclerAdapter.OnNoteListener {

    private static final String TAG_DEBUG = "CreateStoryBoardActivity";

    private RecyclerView mRecyclerView;
    ArrayList<Action> actions = new ArrayList<>();
    private POI currentPoi;
    private int currentPoiPosition;

    private Button buttCreate, buttLocation, buttMovements, buttBalloon, buttShapes, buttTest, buttDelete, buttSave;
    private TextView connectionStatus, imageAvailable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_storyboard);

        mRecyclerView = findViewById(R.id.my_recycler_view);

        View topBar = findViewById(R.id.top_bar);
        buttCreate = topBar.findViewById(R.id.butt_create_menu);

        buttLocation = findViewById(R.id.butt_location);
        buttMovements = findViewById(R.id.butt_movements);
        buttBalloon = findViewById(R.id.butt_balloon);
        buttShapes = findViewById(R.id.butt_shapes);
        buttDelete = findViewById(R.id.butt_delete);
        buttSave = findViewById(R.id.butt_save);
        connectionStatus = findViewById(R.id.connection_status);
        imageAvailable = findViewById(R.id.image_available);

        buttLocation.setOnClickListener((view) -> {
            Intent intent = new Intent(getApplicationContext(), CreateStoryBoardActionLocationActivity.class);
            intent.putExtra(ActionIdentifier.POSITION.name(), actions.size());
            startActivityForResult(intent, ActionIdentifier.LOCATION_ACTIVITY.getId());
        });

        buttMovements.setOnClickListener((view) -> {
            Intent intent = new Intent(getApplicationContext(), CreateStoryBoardActionMovementActivity.class);
            if (currentPoi == null) {
                CustomDialogUtility.showDialog(CreateStoryBoardActivity.this,
                        getResources().getString(R.string.You_need_a_location_to_create_a_movement));
            } else {
                intent.putExtra(ActionIdentifier.LOCATION_ACTIVITY.name(), currentPoi);
                startActivityForResult(intent, ActionIdentifier.MOVEMENT_ACTIVITY.getId());
            }
        });

        buttBalloon.setOnClickListener((view) -> {
            Intent intent = new Intent(getApplicationContext(), CreateStoryBoardActionBalloonActivity.class);
            if (currentPoi == null) {
                CustomDialogUtility.showDialog(CreateStoryBoardActivity.this,
                        getResources().getString(R.string.You_need_a_location_to_create_a_balloon));
            } else {
                intent.putExtra(ActionIdentifier.LOCATION_ACTIVITY.name(), currentPoi);
                startActivityForResult(intent, ActionIdentifier.BALLOON_ACTIVITY.getId());
            }
        });

        buttShapes.setOnClickListener((view) -> {
            Intent intent = new Intent(getApplicationContext(), CreateStoryBoardActionShapeActivity.class);
            if (currentPoi == null) {
                CustomDialogUtility.showDialog(CreateStoryBoardActivity.this,
                        getResources().getString(R.string.You_need_a_location_to_create_a_shape));
            } else {
                intent.putExtra(ActionIdentifier.LOCATION_ACTIVITY.name(), currentPoi);
                startActivityForResult(intent, ActionIdentifier.SHAPES_ACTIVITY.getId());
            }
        });

        buttDelete.setOnClickListener((view) -> deleteStoryboard());

        changeButtonClickableBackgroundColor();
    }

    private void deleteStoryboard() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        @SuppressLint("InflateParams") View v = this.getLayoutInflater().inflate(R.layout.dialog_fragment, null);
        v.getBackground().setAlpha(220);
        Button ok = v.findViewById(R.id.ok);
        TextView textMessage = v.findViewById(R.id.message);
        textMessage.setText(getResources().getString(R.string.alert_message_delete_storyboard));
        textMessage.setTextSize(23);
        textMessage.setGravity(View.TEXT_ALIGNMENT_CENTER);
        Button cancel = v.findViewById(R.id.cancel);
        cancel.setVisibility(View.VISIBLE);
        builder.setView(v);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ok.setOnClickListener(v1 -> {
            actions = new ArrayList<>();
            currentPoi = null;
            currentPoiPosition = 0;
            initRecyclerView();
            dialog.dismiss();
        });
        cancel.setOnClickListener(v1 ->
                dialog.dismiss());
    }


    @Override
    protected void onResume() {
        loadData();
        initRecyclerView();
        super.onResume();
    }

    /**
     * Load the data
     */
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        boolean isConnected = sharedPreferences.getBoolean(ConstantPrefs.IS_CONNECTED.name(), false);
        if (isConnected) {
            connectionStatus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_status_connection_green));
            imageAvailable.setText(getResources().getString(R.string.image_available_on_screen));
        }
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
        RecyclerView.Adapter mAdapter = new ActionRecyclerAdapter(this, actions, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActionIdentifier.LOCATION_ACTIVITY.getId() && resultCode == Activity.RESULT_OK) {
            resolvePOIAction(data);
        } else if (requestCode == ActionIdentifier.MOVEMENT_ACTIVITY.getId() && resultCode == Activity.RESULT_OK) {
            resolveMovementAction(data);
        } else if (requestCode == ActionIdentifier.BALLOON_ACTIVITY.getId() && resultCode == Activity.RESULT_OK) {
            resolveBalloonAction(data);
        } else if (requestCode == ActionIdentifier.SHAPES_ACTIVITY.getId() && resultCode == Activity.RESULT_OK){
            boolean isDelete = Objects.requireNonNull(data).getBooleanExtra(ActionIdentifier.IS_DELETE.name(), false);
            int position = Objects.requireNonNull(data).getIntExtra(ActionIdentifier.POSITION.name(), -1);
            if (isDelete) {
                if (position != -1) {
                    actions.remove(position);
                }
            } else {
                Shape shape = Objects.requireNonNull(data).getParcelableExtra(ActionIdentifier.SHAPES_ACTIVITY.name());
                boolean isSave = Objects.requireNonNull(data).getBooleanExtra(ActionIdentifier.IS_SAVE.name(), false);
                if (isSave) {
                    if (position != -1) {
                        actions.set(position, shape);
                    }
                } else {
                    actions.add(shape);
                }
            }
        } else{
            Log.w(TAG_DEBUG, "ERROR Result");
        }
    }


    /**
     * Resolve if the poi action is going to be deleted or saved
     *
     * @param data Intent with the info
     */
    private void resolvePOIAction(@Nullable Intent data) {
        boolean isDelete = Objects.requireNonNull(data).getBooleanExtra(ActionIdentifier.IS_DELETE.name(), false);
        int position = Objects.requireNonNull(data).getIntExtra(ActionIdentifier.POSITION.name(), -1);
        if (isDelete) {
            if (position != -1) {
                deletePOI(position);
            }
        } else {
            savePOI(data, position);
        }
    }

    /**
     * Resolve if the movement action is going to be deleted or saved
     *
     * @param data Intent with the info
     */
    private void resolveMovementAction(@Nullable Intent data) {
        boolean isDelete = Objects.requireNonNull(data).getBooleanExtra(ActionIdentifier.IS_DELETE.name(), false);
        int position = Objects.requireNonNull(data).getIntExtra(ActionIdentifier.POSITION.name(), -1);
        if (isDelete) {
            if (position != -1) {
                actions.remove(position);
            }
        } else {
            Movement movement = Objects.requireNonNull(data).getParcelableExtra(ActionIdentifier.MOVEMENT_ACTIVITY.name());
            boolean isSave = Objects.requireNonNull(data).getBooleanExtra(ActionIdentifier.IS_SAVE.name(), false);
            if (isSave) {
                if (position != -1) {
                    actions.set(position, movement);
                }
            } else {
                actions.add(movement);
            }
        }
    }

    /**
     * Resolve if the balloon action is going to be deleted or saved
     *
     * @param data Intent with the info
     */
    private void resolveBalloonAction(@Nullable Intent data) {
        boolean isDelete = Objects.requireNonNull(data).getBooleanExtra(ActionIdentifier.IS_DELETE.name(), false);
        int position = Objects.requireNonNull(data).getIntExtra(ActionIdentifier.POSITION.name(), -1);
        if (isDelete) {
            if (position != -1) {
                actions.remove(position);
            }
        } else {
            Balloon balloon = Objects.requireNonNull(data).getParcelableExtra(ActionIdentifier.BALLOON_ACTIVITY.name());
            boolean isSave = Objects.requireNonNull(data).getBooleanExtra(ActionIdentifier.IS_SAVE.name(), false);
            if (isSave) {
                if (position != -1) {
                    actions.set(position, balloon);
                }
            } else {
                actions.add(balloon);
            }
        }
    }

    /**
     * Delete the poi
     *
     * @param position the position of the poi
     */
    private void deletePOI(int position) {
        Action action;
        ArrayList<Action> newActions = new ArrayList<>();
        POI newCurrent = null;
        int newPosition = 0;
        int startNewPoi = -1;
        for (int i = 0; i < position; i++) {
            action = actions.get(i);
            newActions.add(action);
            if (action.getType() == ActionIdentifier.LOCATION_ACTIVITY.getId()) {
                newCurrent = (POI) action;
                newPosition = i;
            }
        }
        for (int i = position + 1; i < actions.size(); i++) {
            if (actions.get(i).getType() == ActionIdentifier.LOCATION_ACTIVITY.getId()) {
                startNewPoi = i;
                newCurrent = (POI) actions.get(i);
                newPosition = i;
                newActions.add(actions.get(i));
                break;
            }
        }
        if (startNewPoi == -1) startNewPoi = actions.size();
        for (int i = startNewPoi + 1; i < actions.size(); i++) {
            action = actions.get(i);
            newActions.add(action);
            if (action.getType() == ActionIdentifier.LOCATION_ACTIVITY.getId()) {
                newCurrent = (POI) action;
                newPosition = i;
            }
        }
        currentPoi = newCurrent;
        currentPoiPosition = newPosition;
        actions.clear();
        actions.addAll(newActions);
    }

    /**
     * Save or add the poi
     *
     * @param data     the intent with the data
     * @param position the position of the poi
     */
    private void savePOI(@NonNull Intent data, int position) {
        POI poi = Objects.requireNonNull(data).getParcelableExtra(ActionIdentifier.LOCATION_ACTIVITY.name());
        boolean isSave = Objects.requireNonNull(data).getBooleanExtra(ActionIdentifier.IS_SAVE.name(), false);
        if (isSave) {
            if (position != -1) {
                actions.set(position, poi);
                if (currentPoiPosition == position) currentPoi = poi;
                Action action;
                for (int i = position + 1; i < actions.size(); i++) {
                    action = actions.get(i);
                    if (action.getType() == ActionIdentifier.LOCATION_ACTIVITY.getId()) break;
                    if (action.getType() == ActionIdentifier.MOVEMENT_ACTIVITY.getId()) {
                        Movement movement = (Movement) action;
                        movement.setPoi(poi);
                        actions.set(i, movement);
                    }
                }
            }
        } else {
            actions.add(poi);
            currentPoi = poi;
            currentPoiPosition = position;
        }
    }

    /**
     * Change the background color and the option clickable to false of the button_connect
     */
    private void changeButtonClickableBackgroundColor() {
        changeButtonClickableBackgroundColor(getApplicationContext(), buttCreate);
    }

    @Override
    public void onNoteClick(int position) {
        Action selected = actions.get(position);
        if (selected instanceof POI) {
            Intent intent = new Intent(getApplicationContext(), CreateStoryBoardActionLocationActivity.class);
            intent.putExtra(ActionIdentifier.LOCATION_ACTIVITY.name(), (POI) selected);
            intent.putExtra(ActionIdentifier.POSITION.name(), position);
            startActivityForResult(intent, ActionIdentifier.LOCATION_ACTIVITY.getId());
        } else if (selected instanceof Movement) {
            Intent intent = new Intent(getApplicationContext(), CreateStoryBoardActionMovementActivity.class);
            intent.putExtra(ActionIdentifier.MOVEMENT_ACTIVITY.name(), (Movement) selected);
            intent.putExtra(ActionIdentifier.POSITION.name(), position);
            startActivityForResult(intent, ActionIdentifier.MOVEMENT_ACTIVITY.getId());
        } else if (selected instanceof Balloon) {
            Intent intent = new Intent(getApplicationContext(), CreateStoryBoardActionBalloonActivity.class);
            intent.putExtra(ActionIdentifier.BALLOON_ACTIVITY.name(), (Balloon) selected);
            intent.putExtra(ActionIdentifier.POSITION.name(), position);
            startActivityForResult(intent, ActionIdentifier.BALLOON_ACTIVITY.getId());
        } else if (selected instanceof Shape){
            Intent intent = new Intent(getApplicationContext(), CreateStoryBoardActionShapeActivity.class);
            intent.putExtra(ActionIdentifier.SHAPES_ACTIVITY.name(), (Shape) selected);
            intent.putExtra(ActionIdentifier.POSITION.name(), position);
            startActivityForResult(intent, ActionIdentifier.SHAPES_ACTIVITY.getId());
        }else {
            Log.w(TAG_DEBUG, "ERROR EDIT");
        }
    }
}
