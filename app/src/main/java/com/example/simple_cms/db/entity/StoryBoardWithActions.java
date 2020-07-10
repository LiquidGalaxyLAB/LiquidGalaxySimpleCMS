package com.example.simple_cms.db.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

/**
 * This class represent a StoryBoard with its actions in the DB
 */
public class StoryBoardWithActions {

    @Embedded
    public StoryBoard storyBoard;

    @Relation(
            parentColumn = "storyBoardId",
            entityColumn = "actionStoryBoardID"
    )
    public List<Action> actions;
}
