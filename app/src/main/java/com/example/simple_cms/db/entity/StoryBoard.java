package com.example.simple_cms.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


/**
 * This class represent a StoryBoard in the DB
 */
@Entity
public class StoryBoard {

    @PrimaryKey(autoGenerate = true)
    public long storyBoardId;
    public String nameStoryBoard;

    public static StoryBoard getStoryBoardDBModel(com.example.simple_cms.create.utility.model.StoryBoard storyBoard) {
        StoryBoard storyBoardDBModel = new StoryBoard();

        storyBoardDBModel.storyBoardId = storyBoard.getStoryBoardId();
        storyBoardDBModel.nameStoryBoard = storyBoard.getName();

        return  storyBoardDBModel;
    }
}
