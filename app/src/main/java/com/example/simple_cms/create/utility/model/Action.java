package com.example.simple_cms.create.utility.model;

import android.util.Log;

import com.example.simple_cms.create.utility.model.balloon.Balloon;
import com.example.simple_cms.create.utility.model.movement.Movement;
import com.example.simple_cms.create.utility.model.poi.POI;
import com.example.simple_cms.create.utility.model.shape.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 * It is the class base of the action
 */
public abstract class Action {

    private long id;
    private int type;

    public Action(int type){
        this.type = type;
    }

    public Action(long id, int type){
        this.id = id;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static List<Action> getAction(List<com.example.simple_cms.db.entity.Action> actionsOFStoryBoard) {
        List<Action> actions = new ArrayList<>();

        com.example.simple_cms.db.entity.Action actionDB;
        for(int i = 0; i < actionsOFStoryBoard.size(); i++){
            actionDB = actionsOFStoryBoard.get(i);
            if(actionDB instanceof com.example.simple_cms.db.entity.poi.POI){
                POI poi = POI.getPOI((com.example.simple_cms.db.entity.poi.POI) actionDB);
                actions.add(poi);
            }else if(actionDB instanceof com.example.simple_cms.db.entity.Movement){
                Movement movement = Movement.getMovement((com.example.simple_cms.db.entity.Movement) actionDB);
                actions.add(movement);
            }else if(actionDB instanceof com.example.simple_cms.db.entity.Balloon){
                Balloon balloon = Balloon.getBalloon((com.example.simple_cms.db.entity.Balloon) actionDB);
                actions.add(balloon);
            }else if(actionDB instanceof com.example.simple_cms.db.entity.shape.Shape){
                Shape shape = Shape.getShape((com.example.simple_cms.db.entity.shape.Shape) actionDB);
                actions.add(shape);
            }else{
                Log.w("Action", "ERROR TYPE");
            }
        }

        return actions;
    }

}
