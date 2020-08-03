package com.lglab.diego.simple_cms.web_scraping;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lglab.diego.simple_cms.R;
import com.lglab.diego.simple_cms.top_bar.TobBarActivity;
import com.lglab.diego.simple_cms.utility.ConstantPrefs;
import com.lglab.diego.simple_cms.web_scraping.data.InfoScrapingList;
import com.lglab.diego.simple_cms.web_scraping.data.Constant;
import com.lglab.diego.simple_cms.web_scraping.data.InfoScraping;
import com.lglab.diego.simple_cms.web_scraping.data.TechConferencesSpain;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebScraping extends TobBarActivity implements
        WebScrapingRecyclerAdapter.OnNoteListener {

    private static final String TAG_DEBUG = "WebScraping";

    private RecyclerView mRecyclerView;
    List<InfoScraping> infoScrapingList = new ArrayList<>();

    private Button buttScraping;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scraping);

        View topBar = findViewById(R.id.top_bar);
        buttScraping = topBar.findViewById(R.id.butt_scraping);
        Button test = findViewById(R.id.butt_test);
        mRecyclerView = findViewById(R.id.my_recycler_view);

        changeButtonClickableBackgroundColor();

        test.setOnClickListener(view -> Scrapping());
    }

    /**
     * Initiate the recycleview
     */
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.Adapter mAdapter = new WebScrapingRecyclerAdapter(infoScrapingList, WebScraping.this);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * It re paints the recyclerview with the actions
     */
    private void rePaintRecyclerView() {
        RecyclerView.Adapter mAdapter = new WebScrapingRecyclerAdapter(infoScrapingList, WebScraping.this);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Create the connection to the Tech Conference Spain
     */
    private void Scrapping() {
        new Thread(() -> {
            try {
                getInfoTechConferencesSpain();
            }catch (IOException e){
                Log.w(TAG_DEBUG, "WEB SCRAPPING EXCEPTION: " + e.getMessage());
            }
            runOnUiThread(this::rePaintRecyclerView);
        }).start();
    }

    /**
     * Obtain the basic information
     * @throws IOException IO ERROR
     */
    private void getInfoTechConferencesSpain() throws IOException {
        Document doc = Jsoup.connect("https://npatarino.github.io/tech-conferences-spain/").get();
        Elements table = doc.getElementsByClass("container");
        Elements hTwo = table.select("div > h2");
        Elements hThree = table.select("div > h3");
        Elements hLu = table.select("div > ul");
        getInfo(hTwo, hThree, hLu);
    }

    /**
     * Get the information and add it to the recycler view
     * @param hTwo YEAR
     * @param hThree MONTH
     * @param hLu DAY, name, urls, city
     */
    private void getInfo(Elements hTwo, Elements hThree, Elements hLu) {
        String year, month, day, urlTwitter, urlWebPage, name, city;
        Element element;
        for(int i = 0; i < hTwo.size(); i++){
            year = String.valueOf(hTwo.get(i));
            for(int j = 1; j < hThree.size(); j++){
                month = String.valueOf(hThree.get(j));
                Elements hLi = hLu.get(j - 1).select("li");
                for(int k = 0; k < hLi.size(); k++){
                    element = hLi.get(k);
                    day = element.ownText();
                    urlTwitter = element.select("a").attr("href");
                    urlWebPage = element.select("a.post-link").attr("href");
                    name = element.select("a.post-link").select(".post-link").text();
                    city = element.select("small").text();
                    TechConferencesSpain techConferencesSpain = new TechConferencesSpain(year, month, day, urlTwitter, urlWebPage, name, city);
                    infoScrapingList.add(techConferencesSpain);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPreviousData();
        initRecyclerView();
    }

    /**
     * Load the previous web scrapping to the recyclerview
     */
    private void loadPreviousData() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        String infoWebScraping = sharedPreferences.getString(Constant.INFO_WEB_SCRAPING.name(), "");
        if (!infoWebScraping.equals("")) {
            try {
                InfoScrapingList infoScraping = new InfoScrapingList();
                JSONObject jsonInfoWebScraping = new JSONObject(infoWebScraping);
                infoScraping.unpack(jsonInfoWebScraping);
                infoScrapingList = infoScraping.getInfoScrappingList();
            } catch (JSONException jsonException) {
                Log.w(TAG_DEBUG, "ERROR CONVERTING JSON: " + jsonException);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            InfoScrapingList infoScraping = new InfoScrapingList();
            infoScraping.setInfoScrappingList(infoScrapingList);
            SharedPreferences.Editor editor = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE).edit();
            editor.putString(Constant.INFO_WEB_SCRAPING.name(), infoScraping.pack().toString());
            editor.apply();
        } catch (JSONException jsonException) {
            Log.w(TAG_DEBUG, "ERROR JSON: " + jsonException);
        }
    }

    /**
     * Change the background color and the option clickable to false of the button_scraping
     */
    private void changeButtonClickableBackgroundColor() {
        changeButtonClickableBackgroundColor(getApplicationContext(), buttScraping);
    }

    @Override
    public void onNoteClick(int position) {
        InfoScraping selected = infoScrapingList.get(position);

    }
}
