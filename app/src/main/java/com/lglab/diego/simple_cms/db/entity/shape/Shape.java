package com.lglab.diego.simple_cms.db.entity.shape;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.lglab.diego.simple_cms.db.entity.Action;
import com.lglab.diego.simple_cms.db.entity.poi.SimplePOI;

import java.util.List;

/**
 * This class represent a Shape in the DB
 */
@Entity
public class Shape extends Action {

    @Embedded
    public SimplePOI simplePOI;
    public boolean isExtrude;
    public int duration;

    @Ignore
    public List points;

    public static Shape getShapeDBMODEL(com.lglab.diego.simple_cms.create.utility.model.shape.Shape action) {
        Shape shape = new Shape();

        shape.actionId = action.getId();
        shape.type = action.getType();
        shape.simplePOI = SimplePOI.getSIMPLEPOIDBMODEL(action.getPoi());
        shape.isExtrude = action.isExtrude();
        shape.points = Point.getPointsDBMODEL(action.getPoints());
        shape.duration = action.getDuration();

        return shape;
    }
}
