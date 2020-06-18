package com.example.simple_cms.create.utility.model.placemark;

import android.net.Uri;
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
 * This class is in charge of creating a placemark in current location
 * The class has a poi with the location information
 * making the class use composition over inheritance
 */
public class PlaceMark extends Action implements IJsonPacker, Parcelable {


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public PlaceMark createFromParcel(Parcel in) {
            return new PlaceMark(in);
        }

        public PlaceMark[] newArray(int size) {
            return new PlaceMark[size];
        }
    };

    private long id;
    private POI poi;
    private String description;
    private Uri imageUri;
    private Uri videoUri;

    /**
     * Empty Constructor
     */
    public PlaceMark() {
        super(ActionIdentifier.PLACE_MARK_ACTIVITY.getId());
    }

    public PlaceMark(long id, POI poi, String description, Uri imageUri, Uri videoUri) {
        super(ActionIdentifier.PLACE_MARK_ACTIVITY.getId());
        this.id = id;
        this.poi = poi;
        this.description = description;
        this.imageUri = imageUri;
        this.videoUri = videoUri;
    }

    public PlaceMark(Parcel in) {
        super(ActionIdentifier.PLACE_MARK_ACTIVITY.getId());
        this.id = in.readLong();
        this.poi = in.readParcelable(POI.class.getClassLoader());
        this.description = in.readString();
        this.imageUri = in.readParcelable(Uri.class.getClassLoader());
        this.videoUri = in.readParcelable(Uri.class.getClassLoader());
    }

    public PlaceMark(PlaceMark placeMark) {
        super(ActionIdentifier.PLACE_MARK_ACTIVITY.getId());
        this.id = placeMark.id;
        this.poi = placeMark.poi;
        this.description = placeMark.description;
        this.imageUri = placeMark.imageUri;
        this.videoUri = placeMark.videoUri;
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

    public PlaceMark setPoi(POI poi) {
        this.poi = poi;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PlaceMark setDescription(String description) {
        this.description = description;
        return this;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public PlaceMark setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
        return this;
    }

    public Uri getVideoUri() {
        return videoUri;
    }

    public PlaceMark setVideoUri(Uri videoUri) {
        this.videoUri = videoUri;
        return this;
    }

    @Override
    public JSONObject pack() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("id", id);
        obj.put("place_mark_poi", poi);
        obj.put("description", description);
        obj.put("description", description);
        obj.put("image_uri", imageUri);
        obj.put("video_uri", videoUri);

        return obj;
    }

    @Override
    public Object unpack(JSONObject obj) throws JSONException {

        id = obj.getLong("id");
        poi = (POI) obj.get("place_mark_poi");
        description = obj.getString("description");
        imageUri = (Uri) obj.get("image_uri");
        videoUri = (Uri) obj.get("video_uri");

        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "Location Name: " + this.poi.getPoiLocation().getName() + " Image Uri: " + this.imageUri.toString()  + " Video Uri: " +  this.videoUri.toString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeParcelable(poi, flags);
        parcel.writeString(description);
        parcel.writeParcelable(imageUri, flags);
        parcel.writeParcelable(videoUri, flags);
    }
}
