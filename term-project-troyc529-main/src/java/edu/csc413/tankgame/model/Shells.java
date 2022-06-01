package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;

import java.util.UUID;

public class Shells extends Entity{
  private String uniqueID;
    public Shells(String id, double x, double y, double angle){
        super(id,x,y,angle);
        uniqueID = UUID.randomUUID().toString();
        updateID(uniqueID);
    }



    @Override
    public void move(GameWorld gameWorld) {
        moveForward(Constants.SHELL_MOVEMENT_SPEED);
    }
}

