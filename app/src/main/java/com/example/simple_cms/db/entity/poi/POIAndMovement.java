package com.example.simple_cms.db.entity.poi;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.simple_cms.db.entity.Movement;


public class POIAndMovement {

    @Embedded
    public POI poi;

    @Relation(
            parentColumn = "poiId",
            entityColumn = "poiOwnerMovementId"
    )
    public Movement movement;
}
