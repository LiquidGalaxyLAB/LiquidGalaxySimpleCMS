package com.example.simple_cms.create.utility.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simple_cms.R;
import com.example.simple_cms.create.utility.model.Action;
import com.example.simple_cms.create.utility.model.ActionIdentifier;

import java.util.ArrayList;

public class ActionRecyclerAdapter extends RecyclerView.Adapter<ActionRecyclerAdapter.ViewHolder> {

    private static final String TAG_DEBUG = "ActionRecyclerAdapter";


    private AppCompatActivity activity;
    private ArrayList<Action> actions;
    private OnNoteListener mOnNoteListener;

    public ActionRecyclerAdapter(AppCompatActivity activity, ArrayList<Action> actions, OnNoteListener onNoteListener) {
        this.activity = activity;
        this.actions = actions;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_create_storyboard_action_icon, parent, false);
        return new ViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.w(TAG_DEBUG, "onBindViewHolder called");
        Action currentItem = actions.get(position);
        int type = currentItem.getType();
        if(type == ActionIdentifier.LOCATION_ACTIVITY.getId()){
            holder.imageView.setBackground(ContextCompat.getDrawable(activity, R.drawable.ic_icon_action_location));
        }else if(type == ActionIdentifier.MOVEMENT_ACTIVITY.getId()){
            holder.imageView.setBackground(ContextCompat.getDrawable(activity, R.drawable.ic_icon_action_movements));
        }else if(type == ActionIdentifier.GRAPHICS_ACTIVITY.getId()){
            holder.imageView.setBackground(ContextCompat.getDrawable(activity, R.drawable.ic_icon_action_graphic));
        }else if(type == ActionIdentifier.SHAPES_ACTIVITY.getId()){
            holder.imageView.setBackground(ContextCompat.getDrawable(activity, R.drawable.ic_icon_action_shape));
        }else if (type == ActionIdentifier.DESCRIPTION_ACTIVITY.getId()){
            holder.imageView.setBackground(ContextCompat.getDrawable(activity, R.drawable.ic_icon_action_description));
        } else {
            Log.w(TAG_DEBUG, "ERROR IN TYPE");
        }
    }

    @Override
    public int getItemCount() {
        return actions.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        OnNoteListener mOnNoteListener;

        ViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageView);
            this.mOnNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
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
