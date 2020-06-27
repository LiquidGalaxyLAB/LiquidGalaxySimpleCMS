package com.example.simple_cms.create.utility.model.shape;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class Polygon extends Shape {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Polygon createFromParcel(Parcel in) {
            return new Polygon(in);
        }

        public Polygon[] newArray(int size) {
            return new Polygon[size];
        }
    };

    private Point firstPoint;
    private Point secondPoint;
    private Point thirdPoint;
    private Point fourthPoint;
    private Point fifthPoint;
    private Point sixPoint;

    public Polygon(Point firstPoint, Point secondPoint, Point thirdPoint, Point fourthPoint, Point fifthPoint, Point sixPoint) {
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
        this.thirdPoint = thirdPoint;
        this.fourthPoint = fourthPoint;
        this.fifthPoint = fifthPoint;
        this.sixPoint = sixPoint;
    }

    public Polygon(Parcel in) {
        super(in);
        this.firstPoint = in.readParcelable(Point.class.getClassLoader());
        this.secondPoint = in.readParcelable(Point.class.getClassLoader());
        this.thirdPoint = in.readParcelable(Point.class.getClassLoader());
        this.fourthPoint = in.readParcelable(Point.class.getClassLoader());
        this.fifthPoint = in.readParcelable(Point.class.getClassLoader());
        this.sixPoint = in.readParcelable(Point.class.getClassLoader());
    }

    public Polygon(Polygon polygon){
        super(polygon);
        this.firstPoint = polygon.firstPoint;
        this.secondPoint = polygon.secondPoint;
        this.thirdPoint = polygon.thirdPoint;
        this.fourthPoint = polygon.fourthPoint;
        this.fifthPoint = polygon.fifthPoint;
        this.sixPoint = polygon.sixPoint;
    }

    public Point getFirstPoint() {
        return firstPoint;
    }

    public void setFirstPoint(Point firstPoint) {
        this.firstPoint = firstPoint;
    }

    public Point getSecondPoint() {
        return secondPoint;
    }

    public void setSecondPoint(Point secondPoint) {
        this.secondPoint = secondPoint;
    }

    public Point getThirdPoint() {
        return thirdPoint;
    }

    public void setThirdPoint(Point thirdPoint) {
        this.thirdPoint = thirdPoint;
    }

    public Point getFourthPoint() {
        return fourthPoint;
    }

    public void setFourthPoint(Point fourthPoint) {
        this.fourthPoint = fourthPoint;
    }

    public Point getFifthPoint() {
        return fifthPoint;
    }

    public void setFifthPoint(Point fifthPoint) {
        this.fifthPoint = fifthPoint;
    }

    public Point getSixPoint() {
        return sixPoint;
    }

    public void setSixPoint(Point sixPoint) {
        this.sixPoint = sixPoint;
    }

    @Override
    public JSONObject pack() throws JSONException {
        JSONObject obj = new JSONObject();
        super.pack();

        obj.put("firstPoint", firstPoint);
        obj.put("secondPoint", secondPoint);
        obj.put("thirdPoint", thirdPoint);
        obj.put("fourthPoint", fourthPoint);
        obj.put("fifthPoint", fifthPoint);
        obj.put("sixPoint", sixPoint);

        return obj;
    }

    @Override
    public Object unpack(JSONObject obj) throws JSONException {
        super.unpack(obj);

        this.firstPoint = (Point) obj.get("firstPoint");
        this.secondPoint = (Point) obj.get("secondPoint");
        this.thirdPoint = (Point) obj.get("thirdPoint");
        this.fourthPoint = (Point) obj.get("fourthPoint");
        this.fifthPoint = (Point) obj.get("fifthPoint");
        this.sixPoint = (Point) obj.get("sixPoint");

        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "First Point: " + firstPoint +
                " Second Point: " + secondPoint +
                " Third Point: " + thirdPoint +
                " Fourth point: " + fourthPoint +
                " Fifth Point: " + fifthPoint +
                " Six Point: " + sixPoint;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeParcelable(firstPoint, flags);
        parcel.writeParcelable(secondPoint, flags);
        parcel.writeParcelable(thirdPoint, flags);
        parcel.writeParcelable(fourthPoint, flags);
        parcel.writeParcelable(fifthPoint, flags);
        parcel.writeParcelable(sixPoint, flags);
    }
}
