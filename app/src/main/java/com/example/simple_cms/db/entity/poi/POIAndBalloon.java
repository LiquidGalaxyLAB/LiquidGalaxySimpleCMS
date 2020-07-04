package com.example.simple_cms.db.entity.poi;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.simple_cms.db.entity.Balloon;

public class POIAndBalloon {

    @Embedded
    public POI poi;

    @Relation(
            parentColumn = "poiId",
            entityColumn = "poiOwnerBalloonId"
    )
    public Balloon balloon;
}
