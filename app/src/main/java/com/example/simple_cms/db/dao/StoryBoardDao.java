package com.example.simple_cms.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.simple_cms.db.entity.StoryBoard;
import com.example.simple_cms.db.entity.StoryBoardWithActions;

@Dao
public interface StoryBoardDao {

    @Insert
    void insertStoryBoard(StoryBoard storyBoard);


    @Transaction
    @Query("SELECT * FROM `Action` WHERE storyBoardCreatorID = :id")
    public StoryBoardWithActions getStoryBoardWithActions(long id);

    @Delete
    void delete(StoryBoard StoryBoard);
}
