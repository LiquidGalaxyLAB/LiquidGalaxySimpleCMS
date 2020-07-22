package com.lglab.diego.simple_cms.create;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lglab.diego.simple_cms.R;
import com.lglab.diego.simple_cms.create.action.CreateStoryBoardActionBalloonActivity;
import com.lglab.diego.simple_cms.create.action.CreateStoryBoardActionLocationActivity;
import com.lglab.diego.simple_cms.create.action.CreateStoryBoardActionMovementActivity;
import com.lglab.diego.simple_cms.create.action.CreateStoryBoardActionShapeActivity;
import com.lglab.diego.simple_cms.create.utility.adapter.ActionRecyclerAdapter;
import com.lglab.diego.simple_cms.create.utility.model.Action;
import com.lglab.diego.simple_cms.create.utility.model.ActionIdentifier;
import com.lglab.diego.simple_cms.create.utility.model.movement.Movement;
import com.lglab.diego.simple_cms.create.utility.model.balloon.Balloon;
import com.lglab.diego.simple_cms.create.utility.model.poi.POI;
import com.lglab.diego.simple_cms.create.utility.model.shape.Shape;
import com.lglab.diego.simple_cms.db.AppDatabase;
import com.lglab.diego.simple_cms.db.entity.StoryBoard;
import com.lglab.diego.simple_cms.dialog.CustomDialogUtility;
import com.lglab.diego.simple_cms.my_storyboards.StoryBoardConstant;
import com.lglab.diego.simple_cms.utility.ConstantPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This activity is in charge of creating the storyboards with the respective different actions
 */
public class CreateStoryBoardActivity extends ExportGoogleDriveActivity implements
        ActionRecyclerAdapter.OnNoteListener {

    private static final String TAG_DEBUG = "CreateStoryBoardActivity";
    private static final int PERMISSION_CODE = 1000;

    private RecyclerView mRecyclerView;
    List<Action> actions = new ArrayList<>();
    private POI currentPoi;
    private int currentPoiPosition;
    private long currentStoryBoardId = Long.MIN_VALUE;
    private String currentStoryBoardGoogleDriveID = null;

    private EditText storyBoardName;
    private Button buttCreate;
    private TextView connectionStatus, imageAvailable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_storyboard);

        mRecyclerView = findViewById(R.id.my_recycler_view);

        View topBar = findViewById(R.id.top_bar);
        buttCreate = topBar.findViewById(R.id.butt_create_menu);
        storyBoardName = findViewById(R.id.text_admin_password);


        Button buttTest = findViewById(R.id.butt_test);
        Button buttLocation = findViewById(R.id.butt_location);
        Button buttMovements = findViewById(R.id.butt_movements);
        Button buttBalloon = findViewById(R.id.butt_balloon);
        Button buttShapes = findViewById(R.id.butt_shapes);
        Button buttDelete = findViewById(R.id.butt_delete);
        Button buttSaveLocally = findViewById(R.id.butt_save_locally);
        Button buttSaveGoogleDrive = findViewById(R.id.butt_save_on_google_drive);

        connectionStatus = findViewById(R.id.connection_status);
        imageAvailable = findViewById(R.id.admin_password);

        currentStoryBoardId = getIntent().getLongExtra(StoryBoardConstant.STORY_BOARD_ID.name(), Long.MIN_VALUE);
        String name = getIntent().getStringExtra(StoryBoardConstant.STORY_BOARD_NAME.name());
        if (currentStoryBoardId != Long.MIN_VALUE) chargeStoryBoard(name);

        currentStoryBoardGoogleDriveID = getIntent().getStringExtra(StoryBoardConstant.STORY_BOARD_JSON_ID.name());
        String storyBoardJson = getIntent().getStringExtra(StoryBoardConstant.STORY_BOARD_JSON.name());
        if (storyBoardJson != null) chargeStoryBoardJson(storyBoardJson);

        if (currentStoryBoardId == Long.MIN_VALUE && storyBoardJson == null) loadDataJson();

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

        buttSaveLocally.setOnClickListener((view) -> saveStoryboardLocally());

        buttSaveGoogleDrive.setOnClickListener((view) -> saveStoryboardGoogleDrive());

        buttTest.setOnClickListener((view) -> testStoryBoard());

        initRecyclerView();

        changeButtonClickableBackgroundColor();
    }

    private void loadDataJson() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        String storyBoardJson = sharedPreferences.getString(ConstantPrefs.STORY_BOARD_JSON.name(), "");
        if (!storyBoardJson.equals("")) {
            try {
                com.lglab.diego.simple_cms.create.utility.model.StoryBoard storyBoard = new com.lglab.diego.simple_cms.create.utility.model.StoryBoard();
                JSONObject jsonStoryBoard = new JSONObject(storyBoardJson);
                storyBoard.unpack(jsonStoryBoard);
                List<Action> actionsSaved = storyBoard.getActions();
                storyBoardName.setText(storyBoard.getName());
                if (actionsSaved.size() > 0) {
                    actions = actionsSaved;
                    currentPoi = (POI) actions.get(0);
                }
                currentPoiPosition = 0;
                currentStoryBoardGoogleDriveID = storyBoard.getStoryBoardFileId();
                currentStoryBoardId = storyBoard.getStoryBoardId();
            } catch (JSONException jsonException) {
                Log.w(TAG_DEBUG, "ERROR CONVERTING JSON: " + jsonException);
            }
        }
    }

    /**
     * Test the actions of the storyboard
     */
    private void testStoryBoard() {
        CustomDialogUtility.showDialog(CreateStoryBoardActivity.this, "Testing the storyboard");
        List<Action> actionsSend = new ArrayList<>(actions);
        TestStoryboardThread.getInstance().startConnection(actionsSend);
    }

    /**
     * It charge the storyboard that was selected from google drive
     *
     * @param storyBoardJson the string of the storyboard
     */
    private void chargeStoryBoardJson(String storyBoardJson) {
        Log.w(TAG_DEBUG, "STORY BOARD JSON: " + storyBoardJson);
        try {
            com.lglab.diego.simple_cms.create.utility.model.StoryBoard storyBoard = new com.lglab.diego.simple_cms.create.utility.model.StoryBoard();
            JSONObject jsonStoryBoard = new JSONObject(storyBoardJson);
            storyBoard.unpackStoryBoard(jsonStoryBoard, getApplicationContext());
            actions = storyBoard.getActions();
            storyBoardName.setText(storyBoard.getName());
            currentPoi = (POI) actions.get(0);
            currentPoiPosition = 0;
        } catch (JSONException jsonException) {
            Log.w(TAG_DEBUG, "ERROR CONVERTING JSON: " + jsonException);
        }
    }


    /**
     * Charge the storyboard that was selected locally
     *
     * @param name the name of the storyboard to charge
     */
    private void chargeStoryBoard(String name) {
        try {
            AppDatabase db = AppDatabase.getAppDatabase(this);
            actions = Action.getAction(db.storyBoardDao().getActionsOFStoryBoard(currentStoryBoardId));
            storyBoardName.setText(name);
            currentPoi = (POI) actions.get(0);
            currentPoiPosition = 0;
        } catch (Exception e) {
            Log.w(TAG_DEBUG, "ERROR DB: " + e.getMessage());
        }
    }

    /**
     * Save or update the storyboard Google Drive
     */
    private void saveStoryboardGoogleDrive() {
        if (isLogIn()) {
            String name = storyBoardName.getText().toString();
            if (name.equals("")) {
                CustomDialogUtility.showDialog(CreateStoryBoardActivity.this,
                        getResources().getString(R.string.You_need_a_name_to_create_a_story_board));
            } else {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
                } else {
                    Log.w(TAG_DEBUG, "GOOD");
                    packSaveStoryboard();
                }

            }
        } else {
            CustomDialogUtility.showDialog(CreateStoryBoardActivity.this,
                    getResources().getString(R.string.message_you_need_log_in));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                packSaveStoryboard();
            } else {
                CustomDialogUtility.showDialog(this, getResources().getString(R.string.alert_permission_denied_save));
            }
        }
    }

    private void packSaveStoryboard() {
        try{
            String name = storyBoardName.getText().toString();
            com.lglab.diego.simple_cms.create.utility.model.StoryBoard storyBoard = new com.lglab.diego.simple_cms.create.utility.model.StoryBoard();
            storyBoard.setStoryBoardFileId(currentStoryBoardGoogleDriveID);
            storyBoard.setName(name);
            storyBoard.setActions(actions);
            JSONObject jsonStoryboard = storyBoard.pack();
            requestSignIn(jsonStoryboard.toString(), storyBoard.getNameForExporting(), currentStoryBoardGoogleDriveID, this);
        }catch (JSONException e) {
            Log.w(TAG_DEBUG, "JSON ERROR: " + e.getMessage());
        }
    }

    /**
     * Save or update the storyboard locally
     */
    private void saveStoryboardLocally() {
        String name = storyBoardName.getText().toString();
        if (name.equals("")) {
            CustomDialogUtility.showDialog(CreateStoryBoardActivity.this,
                    getResources().getString(R.string.You_need_a_name_to_create_a_story_board));
        } else {
            try {
                AppDatabase db = AppDatabase.getAppDatabase(this);
                if (currentStoryBoardId != Long.MIN_VALUE) {
                    db.storyBoardDao().updateStoryBoardWithActions(currentStoryBoardId, getActionsDB());
                    CustomDialogUtility.showDialog(CreateStoryBoardActivity.this,
                            getResources().getString(R.string.alert_update_story_board));
                } else {
                    saveStoryBoardRoom(name, db);
                    CustomDialogUtility.showDialog(CreateStoryBoardActivity.this,
                            getResources().getString(R.string.alert_save_story_board));
                }
            } catch (Exception e) {
                Log.w(TAG_DEBUG, "ERROR DB: " + e.getMessage());
            }
        }
    }

    /**
     * This is in charge of saving a new storyboard to the db
     *
     * @param name Name of the storyBoard
     * @param db   Connection to db
     */
    private void saveStoryBoardRoom(String name, AppDatabase db) {
        StoryBoard storyBoard = new StoryBoard();
        storyBoard.nameStoryBoard = name;
        db.storyBoardDao().insertStoryBoardWithAction(storyBoard, getActionsDB());
    }

    /**
     * Convert the actions to actionsDBModel
     *
     * @return The list of actionsDBModel
     */
    private List<com.lglab.diego.simple_cms.db.entity.Action> getActionsDB() {
        List<com.lglab.diego.simple_cms.db.entity.Action> actionsDB = new ArrayList<>();
        Action action;
        for (int i = 0; i < actions.size(); i++) {
            action = actions.get(i);
            if (action instanceof POI) {
                com.lglab.diego.simple_cms.db.entity.poi.POI poi = com.lglab.diego.simple_cms.db.entity.poi.POI.getPOIDBMODEL((POI) action);
                actionsDB.add(poi);
            } else if (action instanceof Movement) {
                com.lglab.diego.simple_cms.db.entity.Movement movement = com.lglab.diego.simple_cms.db.entity.Movement.getMovementDBMODEL((Movement) action);
                actionsDB.add(movement);
            } else if (action instanceof Balloon) {
                com.lglab.diego.simple_cms.db.entity.Balloon balloon = com.lglab.diego.simple_cms.db.entity.Balloon.getBalloonDBMODEL((Balloon) action);
                actionsDB.add(balloon);
            } else if (action instanceof Shape) {
                com.lglab.diego.simple_cms.db.entity.shape.Shape shape = com.lglab.diego.simple_cms.db.entity.shape.Shape.getShapeDBMODEL((Shape) action);
                actionsDB.add(shape);
            }
        }
        return actionsDB;
    }


    /**
     * Clean the actions of the recyclerview
     */
    private void deleteStoryboard() {
        @SuppressLint("InflateParams") View v = this.getLayoutInflater().inflate(R.layout.dialog_fragment, null);
        v.getBackground().setAlpha(220);
        Button ok = v.findViewById(R.id.ok);
        TextView textMessage = v.findViewById(R.id.message);
        textMessage.setText(getResources().getString(R.string.alert_message_delete_storyboard));
        textMessage.setTextSize(23);
        textMessage.setGravity(View.TEXT_ALIGNMENT_CENTER);
        Button cancel = v.findViewById(R.id.cancel);
        cancel.setVisibility(View.VISIBLE);
        createAlertDialog(v, ok, cancel);
    }

    /**
     * Create a alert dialog for the user
     *
     * @param v      view
     * @param ok     button ok
     * @param cancel button cancel
     */
    private void createAlertDialog(View v, Button ok, Button cancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ok.setOnClickListener(v1 -> {
            actions = new ArrayList<>();
            currentPoi = null;
            currentPoiPosition = 0;
            currentStoryBoardId = Long.MIN_VALUE;
            currentStoryBoardGoogleDriveID = null;
            storyBoardName.setText("");
            initRecyclerView();
            dialog.dismiss();
        });
        cancel.setOnClickListener(v1 -> dialog.dismiss());
    }


    @Override
    protected void onResume() {
        loadData();
        rePaintRecyclerView();
        super.onResume();
    }

    @Override
    protected void onPause() {
        try {
            com.lglab.diego.simple_cms.create.utility.model.StoryBoard storyBoard = new com.lglab.diego.simple_cms.create.utility.model.StoryBoard();
            Log.w(TAG_DEBUG, "GOOGLE ID: " + currentStoryBoardGoogleDriveID);
            storyBoard.setStoryBoardFileId(currentStoryBoardGoogleDriveID);
            storyBoard.setStoryBoardId(currentStoryBoardId);
            storyBoard.setName(storyBoardName.getText().toString());
            storyBoard.setActions(actions);
            SharedPreferences.Editor editor = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE).edit();
            editor.putString(ConstantPrefs.STORY_BOARD_JSON.name(), storyBoard.pack().toString());
            editor.apply();
        } catch (JSONException jsonException) {
            Log.w(TAG_DEBUG, "ERROR JSON: " + jsonException);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
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

    /**
     * It re paints the recyclerview with the actions
     */
    private void rePaintRecyclerView() {
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
        } else if (requestCode == ActionIdentifier.SHAPES_ACTIVITY.getId() && resultCode == Activity.RESULT_OK) {
            resolveShapeAction(data);
        }
    }

    /**
     * Resolve if the shape action is going to be deleted or saved
     *
     * @param data Intent with the info
     */
    private void resolveShapeAction(@Nullable Intent data) {
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
        } else if (selected instanceof Shape) {
            Intent intent = new Intent(getApplicationContext(), CreateStoryBoardActionShapeActivity.class);
            intent.putExtra(ActionIdentifier.SHAPES_ACTIVITY.name(), (Shape) selected);
            intent.putExtra(ActionIdentifier.POSITION.name(), position);
            startActivityForResult(intent, ActionIdentifier.SHAPES_ACTIVITY.getId());
        } else {
            Log.w(TAG_DEBUG, "ERROR EDIT");
        }
    }
}
