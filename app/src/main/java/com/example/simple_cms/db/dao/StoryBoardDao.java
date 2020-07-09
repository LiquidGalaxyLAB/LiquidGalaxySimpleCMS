package com.example.simple_cms.db.dao;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.simple_cms.create.utility.model.ActionIdentifier;
import com.example.simple_cms.db.entity.Action;
import com.example.simple_cms.db.entity.Balloon;
import com.example.simple_cms.db.entity.Movement;
import com.example.simple_cms.db.entity.StoryBoard;
import com.example.simple_cms.db.entity.StoryBoardWithActions;
import com.example.simple_cms.db.entity.poi.POI;
import com.example.simple_cms.db.entity.shape.Point;
import com.example.simple_cms.db.entity.shape.Shape;
import com.example.simple_cms.db.entity.shape.ShapeAndPoints;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the dao to the storyboard
 */
@Dao
public abstract class StoryBoardDao {

    private static final String TAG_DEBUG = "StoryBoardDao";

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertStoryBoard(StoryBoard storyBoard);

    @Transaction
    @Query("SELECT * FROM `StoryBoard`")
    public abstract List<StoryBoard> getStoryBoardsWithOutActions();

    @Transaction
    @Query("SELECT * FROM `StoryBoard` WHERE storyBoardId = :id")
    public abstract StoryBoard getStoryBoard(long id);

    @Transaction
    @Query("SELECT * FROM `StoryBoard` WHERE storyBoardId = :id")
    public abstract StoryBoardWithActions getStoryBoardWithActions(long id);

    @Delete
    public abstract void deleteStoryBoard(StoryBoard storyBoard);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertAction(Action action);

    @Delete
    public abstract void deleteAction(Action action);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertPOI(POI poi);

    @Query("SELECT * FROM `POI` WHERE actionId = :poiId")
    public abstract POI getPOI(long poiId);

    @Delete
    public abstract void deletePOI(POI poi);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertMovement(Movement movement);

    @Query("SELECT * FROM `Movement` WHERE actionId = :movementId")
    public abstract Movement getMovement(long movementId);

    @Delete
    public abstract void deleteMovement(Movement movement);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertBalloon(Balloon balloon);

    @Query("SELECT * FROM `Balloon` WHERE actionId = :balloonId")
    public abstract Balloon getBalloon(long balloonId);

    @Delete
    public abstract void deleteBalloon(Balloon balloon);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertShape(Shape shape);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertPoint(Point point);

    @Transaction
    @Query("SELECT * FROM `Shape` WHERE actionId = :ShapeId")
    public abstract ShapeAndPoints getShapeAndPoints(long ShapeId);

    @Delete
    public abstract void deleteShapeWithPoints(Shape shape, List<Point> points);

    /**
     * Insert the shape and its points
     * @param shape Shape to add
     * @param points The lis of points to add
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    private void insertShapeWithPoints(Shape shape, List points) {
        final long shapeID = insertShape(shape);

        Point point;
        for (int i = 0; i < points.size(); i++) {
            point = (Point) points.get(i);
            point.shapeCreatorId = shapeID;
            insertPoint(point);
        }
    }


    /**
     * This method is in charge of inserting the storyboard with its actions
     *
     * @param storyBoard StoryBoard to insert
     * @param actions    List of actions of the Storyboard
     */
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertStoryBoardWithAction(StoryBoard storyBoard, List<Action> actions) {

        final long storyBoardId = insertStoryBoard(storyBoard);

        Action action;
        long actionIDPOI = 0;
        for (int i = 0; i < actions.size(); i++) {
            action = actions.get(i);
            action.actionStoryBoardID = storyBoardId;
            long actionId = insertAction(action);
            if (action instanceof POI) {
                POI poi = (POI) action;
                actionIDPOI = actionId;
                poi.actionId = actionId;
                poi.type = ActionIdentifier.LOCATION_ACTIVITY.getId();
                poi.actionStoryBoardID = storyBoardId;
                insertPOI(poi);
            } else if (action instanceof Movement) {
                Movement movement = (Movement) action;
                movement.actionId = actionId;
                movement.simplePOI.simplePOIId = actionIDPOI;
                movement.type = ActionIdentifier.MOVEMENT_ACTIVITY.getId();
                movement.actionStoryBoardID = storyBoardId;
                insertMovement(movement);
            } else if (action instanceof Balloon) {
                Balloon balloon = (Balloon) action;
                balloon.actionId = actionId;
                balloon.simplePOI.simplePOIId = actionIDPOI;
                balloon.type = ActionIdentifier.BALLOON_ACTIVITY.getId();
                balloon.actionStoryBoardID = storyBoardId;
                insertBalloon(balloon);
            } else if (action instanceof Shape) {
                Shape shape = (Shape) action;
                shape.actionId = actionId;
                shape.simplePOI.simplePOIId = actionIDPOI;
                shape.type = ActionIdentifier.SHAPES_ACTIVITY.getId();
                shape.actionStoryBoardID = storyBoardId;
                List points = shape.points;
                insertShapeWithPoints(shape, points);
            } else {
                Log.w(TAG_DEBUG, "ERROR TYPE ACTION");
            }
        }
    }

    /**
     * Get the the actions of the storyBoard
     * @param id StoryBoard ID
     * @return list with the actions
     */
    @Transaction
    public List<Action> getActionsOFStoryBoard(long id) {
        List<Action> newActions = new ArrayList<>();

        StoryBoardWithActions storyBoardWithActions = getStoryBoardWithActions(id);
        List<Action> actions = storyBoardWithActions.actions;

        for (int i = 0; i < actions.size(); i++) {
            Action action = actions.get(i);
            if (action.type == ActionIdentifier.LOCATION_ACTIVITY.getId()) {
                POI poi = getPOI(action.actionId);
                newActions.add(poi);
            } else if (action.type == ActionIdentifier.MOVEMENT_ACTIVITY.getId()) {
                Movement movement = getMovement(action.actionId);
                newActions.add(movement);
            } else if (action.type == ActionIdentifier.BALLOON_ACTIVITY.getId()) {
                Balloon balloon = getBalloon(action.actionId);
                newActions.add(balloon);
            } else if (action.type == ActionIdentifier.SHAPES_ACTIVITY.getId()) {
                ShapeAndPoints shapeAndPoints = getShapeAndPoints(action.actionId);
                Shape shape = shapeAndPoints.shape;
                shape.points = shapeAndPoints.points;
                newActions.add(shape);
            } else {
                Log.w(TAG_DEBUG, "ERROR TYPE");
            }
        }

        return newActions;
    }

    /**
     * Update the storyboard
     * @param currentStoryBoardId Id of the current storyboard
     * @param updateActions update the actions
     */
    @Transaction
    @Update
    public void updateStoryBoardWithActions(long currentStoryBoardId, List<Action> updateActions){

        //Clean old entity
        StoryBoard oldStoryBoard = getStoryBoard(currentStoryBoardId);
        List<Action> oldActions = getActionsOFStoryBoard(currentStoryBoardId);
        deleteStoryBoardWithActions(oldStoryBoard, oldActions);

        insertStoryBoardWithAction(oldStoryBoard, updateActions);

    }


    /**
     * Delete the storyboard with its actions
     * @param storyBoard The storyboard that is going to be deleted
     * @param actions The actions of the storyboard
     */
    @Transaction
    @Delete
    private void deleteStoryBoardWithActions(StoryBoard storyBoard, List<Action> actions){

        Action action;
        for (int i = 0; i < actions.size(); i++) {
            action = actions.get(i);
            deleteAction(action);
            if (action instanceof POI) {
                POI poi = (POI) action;
                deletePOI(poi);
            } else if (action instanceof Movement) {
                Movement movement = (Movement) action;
                deleteMovement(movement);
            } else if (action instanceof Balloon) {
                Balloon balloon = (Balloon) action;
                deleteBalloon(balloon);
            } else if (action instanceof Shape) {
                Shape shape = (Shape) action;
                List<Point> points = shape.points;
                deleteShapeWithPoints(shape, points);
            } else {
                Log.w(TAG_DEBUG, "ERROR TYPE ACTION");
            }
        }

        deleteStoryBoard(storyBoard);
    }


    @Transaction
    @Delete
    public void deleteStoryBoardFormRecyclerView(StoryBoard storyBoard) {
        deleteStoryBoardWithActions(storyBoard, getActionsOFStoryBoard(storyBoard.storyBoardId));
    }
}
