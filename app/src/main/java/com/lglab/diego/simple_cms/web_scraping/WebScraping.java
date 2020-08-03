package com.lglab.diego.simple_cms.web_scraping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.lglab.diego.simple_cms.R;
import com.lglab.diego.simple_cms.top_bar.TobBarActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WebScraping extends TobBarActivity {

    private static final String TAG_DEBUG = "WebScraping";

    private Button buttScraping, test;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scraping);

        View topBar = findViewById(R.id.top_bar);
        buttScraping = topBar.findViewById(R.id.butt_scraping);
        test = findViewById(R.id.butt_test);

        changeButtonClickableBackgroundColor();

        test.setOnClickListener(view -> Scrapping());
    }

    private void Scrapping() {

/*
        document.querySelector("x-gdgmain").shadowRoot.querySelector("search-gdg").shadowRoot.querySelector("x-table").shadowRoot.querySelector("table");
*/

        /*WebView webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        MyJavaScriptInterface jInterface = new MyJavaScriptInterface(getApplicationContext());
        webView.addJavascriptInterface(jInterface, "HtmlViewer");
        webView.loadUrl("https://gdgsearch.com/");

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                //Load HTML
                webView.evaluateJavascript(
                        "(function() { return (document.querySelector(\"x-gdgmain\").shadowRoot.querySelector(\"search-gdg\").shadowRoot.querySelector(\"x-table\").shadowRoot.querySelector(\"table\")); })();",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String html) {
                                Log.w(TAG_DEBUG, "HTML: " +html);
                                // code here
                            }
                        });
            }

        });*/

        new Thread(() -> {
            final StringBuilder builder = new StringBuilder();
            try {

                Document doc = Jsoup.connect("https://npatarino.github.io/tech-conferences-spain/").get();

                Elements table = doc.getElementsByClass("container");
                Elements hTwo = table.select("div > h2");
                Elements hThree = table.select("div > h3");
                Elements hLu = table.select("div > ul");
                for(int i = 0; i < hTwo.size(); i++){
                    Log.w(TAG_DEBUG, "H2: " +  hTwo.get(i));
                    for(int j = 1; j < hThree.size(); j++){
                        Log.w(TAG_DEBUG, "H3: " +  hThree.get(j));
                        Log.w(TAG_DEBUG, "hLu: " +  hLu.get(j - 1));
                        Elements hLi = hLu.get(j - 1).select("li");
                        for(int k = 0; k < hLi.size(); k++){
                            Log.w(TAG_DEBUG, "hLi: " +  hLi.get(k));
                            //TODO
                        }
                    }
                }


            }catch (IOException e){
                Log.w(TAG_DEBUG, "WEB SCRAPPING EXCEPTION: " + e.getMessage());
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //llenar el UI
                }
            });
        }).start();
    }


    /**
     * Change the background color and the option clickable to false of the button_scraping
     */
    private void changeButtonClickableBackgroundColor() {
        changeButtonClickableBackgroundColor(getApplicationContext(), buttScraping);
    }

    public class MyJavaScriptInterface {

        private Context ctx;
        public String html;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showHTML(String _html) {
            html = _html;
        }
    }
}
