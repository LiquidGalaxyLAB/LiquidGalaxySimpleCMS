package com.example.simple_cms.create.utility.model.shape;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.simple_cms.create.utility.IJsonPacker;
import com.example.simple_cms.create.utility.model.Action;
import com.example.simple_cms.create.utility.model.ActionIdentifier;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Shape extends Action implements IJsonPacker, Parcelable {

    private long id;
    private String shapeType;

    /**
     * Empty Constructor
     */
    public Shape() {
        super(ActionIdentifier.SHAPES_ACTIVITY.getId());
    }

    public Shape(long id, String shapeType) {
        super(ActionIdentifier.SHAPES_ACTIVITY.getId());
        this.id = id;
        this.shapeType = shapeType;
    }

    public Shape(Parcel in) {
        super(ActionIdentifier.SHAPES_ACTIVITY.getId());
        this.id = in.readLong();
        this.shapeType =  in.readString();
    }

    public Shape(Shape shape) {
        super(ActionIdentifier.SHAPES_ACTIVITY.getId());
        this.id = shape.id;
        this.shapeType = shape.shapeType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShapeType() {
        return shapeType;
    }

    public void setType(String shapeType) {
        this.shapeType = shapeType;
    }

    @Override
    public JSONObject pack() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("id", id);
        obj.put("shapeType", shapeType);

        return obj;
    }

    @Override
    public Object unpack(JSONObject obj) throws JSONException {
        id = obj.getLong("id");
        shapeType = obj.getString("shapeType");

        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "Shape Type: " + shapeType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeString(shapeType);
    }
}
