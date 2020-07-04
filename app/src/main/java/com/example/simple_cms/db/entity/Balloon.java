package com.example.simple_cms.db.entity;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Balloon extends Action {

    public long poiOwnerBalloonId;
    public String descriptionBalloon;
    public Uri imageUriBalloon;
    public String imagePathBalloon;
    public String videoPathBalloon;

}
