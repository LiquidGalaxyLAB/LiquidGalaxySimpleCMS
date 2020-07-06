package com.example.simple_cms.db.entity;

import android.net.Uri;

import androidx.room.Embedded;
import androidx.room.Entity;

import com.example.simple_cms.db.entity.poi.SimplePOI;

@Entity
public class Balloon extends Action {

    @Embedded
    public SimplePOI simplePOI;
    public String descriptionBalloon;
    public String imageUriBalloon;
    public String imagePathBalloon;
    public String videoPathBalloon;

    public static Balloon getBalloonDBMODEL(com.example.simple_cms.create.utility.model.balloon.Balloon action) {
        Balloon balloon = new Balloon();

        balloon.actionId = action.getId();
        balloon.type = action.getType();
        balloon.simplePOI = SimplePOI.getSIMPLEPOIDBMODEL(action.getPoi());
        balloon.descriptionBalloon = action.getDescription();
        //to parse it use Uri.parse(string)
        balloon.imageUriBalloon = action.getImageUri() != null ? action.getImageUri().toString() : "";
        balloon.imagePathBalloon = action.getImagePath();
        balloon.videoPathBalloon = action.getVideoPath();

        return balloon;
    }

}
