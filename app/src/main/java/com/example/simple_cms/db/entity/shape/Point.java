package com.example.simple_cms.db.entity.shape;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Point {

    @PrimaryKey
    public long pointId;

    public long shapeCreatorId;

    public double pointLongitude;
    public double pointLatitude;
    public double pointAltitude;
}
