package com.example.simple_cms.create.utility.model;

/**
 * It is the class base of the action
 */
public class Action {

    private int type;

    public Action(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
