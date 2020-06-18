package com.example.simple_cms.create.utility.model;

public enum ActionIdentifier {

    IS_DELETE(-2), IS_SAVE(-1), POSITION(0), LOCATION_ACTIVITY(1),
    MOVEMENT_ACTIVITY(2), PLACE_MARK_ACTIVITY(3), SHAPES_ACTIVITY(4);

    private final int id;

    ActionIdentifier(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
