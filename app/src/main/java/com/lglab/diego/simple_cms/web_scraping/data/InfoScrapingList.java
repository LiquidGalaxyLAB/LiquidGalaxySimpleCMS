package com.lglab.diego.simple_cms.web_scraping.data;

import android.util.Log;

import com.lglab.diego.simple_cms.create.utility.IJsonPacker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the list of the web scraping info
 */
public class InfoScrapingList implements IJsonPacker {

    private static final String TAG_DEBUG = "ListInfoScrapping";

    private List<InfoScraping> infoScrappingList;

    public InfoScrapingList(){}

    public InfoScrapingList(List<InfoScraping> infoScrappingList) {
        this.infoScrappingList = infoScrappingList;
    }

    public List<InfoScraping> getInfoScrappingList() {
        return infoScrappingList;
    }

    public void setInfoScrappingList(List<InfoScraping> infoScrappingList) {
        this.infoScrappingList = infoScrappingList;
    }

    @Override
    public JSONObject pack() throws JSONException {
        JSONObject obj = new JSONObject();

        JSONArray jsonInfoScrapping = new JSONArray();
        InfoScraping infoScrapping;
        for(int i = 0; i < infoScrappingList.size(); i++){
            infoScrapping = infoScrappingList.get(i);
            if(infoScrapping instanceof TechConferencesSpain){
                TechConferencesSpain techConferencesSpain = (TechConferencesSpain) infoScrapping;
                jsonInfoScrapping.put(techConferencesSpain.pack());
            }else if(infoScrapping instanceof GDG){
                GDG gdg = (GDG) infoScrapping;
                jsonInfoScrapping.put(gdg.pack());
            }else {
                Log.w(TAG_DEBUG, "ERROR TYPE");
            }
        }

        obj.put("jsonInfoScrapping", jsonInfoScrapping);

        return obj;
    }

    @Override
    public Object unpack(JSONObject obj) throws JSONException {
        JSONArray jsonInfoScrapping = obj.getJSONArray("jsonInfoScrapping");
        List<InfoScraping> arrayInfoScrapping = new ArrayList<>();
        JSONObject infoScrappingJson;
        int type;
        for (int i = 0; i < jsonInfoScrapping.length(); i++) {
            infoScrappingJson = jsonInfoScrapping.getJSONObject(i);
            type = infoScrappingJson.getInt("type");
            Log.w("WebScraping", "TYPE: " + type);
            if(type == Constant.TECH_CONFERENCES_SPAIN.getId()){
                TechConferencesSpain techConferencesSpain = new TechConferencesSpain();
                arrayInfoScrapping.add(techConferencesSpain.unpack(infoScrappingJson));
            }else if(type == Constant.GDG.getId()){
                GDG gdg = new GDG();
                arrayInfoScrapping.add(gdg.unpack(infoScrappingJson));
            }else {
                Log.w(TAG_DEBUG, "ERROR TYPE");
            }
        }

        infoScrappingList = arrayInfoScrapping;

        return this;
    }
}
