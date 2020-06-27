package com.example.simple_cms.create.utility.model.shape;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.simple_cms.create.utility.IJsonPacker;

import org.json.JSONException;
import org.json.JSONObject;

public class Point implements IJsonPacker, Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Point createFromParcel(Parcel in) {
            return new Point(in);
        }

        public Point[] newArray(int size) {
            return new Point[size];
        }
    };

    private double longitude;
    private double latitude;

    public Point(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Point(Parcel in){
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public JSONObject pack() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("longitude", longitude);
        obj.put("latitude", latitude);

        return obj;
    }

    @Override
    public Object unpack(JSONObject obj) throws JSONException {

        longitude = obj.getDouble("longitude");
        latitude = obj.getDouble("latitude");

        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "Longitude: " + longitude + " Latitude: " + latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
    }
}
