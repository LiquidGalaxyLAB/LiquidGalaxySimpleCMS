package com.example.simple_cms.db.entity;

import androidx.room.Embedded;
import androidx.room.Entity;

import com.example.simple_cms.db.entity.poi.SimplePOI;

/**
 * This class represent a Movement in the DB
 */
@Entity
public class Movement extends Action {

    @Embedded
    public SimplePOI simplePOI;
    public double newHeading;
    public double newTilt;
    public boolean isOrbitMode;

    public static Movement getMovementDBMODEL(com.example.simple_cms.create.utility.model.movement.Movement action) {
        Movement movement = new Movement();

        movement.actionId = action.getId();
        movement.type = action.getType();
        movement.simplePOI = SimplePOI.getSIMPLEPOIDBMODEL(action.getPoi());
        movement.newHeading = action.getNewHeading();
        movement.newTilt = action.getNewTilt();
        movement.isOrbitMode = action.isOrbitMode();

        return movement;
    }
}
