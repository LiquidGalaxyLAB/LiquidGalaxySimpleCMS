package com.example.simple_cms.db.entity.shape;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

/**
 * This class represent a Shape with his points in the DB
 */
public class ShapeAndPoints {

    @Embedded
    public Shape shape;

    @Relation(
            parentColumn = "actionId",
            entityColumn = "shapeCreatorId"
    )
    public List<Point> points;
}
