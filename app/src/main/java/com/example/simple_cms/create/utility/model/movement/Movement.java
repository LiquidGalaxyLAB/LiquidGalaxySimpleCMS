package com.example.simple_cms.create.utility.model.movement;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.simple_cms.create.utility.IJsonPacker;
import com.example.simple_cms.create.utility.model.Action;
import com.example.simple_cms.create.utility.model.ActionIdentifier;
import com.example.simple_cms.create.utility.model.poi.POI;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is in charge of doing the camera's movement
 * The class has a poi with the location information
 * making the class use composition over inheritance
 */
public class Movement extends Action implements IJsonPacker, Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movement createFromParcel(Parcel in) {
            return new Movement(in);
        }

        public Movement[] newArray(int size) {
            return new Movement[size];
        }
    };

    private POI poi;
    private double newHeading;
    private double newTilt;
    private boolean isOrbitMode;

    /**
     * Empty Constructor
     */
    public Movement(){
        super(ActionIdentifier.MOVEMENT_ACTIVITY.getId());
    }

    public Movement(long id, POI poi, double newHeading, double newTilt, boolean isOrbitMode){
        super(id, ActionIdentifier.MOVEMENT_ACTIVITY.getId());
        this.poi = poi;
        this.newHeading = newHeading;
        this.newTilt = newTilt;
        this.isOrbitMode = isOrbitMode;
    }

    public Movement(Parcel in) {
        super(in.readLong(), ActionIdentifier.MOVEMENT_ACTIVITY.getId());
        this.poi = in.readParcelable(POI.class.getClassLoader());
        this.newHeading = in.readDouble();
        this.newTilt = in.readDouble();
        this.isOrbitMode = in.readInt() != 0;
    }

    public Movement(Movement movement) {
        super(movement.getId(), ActionIdentifier.MOVEMENT_ACTIVITY.getId());
        this.poi = movement.poi;
        this.newHeading = movement.newHeading;
        this.newTilt = movement.newTilt;
        this.isOrbitMode = movement.isOrbitMode;
    }

    public static Movement getMovement(com.example.simple_cms.db.entity.Movement actionDB) {
        POI poi = POI.getSimplePOI(actionDB.actionId, actionDB.simplePOI);
        return new Movement(actionDB.actionId, poi, actionDB.newHeading, actionDB.newTilt, actionDB.isOrbitMode);
    }

    public POI getPoi() {
        return poi;
    }

    public Movement setPoi(POI poi) {
        this.poi = poi;
        return this;
    }

    public double getNewHeading() {
        return newHeading;
    }

    public Movement setNewHeading(double newHeading) {
        this.newHeading = newHeading;
        return this;
    }

    public double getNewTilt() {
        return newTilt;
    }

    public Movement setNewTilt(double newTilt) {
        this.newTilt = newTilt;
        return this;
    }

    public boolean isOrbitMode() {
        return isOrbitMode;
    }

    public Movement setOrbitMode(boolean orbitMode) {
        isOrbitMode = orbitMode;
        return this;
    }

    @Override
    public JSONObject pack() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("movement_id", this.getId());
        obj.put("type", this.getType());
        obj.put("movement_poi", poi.pack());
        obj.put("movement_new_heading", newHeading);
        obj.put("movement_new_tilt", newTilt);
        obj.put("movement_orbit_mode", isOrbitMode);

        return obj;
    }

    @Override
    public Movement unpack(JSONObject obj) throws JSONException {

        this.setId(obj.getLong("movement_id"));
        this.setType(obj.getInt("type"));

        POI newPoi = new POI();
        poi =  newPoi.unpack(obj.getJSONObject("movement_poi"));
        newHeading = obj.getDouble("movement_new_heading");
        newTilt = obj.getDouble("movement_new_tilt");
        isOrbitMode = obj.getBoolean("movement_orbit_mode");

        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "Location Name: " + this.poi.getPoiLocation().getName() + " New Heading: " + this.newHeading + " New Tilt: " +  this.newTilt;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(this.getId());
        parcel.writeParcelable(poi, flags);
        parcel.writeDouble(newHeading);
        parcel.writeDouble(newTilt);
        parcel.writeInt(isOrbitMode ? 1 : 0);
    }
}
