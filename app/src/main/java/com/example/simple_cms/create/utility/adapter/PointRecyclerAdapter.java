package com.example.simple_cms.create.utility.adapter.point;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simple_cms.R;
import com.example.simple_cms.create.utility.model.shape.Point;

import java.util.ArrayList;

public class PointRecyclerAdapter extends RecyclerView.Adapter<PointRecyclerAdapter.ViewHolder> {

    private static final String TAG_DEBUG = "BalloonRecyclerAdapter";

    private ArrayList<Point> points;

    public PointRecyclerAdapter(ArrayList<Point> points) {
        this.points = points;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_create_storyboard_action_balloon_point, parent, false);
        return new ViewHolder(view, new MyLongitudeTextListener(), new MyLatitudeTextListener(), new MyAltitudeTextListener());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.w(TAG_DEBUG, "onBindViewHolder called");
        String pointPosition = "Point " + position;
        holder.textView.setText(pointPosition);
        Point point = points.get(position);

        holder.myLongitudeTextListener.updatePosition(holder.getAdapterPosition());
        holder.longitude.setText(String.valueOf(point.getLongitude()));

        holder.myLatitudeTextListener.updatePosition(holder.getAdapterPosition());
        holder.latitude.setText(String.valueOf(point.getLatitude()));

        holder.myAltitudeTextListener.updatePosition(holder.getAdapterPosition());
        holder.altitude.setText(String.valueOf(point.getAltitude()));
    }

    @Override
    public int getItemCount() {
        return points.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        EditText longitude, latitude, altitude;

        MyLongitudeTextListener myLongitudeTextListener;
        MyLatitudeTextListener myLatitudeTextListener;
        MyAltitudeTextListener myAltitudeTextListener;

        ViewHolder(View itemView, MyLongitudeTextListener myLongitudeTextListener,
                   MyLatitudeTextListener myLatitudeTextListener, MyAltitudeTextListener myAltitudeTextListener) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.textView);
            this.longitude = itemView.findViewById(R.id.longitude);
            this.latitude = itemView.findViewById(R.id.latitude);
            this.altitude = itemView.findViewById(R.id.altitude);

            this.myLongitudeTextListener = myLongitudeTextListener;
            this.myLatitudeTextListener = myLatitudeTextListener;
            this.myAltitudeTextListener = myAltitudeTextListener;

            longitude.addTextChangedListener(myLongitudeTextListener);
            latitude.addTextChangedListener(myLatitudeTextListener);
            altitude.addTextChangedListener(myAltitudeTextListener);
        }
    }


    public class MyLatitudeTextListener implements TextWatcher {
        private int position;

        void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            try{
                Point point = points.get(position);
                point.setLatitude(Double.parseDouble(charSequence.toString()));
                points.set(position, point);
            } catch (Exception e){
                Log.w(TAG_DEBUG, "Empty String");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    }

    public class MyAltitudeTextListener implements TextWatcher {
        private int position;

        void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            try {
                Point point = points.get(position);
                point.setAltitude(Double.parseDouble(charSequence.toString()));
                points.set(position, point);
            } catch (Exception e){
                Log.w(TAG_DEBUG, "Empty String");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    }

}
