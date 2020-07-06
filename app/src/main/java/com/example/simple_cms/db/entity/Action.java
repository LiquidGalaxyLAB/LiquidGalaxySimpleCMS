package com.example.simple_cms.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Action {

    @PrimaryKey(autoGenerate = true)
    public long actionId;
    public long actionStoryBoardID;
    public int type;

}
