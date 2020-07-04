package com.example.simple_cms.db.entity.shape;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.simple_cms.create.utility.model.poi.POI;

import java.util.ArrayList;

@Entity
public class Shape {

    public long poiOwnerShapeId;
    public boolean isExtrude;
}
