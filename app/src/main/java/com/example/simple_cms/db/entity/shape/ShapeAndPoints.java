package com.example.simple_cms.db.entity.shape;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ShapeAndPoints {

    @Embedded
    public Shape shape;

    @Relation(
            parentColumn = "shapeId",
            entityColumn = "shapeCreatorId"
    )
    public List<Point> points;
}
