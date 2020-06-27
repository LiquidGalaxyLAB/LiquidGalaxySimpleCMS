package com.example.simple_cms.create.utility.model.shape;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.simple_cms.create.utility.model.ActionIdentifier;

import org.json.JSONException;
import org.json.JSONObject;

public class Line extends Shape {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Line createFromParcel(Parcel in) {
            return new Line(in);
        }

        public Line[] newArray(int size) {
            return new Line[size];
        }
    };

    private Point initPoint;
    private Point finalPoint;

    public Line(){
        super();
    }

    public Line(long id, String shapeType, Point initPoint, Point finalPoint) {
        super(id, shapeType);
        this.initPoint = initPoint;
        this.finalPoint = finalPoint;
    }

    public Line(Parcel in) {
        super(in);
        this.initPoint = in.readParcelable(Point.class.getClassLoader());
        this.finalPoint = in.readParcelable(Point.class.getClassLoader());
    }

    public Line(Line line){
        super(line);
        this.initPoint = line.initPoint;
        this.finalPoint = line.finalPoint;
    }

    public Point getInitPoint() {
        return initPoint;
    }

    public void setInitPoint(Point initPoint) {
        this.initPoint = initPoint;
    }

    public Point getFinalPoint() {
        return finalPoint;
    }

    public void setFinalPoint(Point finalPoint) {
        this.finalPoint = finalPoint;
    }

    @Override
    public JSONObject pack() throws JSONException {
        JSONObject obj = new JSONObject();
        super.pack();

        obj.put("initPoint", initPoint);
        obj.put("finalPoint", finalPoint);

        return obj;
    }

    @Override
    public Object unpack(JSONObject obj) throws JSONException {
        super.unpack(obj);

        initPoint = (Point) obj.get("initPoint");
        finalPoint = (Point) obj.get("finalPoint");

        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "Init Point: " + initPoint + " Final Point: " + finalPoint;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeParcelable(initPoint, flags);
        parcel.writeParcelable(finalPoint, flags);
    }
}
