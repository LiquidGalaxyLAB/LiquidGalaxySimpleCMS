package com.lglab.diego.simple_cms.web_scraping.data;

import com.lglab.diego.simple_cms.create.utility.IJsonPacker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The model to save the info get of the api of https://npatarino.github.io/tech-conferences-spain/
 */
public class TechConferencesSpain extends InfoScraping implements IJsonPacker {

    private String year;
    private String month;
    private String day;
    private String urlTwitter;
    private String urlWebPage;
    private String name;
    private String city;

    public TechConferencesSpain(){
        super(Constant.TECH_CONFERENCES_SPAIN.getId());
    };

    public TechConferencesSpain(String year, String month, String day, String urlTwitter, String urlWebPage, String name, String city) {
        super(Constant.TECH_CONFERENCES_SPAIN.getId());
        this.year = year;
        this.month = month;
        this.day = day;
        this.urlTwitter = urlTwitter;
        this.urlWebPage = urlWebPage;
        this.name = name;
        this.city = city;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getUrlTwitter() {
        return urlTwitter;
    }

    public String getUrlWebPage() {
        return urlWebPage;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    @Override
    public JSONObject pack() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("type", Constant.TECH_CONFERENCES_SPAIN.getId());
        obj.put("year", year);
        obj.put("month", month);
        obj.put("day", day);
        obj.put("urlTwitter", urlTwitter);
        obj.put("urlWebPage", urlWebPage);
        obj.put("name", name);
        obj.put("city", city);

        return obj;
    }

    @Override
    public TechConferencesSpain unpack(JSONObject obj) throws JSONException {

       this.setType(obj.getInt("type"));

        year = obj.getString("year");
        month = obj.getString("month");
        day = obj.getString("day");
        urlTwitter = obj.getString("urlTwitter");
        urlWebPage = obj.getString("urlWebPage");
        name = obj.getString("name");
        city = obj.getString("city");

        return this;
    }
}
