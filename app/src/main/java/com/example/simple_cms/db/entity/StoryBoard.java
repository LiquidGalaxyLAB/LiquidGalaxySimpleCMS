package com.example.simple_cms.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class StoryBoard {

    @PrimaryKey
    public long storyBoardId;
    public String nameStoryBoard;

}
