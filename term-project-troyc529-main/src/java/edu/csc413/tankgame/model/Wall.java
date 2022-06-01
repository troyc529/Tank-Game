package edu.csc413.tankgame.model;

import edu.csc413.tankgame.WallInformation;

import java.util.UUID;

public class Wall extends Entity{
    private String uniqueID;
    public Wall(WallInformation wall){
        super(wall.getImageFile(), wall.getX(), wall.getY(), 0);
        uniqueID = UUID.randomUUID().toString();
        updateID(uniqueID);
    }

    @Override
    public void move(GameWorld gameWorld){
    }
}
