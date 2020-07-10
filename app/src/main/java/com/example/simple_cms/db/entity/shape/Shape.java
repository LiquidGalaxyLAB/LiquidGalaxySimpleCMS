package com.example.simple_cms.db.entity.shape;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.example.simple_cms.db.entity.Action;
import com.example.simple_cms.db.entity.poi.SimplePOI;

import java.util.List;

/**
 * This class represent a Shape in the DB
 */
@Entity
public class Shape extends Action {

    @Embedded
    public SimplePOI simplePOI;
    public boolean isExtrude;

    @Ignore
    public List points;

    public static Shape getShapeDBMODEL(com.example.simple_cms.create.utility.model.shape.Shape action) {
        Shape shape = new Shape();

        shape.actionId = action.getId();
        shape.type = action.getType();
        shape.simplePOI = SimplePOI.getSIMPLEPOIDBMODEL(action.getPoi());
        shape.isExtrude = action.isExtrude();
        shape.points = Point.getPointsDBMODEL(action.getPoints());

        return shape;
    }
}
