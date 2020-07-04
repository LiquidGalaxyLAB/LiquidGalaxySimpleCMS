package com.example.simple_cms.db.entity.poi;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.simple_cms.db.entity.shape.Shape;

public class POIAndShape {

    @Embedded
    public POI poi;

    @Relation(
            parentColumn = "poiId",
            entityColumn = "poiOwnerShapeId"
    )
    public Shape shapeAction;
}
