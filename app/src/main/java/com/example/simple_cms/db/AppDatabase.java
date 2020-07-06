package com.example.simple_cms.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.simple_cms.db.dao.StoryBoardDao;
import com.example.simple_cms.db.entity.Action;
import com.example.simple_cms.db.entity.Balloon;
import com.example.simple_cms.db.entity.Movement;
import com.example.simple_cms.db.entity.poi.POI;
import com.example.simple_cms.db.entity.StoryBoard;
import com.example.simple_cms.db.entity.poi.SimplePOI;
import com.example.simple_cms.db.entity.shape.Point;
import com.example.simple_cms.db.entity.shape.Shape;

@Database(entities = {StoryBoard.class, Action.class, POI.class, Movement.class, Balloon.class, Shape.class, Point.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract StoryBoardDao storyBoardDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =  Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "story_board").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
