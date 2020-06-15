package com.example.simple_cms.create;

import android.app.Activity;
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
import com.example.simple_cms.create.utility.model.poi.POI;
import com.example.simple_cms.dialog.CustomDialogUtility;
import com.example.simple_cms.top_bar.TobBarActivity;
import com.example.simple_cms.utility.ConstantPrefs;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This activity is in charge of creating the storyboards with the respective different actions
 */
public class CreateStoryBoardActivity extends TobBarActivity implements
        ActionRecyclerAdapter.OnNoteListener{

    private static final String TAG_DEBUG = "CreateStoryBoardActivity";

    private RecyclerView mRecyclerView;
    ArrayList<Action> actions = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private POI currentPoi;

    private Button buttCreate, buttLocation, buttMovements, buttGraphics, buttShapes, buttDescription, buttCancel, buttSave;
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
        buttGraphics = findViewById(R.id.butt_graphics);
        buttShapes = findViewById(R.id.butt_shapes);
        buttDescription = findViewById(R.id.butt_description);
        buttCancel = findViewById(R.id.butt_cancel);
        buttSave = findViewById(R.id.butt_save);
        connectionStatus = findViewById(R.id.connection_status);
        imageAvailable = findViewById(R.id.image_available);

        buttLocation.setOnClickListener( (view) -> {
            Intent intent = new Intent(getApplicationContext(), CreateStoryBoardActionLocationActivity.class);
            startActivityForResult(intent, ActionIdentifier.LOCATION_ACTIVITY.getId());
        });

        buttMovements.setOnClickListener( (view) -> {
            Intent intent = new Intent(getApplicationContext(), CreateStoryBoardActionMovementActivity.class);
            if(currentPoi == null){
                CustomDialogUtility.showDialog(CreateStoryBoardActivity.this,
                        getResources().getString(R.string.You_need_a_location_to_create_a_movement));
            } else {
                intent.putExtra(ActionIdentifier.LOCATION_ACTIVITY.name(), currentPoi);
                startActivityForResult(intent, ActionIdentifier.MOVEMENT_ACTIVITY.getId());
            }
        });

        changeButtonClickableBackgroundColor();
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
        if(isConnected){
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
        mAdapter = new ActionRecyclerAdapter(this, actions, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ActionIdentifier.LOCATION_ACTIVITY.getId() && resultCode == Activity.RESULT_OK){
            resolvePOIAction(data);
        } else if(requestCode == ActionIdentifier.MOVEMENT_ACTIVITY.getId() && resultCode == Activity.RESULT_OK){
            boolean isDelete = Objects.requireNonNull(data).getBooleanExtra(ActionIdentifier.IS_DELETE.name(), false);
            int position = Objects.requireNonNull(data).getIntExtra(ActionIdentifier.POSITION.name(), -1);
            if(isDelete){
                if(position != -1 ){
                    actions.remove(position);
                }
            }else{
                Movement movement = Objects.requireNonNull(data).getParcelableExtra(ActionIdentifier.MOVEMENT_ACTIVITY.name());
                boolean isSave = Objects.requireNonNull(data).getBooleanExtra(ActionIdentifier.IS_SAVE.name(), false);
                if(isSave){
                    if(position != -1 ){
                        actions.set(position, movement);
                    }
                }else {
                    actions.add(movement);
                }
            }
        }
    }

    /**
     * Resolve if the poi is going to be deleted or saved
     * @param data Intent with the info
     */
    private void resolvePOIAction(@Nullable Intent data) {
        boolean isDelete = Objects.requireNonNull(data).getBooleanExtra(ActionIdentifier.IS_DELETE.name(), false);
        int position = Objects.requireNonNull(data).getIntExtra(ActionIdentifier.POSITION.name(), -1);
        if(isDelete){
            if(position != -1 ){
                deletePOI(position);
            }
        }else{
            savePOI(data, position);
        }
    }

    /**
     * Delete the poi
     * @param position the position of the poi
     */
    private void deletePOI(int position) {
        Action action;
        int startNewPoi = -1;
        for(int i = position + 1; i < actions.size(); i++ ){
            action = actions.get(i);
            if(action.getType() == ActionIdentifier.LOCATION_ACTIVITY.getId() ){
                startNewPoi = i;
                break;
            }
        }
        if(startNewPoi == -1) startNewPoi = actions.size();
        ArrayList<Action> newActions = new ArrayList<>(actions.subList(startNewPoi, actions.size()));
        actions.clear();
        actions.addAll(newActions);
    }

    /**
     * Save or add the poi
     * @param data the intent with the data
     * @param position the position of the poi
     */
    private void savePOI(@NonNull Intent data, int position) {
        POI poi = Objects.requireNonNull(data).getParcelableExtra(ActionIdentifier.LOCATION_ACTIVITY.name());
        boolean isSave = Objects.requireNonNull(data).getBooleanExtra(ActionIdentifier.IS_SAVE.name(), false);
        if(isSave){
            if(position != -1 ){
                actions.set(position, poi);
                Action action;
                for(int i = position + 1; i < actions.size(); i++ ){
                    action = actions.get(i);
                    if(action.getType() == ActionIdentifier.LOCATION_ACTIVITY.getId() ) break;
                    if(action.getType() == ActionIdentifier.MOVEMENT_ACTIVITY.getId()){
                        Movement movement = (Movement) action;
                        movement.setPoi(poi);
                        actions.set(i, movement);
                    }
                }
            }
        }else{
            actions.add(poi);
            currentPoi = poi;
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
        if(selected instanceof POI){
            Intent intent = new Intent(getApplicationContext(), CreateStoryBoardActionLocationActivity.class);
            intent.putExtra(ActionIdentifier.LOCATION_ACTIVITY.name(), (POI) selected);
            intent.putExtra(ActionIdentifier.POSITION.name(), position);
            startActivityForResult(intent, ActionIdentifier.LOCATION_ACTIVITY.getId());
        }else if(selected instanceof Movement){
            Intent intent = new Intent(getApplicationContext(), CreateStoryBoardActionMovementActivity.class);
            intent.putExtra(ActionIdentifier.MOVEMENT_ACTIVITY.name(), (Movement) selected);
            intent.putExtra(ActionIdentifier.POSITION.name(), position);
            startActivityForResult(intent, ActionIdentifier.MOVEMENT_ACTIVITY.getId());
        }else {
            Log.w(TAG_DEBUG, "ERROR");
        }
    }
}
