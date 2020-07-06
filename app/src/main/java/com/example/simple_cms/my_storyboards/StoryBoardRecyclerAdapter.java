package com.example.simple_cms.my_storyboards;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simple_cms.R;
import com.example.simple_cms.create.utility.model.StoryBoard;

import java.util.List;

public class StoryBoardRecyclerAdapter extends RecyclerView.Adapter<StoryBoardRecyclerAdapter.ViewHolder> {

    private static final String TAG_DEBUG = "StoryBoardRecyclerAdapter";


    private List<StoryBoard> storyBoards;
    private StoryBoardRecyclerAdapter.OnNoteListener mOnNoteListener;

    public StoryBoardRecyclerAdapter(List<StoryBoard> storyBoards, StoryBoardRecyclerAdapter.OnNoteListener onNoteListener) {
        this.storyBoards = storyBoards;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public StoryBoardRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_my_storyboard, parent, false);
        return new StoryBoardRecyclerAdapter.ViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryBoardRecyclerAdapter.ViewHolder holder, int position) {
        Log.w(TAG_DEBUG, "onBindViewHolder called");
        StoryBoard currentItem = storyBoards.get(position);
        holder.fileNameText.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return storyBoards.size();
    }


    /**
     * This is the most efficient way to have the view holder and the click listener
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView fileName, fileNameText;
        StoryBoardRecyclerAdapter.OnNoteListener mOnNoteListener;

        ViewHolder(View itemView, StoryBoardRecyclerAdapter.OnNoteListener onNoteListener) {
            super(itemView);

            this.fileName = itemView.findViewById(R.id.file_name);
            this.fileNameText = itemView.findViewById(R.id.file_name_text);
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
