package edu.csc413.tankgame.model;

import edu.csc413.tankgame.WallInformation;

import java.util.UUID;

public class HP extends Entity {

    public HP(String id, double x, double y, double angle) {
        super(id, x, y, angle);
    }

    @Override
    public void move(GameWorld gameWorld) {
    }
}
