package com.lglab.diego.simple_cms.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lglab.diego.simple_cms.db.dao.StoryBoardDao;
import com.lglab.diego.simple_cms.db.entity.Action;
import com.lglab.diego.simple_cms.db.entity.Balloon;
import com.lglab.diego.simple_cms.db.entity.Movement;
import com.lglab.diego.simple_cms.db.entity.poi.POI;
import com.lglab.diego.simple_cms.db.entity.StoryBoard;
import com.lglab.diego.simple_cms.db.entity.shape.Point;
import com.lglab.diego.simple_cms.db.entity.shape.Shape;

/**
 * Singleton of connection to the database
 */
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
