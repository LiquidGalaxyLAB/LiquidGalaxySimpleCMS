package com.example.simple_cms.create.utility.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simple_cms.R;
import com.example.simple_cms.create.utility.model.shape.Shape;

import java.util.ArrayList;

public class ShapeRecyclerAdapter extends RecyclerView.Adapter<ShapeRecyclerAdapter.ViewHolder> {

    private static final String TAG_DEBUG = "BalloonRecyclerAdapter";


    private AppCompatActivity activity;
    private ArrayList<Shape> shapes;
    private OnNoteListener mOnNoteListener;

    public ShapeRecyclerAdapter(AppCompatActivity activity, ArrayList<Shape> shapes, OnNoteListener onNoteListener) {
        this.activity = activity;
        this.shapes = shapes;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_create_storyboard_action_balloon_point, parent, false);
        return new ViewHolder(view, mOnNoteListener, new ViewHolder.MyTextListener());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.w(TAG_DEBUG, "onBindViewHolder called");
        holder.textView.setTag(position);
        holder.textView.setText("Point " + position);
        /*holder.longitude.setText(String.valueOf(shapes.get(position).getPoints().get(position).getLongitude()));
        holder.latitude.setText(String.valueOf(shapes.get(position).getPoints().get(position).getLatitude()));
        holder.altitude.setText(String.valueOf(shapes.get(position).getPoints().get(position).getAltitude()));*/
    }

    @Override
    public int getItemCount() {
        return shapes.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;
        EditText longitude, latitude, altitude;

        OnNoteListener mOnNoteListener;

        ViewHolder(View itemView, OnNoteListener onNoteListener, MyTextListener myTextListener) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.textView);
            this.longitude = itemView.findViewById(R.id.longitude);
            this.latitude = itemView.findViewById(R.id.latitude);
            this.altitude = itemView.findViewById(R.id.altitude);
            this.mOnNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
            textView.addTextChangedListener(myTextListener);
            longitude.addTextChangedListener(myTextListener);
            latitude.addTextChangedListener(myTextListener);
            altitude.addTextChangedListener(myTextListener);
        }

        private static class MyTextListener implements TextWatcher {
            private int position;

            public void updatePosition(int position) {
                this.position = position;
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mDataset[position] = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
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
