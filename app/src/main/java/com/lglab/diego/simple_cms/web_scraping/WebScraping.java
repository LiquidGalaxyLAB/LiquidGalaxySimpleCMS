package com.lglab.diego.simple_cms.web_scraping;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lglab.diego.simple_cms.R;
import com.lglab.diego.simple_cms.dialog.CustomDialogUtility;
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

    private TextView connectionStatus, imageAvailable;
    private Button buttScraping;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scraping);

        View topBar = findViewById(R.id.top_bar);
        buttScraping = topBar.findViewById(R.id.butt_scraping);
        Button buttGDG = findViewById(R.id.butt_gdg);
        Button buttTCS = findViewById(R.id.butt_tcs);
        Button buttUpdate = findViewById(R.id.butt_refresh);
        mRecyclerView = findViewById(R.id.my_recycler_view);

        connectionStatus = findViewById(R.id.connection_status);
        imageAvailable = findViewById(R.id.image_text);

        changeButtonClickableBackgroundColor();

        buttGDG.setOnClickListener(view -> scrappingGDG());
        buttTCS.setOnClickListener(view -> scrappingTCS());
        buttUpdate.setOnClickListener(view -> updateScraping());
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
     * Refresh the scraping
     */
    private void updateScraping() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        int refreshWebScraping = sharedPreferences.getInt(Constant.REFRESH_WEB_SCRAPING.name(), 0);
        if(refreshWebScraping == 1){
            scrappingTCS();
        }else if(refreshWebScraping == 2){
            scrappingGDG();
        }else{
            CustomDialogUtility.showDialog(WebScraping.this, getResources().getString(R.string.message_update_web_scraping));
        }
    }

    /**
     * Create the connection to the Tech Conference Spain
     */
    private void scrappingTCS() {
        CustomDialogUtility.showDialog(WebScraping.this, getResources().getString(R.string.message_downloading_data_tcs));
        SharedPreferences.Editor editor = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE).edit();
        editor.putInt(Constant.REFRESH_WEB_SCRAPING.name(), 1);
        editor.apply();
        new Thread(() -> {
            try {
                getInfoTechConferencesSpain();
            }catch (IOException e){
                CustomDialogUtility.showDialog(this, getResources().getString(R.string.message_error_connection));
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
        infoScrapingList.clear();
        String year, month, day, urlTwitter, urlWebPage, name, city;
        Element element;
        for(int i = 0; i < hTwo.size(); i++){
            year = String.valueOf(hTwo.get(i).ownText());
            Log.w(TAG_DEBUG, "YEAR: " + year);
            for(int j = 1; j < hThree.size(); j++){
                month = String.valueOf(hThree.get(j).ownText());
                Log.w(TAG_DEBUG, "MONTH: " + month);
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

    /**
     * Create the connection to the Google Meet GDG
     */
    private void scrappingGDG() {
        CustomDialogUtility.showDialog(WebScraping.this, getResources().getString(R.string.message_downloading_data_gdg));
        SharedPreferences.Editor editor = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE).edit();
        editor.putInt(Constant.REFRESH_WEB_SCRAPING.name(), 2);
        editor.apply();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadPreviousData();
        loadConnectionStatus(getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE));
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
     * Set the connection status on the view
     */
    private void loadConnectionStatus(SharedPreferences sharedPreferences) {
        boolean isConnected = sharedPreferences.getBoolean(ConstantPrefs.IS_CONNECTED.name(), false);
        if (isConnected) {
            connectionStatus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_status_connection_green));
            imageAvailable.setText(getResources().getString(R.string.image_available_on_screen));
        }else{
            connectionStatus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_status_connection_red));
            imageAvailable.setText(getResources().getString(R.string.image_not_available_on_screen));
        }
    }

    /**
     * Change the background color and the option clickable to false of the button_scraping
     */
    private void changeButtonClickableBackgroundColor() {
        changeButtonClickableBackgroundColor(getApplicationContext(), buttScraping);
    }

    @Override
    public void onNoteClick(int position) {}
}
