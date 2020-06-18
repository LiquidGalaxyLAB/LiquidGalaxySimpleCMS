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

    private long id;
    private POI poi;
    private double newHeading;
    private double newTilt;

    /**
     * Empty Constructor
     */
    public Movement(){
        super(ActionIdentifier.MOVEMENT_ACTIVITY.getId());
    }

    public Movement(long id, POI poi, double newHeading, double newTilt){
        super(ActionIdentifier.MOVEMENT_ACTIVITY.getId());
        this.id = id;
        this.poi = poi;
        this.newHeading = newHeading;
        this.newTilt = newTilt;
    }

    public Movement(Parcel in) {
        super(ActionIdentifier.MOVEMENT_ACTIVITY.getId());
        this.id = in.readLong();
        this.poi = in.readParcelable(POI.class.getClassLoader());
        this.newHeading = in.readDouble();
        this.newTilt = in.readDouble();
    }

    public Movement(Movement movement) {
        super(ActionIdentifier.MOVEMENT_ACTIVITY.getId());
        this.id = movement.id;
        this.poi = movement.poi;
        this.newHeading = movement.newHeading;
        this.newTilt = movement.newTilt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public JSONObject pack() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("id", id);
        obj.put("movement_poi", poi);
        obj.put("movement_new_heading", newHeading);
        obj.put("movement_new_tilt", newTilt);

        return obj;
    }

    @Override
    public Object unpack(JSONObject obj) throws JSONException {

        id = obj.getLong("id");
        poi = (POI) obj.get("movement_poi");
        newHeading = obj.getDouble("movement_new_heading");
        newHeading = obj.getDouble("movement_new_tilt");

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
        parcel.writeLong(id);
        parcel.writeParcelable(poi, flags);
        parcel.writeDouble(newHeading);
        parcel.writeDouble(newTilt);
    }
}
