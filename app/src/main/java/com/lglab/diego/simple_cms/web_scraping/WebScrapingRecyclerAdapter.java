package com.lglab.diego.simple_cms.web_scraping;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.lglab.diego.simple_cms.R;
import com.lglab.diego.simple_cms.web_scraping.data.GDG;
import com.lglab.diego.simple_cms.web_scraping.data.InfoScraping;
import com.lglab.diego.simple_cms.web_scraping.data.Constant;
import com.lglab.diego.simple_cms.web_scraping.data.TechConferencesSpain;

import java.util.List;

/**
 * This is the class in charge of the adapter of the WebScraping recyclerview of the class WebScraping
 */
public class WebScrapingRecyclerAdapter extends RecyclerView.Adapter<WebScrapingRecyclerAdapter.ViewHolder> {

    private static final String TAG_DEBUG = "WebScrapingRecyclerAdapter";

    private List<InfoScraping> infoScrapings;
    private WebScrapingRecyclerAdapter.OnNoteListener mOnNoteListener;

    public WebScrapingRecyclerAdapter(List<InfoScraping> infoScrapings, WebScrapingRecyclerAdapter.OnNoteListener onNoteListener) {
        this.infoScrapings = infoScrapings;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public WebScrapingRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_web_scrapping, parent, false);
        return new WebScrapingRecyclerAdapter.ViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WebScrapingRecyclerAdapter.ViewHolder holder, int position) {
        InfoScraping currentItem = infoScrapings.get(position);
        int type = currentItem.getType();
        if(type == Constant.TECH_CONFERENCES_SPAIN.getId()){
            TechConferencesSpain techConferencesSpain = (TechConferencesSpain) currentItem;
            holder.name.setText(techConferencesSpain.getName());
            String date = techConferencesSpain.getDay() + "-" + techConferencesSpain.getMonth() + "-" +techConferencesSpain.getYear();
            holder.date.setText(date);
            holder.location.setText(techConferencesSpain.getCity());
        }else if(type == Constant.GDG.getId()){
            GDG gdg = (GDG) currentItem;
            holder.name.setText(gdg.getName());
            holder.date.setText(gdg.getStatus());
            String location = gdg.getCity() + ", " + gdg.getCountry();
            holder.location.setText(location);
        }else{
            Log.w(TAG_DEBUG, "ERROR TYPE");
        }
    }

    @Override
    public int getItemCount() {
        return infoScrapings.size();
    }


    /**
     * This is the most efficient way to have the view holder and the click listener
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView date, name, location;
        Button buttonShowLG;
        WebScrapingRecyclerAdapter.OnNoteListener mOnNoteListener;

        ViewHolder(View itemView, WebScrapingRecyclerAdapter.OnNoteListener mOnNoteListener) {
            super(itemView);
            this.name = itemView.findViewById(R.id.file_name);
            this.date = itemView.findViewById(R.id.date);
            this.location = itemView.findViewById(R.id.location);
            buttonShowLG = itemView.findViewById(R.id.butt_show);
            buttonShowLG.setOnClickListener(view -> showLG());
            this.mOnNoteListener = mOnNoteListener;
            itemView.setOnClickListener(this);
        }

        private void showLG(){
            Log.w(TAG_DEBUG, "NAME: " + name.getText());
        }

        @Override
        public void onClick(View view) {
            Log.w(TAG_DEBUG, "onClick: " + getAdapterPosition());
            mOnNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
