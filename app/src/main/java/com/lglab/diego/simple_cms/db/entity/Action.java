package com.lglab.diego.simple_cms.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


/**
 * This class represent an Action in the DB
 */
@Entity
public class Action {

    @PrimaryKey(autoGenerate = true)
    public long actionId;
    public long actionStoryBoardID;
    public int type;

}
