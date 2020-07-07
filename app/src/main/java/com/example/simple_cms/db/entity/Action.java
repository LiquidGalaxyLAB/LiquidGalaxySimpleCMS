package com.example.simple_cms.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Action {

    @PrimaryKey(autoGenerate = true)
    public long actionId;
    public long actionStoryBoardID;
    public int type;

    public static List<Action> getActionDBModel(List<com.example.simple_cms.create.utility.model.Action> actions, long actionStoryBoardID) {
        List<Action> actionsDB = new ArrayList<>();

        com.example.simple_cms.create.utility.model.Action action;
        for(int i = 0; i < actions.size(); i++){
            action = actions.get(i);
            Action actionDB = new Action();
            actionDB.actionId = action.getId();
            actionDB.actionStoryBoardID = actionStoryBoardID;
            actionDB.type = action.getType();
            actionsDB.add(actionDB);
        }

        return actionsDB;
    }
}
