package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;
import edu.csc413.tankgame.view.RunGameView;

import java.util.UUID;

public class AI extends Tank {
    private String uniqueID;

    public AI(String id, double x, double y, double angle) {
        super(id, x, y, angle);
        uniqueID = UUID.randomUUID().toString();
        updateID(uniqueID);
    }


    @Override
    public void move(GameWorld gameWorld) {
        Entity playerTank = gameWorld.getEntity(Constants.PLAYER_TANK_ID);
        double dx = playerTank.getX() - getX();
        double dy = playerTank.getY() - getY();
        double angleToPlayer = Math.atan2(dy, dx);
        double angleDifference = getAngle() - angleToPlayer;
        angleDifference -=
                Math.floor(angleDifference / Math.toRadians(360.0) + 0.5)
                        * Math.toRadians(360.0);
        if (getId().contains("tank-1")) {
            if (fireCooldown > -20) {
                fireCooldown -= 1;
            }

            if (angleDifference < -Math.toRadians(3.0)) {
                turnRight(Constants.TANK_TURN_SPEED);
                moveForward(Constants.TANK_MOVEMENT_SPEED / 2);
            } else if (angleDifference > Math.toRadians(3.0)) {
                turnLeft(Constants.TANK_TURN_SPEED);
                moveForward(Constants.TANK_MOVEMENT_SPEED / 2);
            }
            if (getX() - playerTank.getX() < 15 && getX() - playerTank.getX() > -15
                    || getY() - playerTank.getY() < 15 && getY() - playerTank.getY() > -15) {
                moveBackward(Constants.TANK_MOVEMENT_SPEED/2);
            }
        }
            if (getId().contains("tank-2")) {
                if (fireCooldown > -20) {
                    fireCooldown -= 1;
                }

                if (getX() - playerTank.getX() < 35 && getX() - playerTank.getX() > -35
                        || getY() - playerTank.getY() > 35 && getY() - playerTank.getY() > -35) {

                    if (angleDifference < -Math.toRadians(3.0)) {
                        turnRight(Constants.TANK_TURN_SPEED);
                    } else if (angleDifference > Math.toRadians(3.0)) {
                        turnLeft(Constants.TANK_TURN_SPEED);
                    }
                } else {
                    moveForward(Constants.TANK_MOVEMENT_SPEED / 2);
                }
            }


            if (fireCooldown == -20) {
                fireShell(gameWorld);
                gameWorld.playWav(RunGameView.Shell_fire);
                fireCooldown = 180;
            }


    }

    private void fireShell(GameWorld gameWorld) {
        Shells shell = new Shells("shellboom", getShellX(), getShellY(), getShellAngle());
        gameWorld.addEntity(shell);
    }

}