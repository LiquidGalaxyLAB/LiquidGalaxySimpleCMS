package com.example.simple_cms.db.entity;

import androidx.room.Embedded;
import androidx.room.Entity;

@Entity
public class Movement extends Action{

    @Embedded
    public long movementId;
    public long poiOwnerMovementId;
    public double newHeading;
    public double newTilt;
    public boolean isOrbitMode;
}
