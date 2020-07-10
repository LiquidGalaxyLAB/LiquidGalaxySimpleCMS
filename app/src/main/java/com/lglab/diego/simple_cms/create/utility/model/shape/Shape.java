package com.lglab.diego.simple_cms.create.utility.model.shape;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.lglab.diego.simple_cms.create.utility.IJsonPacker;
import com.lglab.diego.simple_cms.create.utility.model.Action;
import com.lglab.diego.simple_cms.create.utility.model.ActionIdentifier;
import com.lglab.diego.simple_cms.create.utility.model.poi.POI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is in charge of the action shape that you can send to LG
 */
public class Shape extends Action implements IJsonPacker, Parcelable{

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Shape createFromParcel(Parcel in) {
            return new Shape(in);
        }

        public Shape[] newArray(int size) {
            return new Shape[size];
        }
    };

    private POI poi;
    private List points;
    private boolean isExtrude;


    public Shape(){
        super(ActionIdentifier.SHAPES_ACTIVITY.getId());
        poi = null;
        points = new ArrayList<>();
        isExtrude = false;
    }


    public Shape(long id, List<Point> points, boolean isExtrude, POI poi) {
       super(id, ActionIdentifier.SHAPES_ACTIVITY.getId());
       this.poi = poi;
       this.points = points;
       this.isExtrude = isExtrude;
    }

    public Shape(Parcel in) {
        super(in.readLong(), ActionIdentifier.SHAPES_ACTIVITY.getId());
        this.poi = in.readParcelable(POI.class.getClassLoader());
        this.points = in.readArrayList(Point.class.getClassLoader());
        this.isExtrude = in.readInt() != 0;
    }

    public Shape(Shape shape){
        super(shape.getId(), ActionIdentifier.SHAPES_ACTIVITY.getId());
        this.poi = shape.poi;
        this.points = shape.points;
        this.isExtrude = shape.isExtrude;
    }

    public static Shape getShape(com.lglab.diego.simple_cms.db.entity.shape.Shape actionDB) {
        POI poi = POI.getSimplePOI(actionDB.actionId, actionDB.simplePOI);
        List<Point> points = Point.getPoints(actionDB.points);
        return new Shape(actionDB.actionId, points, actionDB.isExtrude, poi);
    }


    public List getPoints() {
        return points;
    }

    public Shape setPoints(List<Point> points) {
        this.points = points;
        return this;
    }

    public boolean isExtrude() {
        return isExtrude;
    }

    public Shape setExtrude(boolean extrude) {
        isExtrude = extrude;
        return this;
    }

    public POI getPoi() {
        return poi;
    }

    public Shape setPoi(POI poi) {
        this.poi = poi;
        return this;
    }

    @Override
    public JSONObject pack() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("shape_id", this.getId());
        obj.put("type", this.getType());
        obj.put("shape_poi", poi.pack());
        JSONArray jsonPoints = new JSONArray();
        for(int i = 0; i < points.size(); i++){
            jsonPoints.put(((Point) points.get(i)).pack());
        }
        obj.put("jsonPoints", jsonPoints);
        obj.put("isExtrude", isExtrude);

        return obj;
    }

    @Override
    public Shape unpack(JSONObject obj) throws JSONException {

        this.setId(obj.getLong("shape_id"));
        this.setType(obj.getInt("type"));

        POI newPoi = new POI();
        poi =  newPoi.unpack(obj.getJSONObject("shape_poi"));


        JSONArray jsonPoints =  obj.getJSONArray("jsonPoints");
        List<Point> arrayPoint = new ArrayList<>();
        for(int i = 0; i < jsonPoints.length(); i++){
            Point point = new Point();
            point.unpack(jsonPoints.getJSONObject(i));
            arrayPoint.add(point);
        }
        this.points = arrayPoint;
        this.isExtrude = obj.getBoolean("isExtrude");

        return this;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Location Name: ").append(poi.getPoiLocation().getName());
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
        parcel.writeLong(this.getId());
        parcel.writeParcelable(poi, flags);
        parcel.writeList(points);
        parcel.writeInt(isExtrude ? 1 : 0);
    }
}
