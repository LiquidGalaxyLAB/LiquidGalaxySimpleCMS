package com.example.simple_cms.create.utility.model;

import java.util.ArrayList;
import java.util.List;

public class StoryBoard {

    private long storyBoardId;
    private String name;
    private ArrayList<Action> actions;

    public StoryBoard(){}

    public StoryBoard(long storyBoardId, String name, ArrayList<Action> actions) {
        this.storyBoardId = storyBoardId;
        this.name = name;
        this.actions = actions;
    }


    public long getStoryBoardId() {
        return storyBoardId;
    }

    public void setStoryBoardId(long storyBoardId) {
        this.storyBoardId = storyBoardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }

    public static List<StoryBoard> getStoryBoardsWithOutActions(List<com.example.simple_cms.db.entity.StoryBoard> storyBoardsDB) {
        List<StoryBoard> storyBoards = new ArrayList<>();

        com.example.simple_cms.db.entity.StoryBoard storyBoardDB;
        for(int i = 0; i < storyBoardsDB.size(); i++){
            storyBoardDB = storyBoardsDB.get(i);

            StoryBoard storyBoard = new StoryBoard();
            storyBoard.storyBoardId = storyBoardDB.storyBoardId;
            storyBoard.name = storyBoardDB.nameStoryBoard;
            storyBoard.actions = new ArrayList<>();

            storyBoards.add(storyBoard);
        }

        return storyBoards;
    }
}
