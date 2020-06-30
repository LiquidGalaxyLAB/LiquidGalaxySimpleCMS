package com.example.simple_cms.create.utility.model.shape;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.simple_cms.create.utility.IJsonPacker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Shape implements IJsonPacker, Parcelable{

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Shape createFromParcel(Parcel in) {
            return new Shape(in);
        }

        public Shape[] newArray(int size) {
            return new Shape[size];
        }
    };

    private ArrayList<Point> points;
    private boolean isExtrude;

    public Shape(){
        points = new ArrayList<>();
        isExtrude = false;
    }


    public Shape(ArrayList<Point> points, boolean isExtrude) {
       this.points = points;
       this.isExtrude = isExtrude;
    }

    public Shape(Parcel in) {
        this.points = in.readArrayList(Point.class.getClassLoader());
        isExtrude = in.readInt() != 0;
    }

    public Shape(Shape shape){
        this.points = shape.points;
        this.isExtrude = shape.isExtrude;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

    public boolean isExtrude() {
        return isExtrude;
    }

    public void setExtrude(boolean extrude) {
        isExtrude = extrude;
    }

    @Override
    public JSONObject pack() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("point", points);
        obj.put("isExtrude", isExtrude);

        return obj;
    }

    @Override
    public Object unpack(JSONObject obj) throws JSONException {

        this.points = (ArrayList<Point>) obj.get("points");
        this.isExtrude = obj.getBoolean("isExtrude");

        return this;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < points.size(); i++){
            stringBuilder.append("Point ").append(i).append(": ").append(points.get(i).toString()).append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeList(points);
        parcel.writeInt(isExtrude ? 1 : 0);
    }
}
