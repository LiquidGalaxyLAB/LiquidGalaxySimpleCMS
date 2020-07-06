package com.example.simple_cms.db.entity.shape;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Point {

    @PrimaryKey(autoGenerate = true)
    public long pointId;

    public long shapeCreatorId;

    public double pointLongitude;
    public double pointLatitude;
    public double pointAltitude;

    public static List getPointsDBMODEL(ArrayList points) {
        List pointsDBMODEL = new ArrayList();

        for(int i = 0; i < points.size(); i++){
            Point pointDBMODEL = new Point();
            com.example.simple_cms.create.utility.model.shape.Point point = (com.example.simple_cms.create.utility.model.shape.Point) points.get(i);
            pointDBMODEL.pointLongitude = point.getLongitude();
            pointDBMODEL.pointLatitude = point.getLatitude();
            pointDBMODEL.pointAltitude = point.getAltitude();
            pointsDBMODEL.add(pointDBMODEL);
        }

        return pointsDBMODEL;
    }
}
