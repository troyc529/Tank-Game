package edu.csc413.tankgame.model;
import edu.csc413.tankgame.Constants;
import edu.csc413.tankgame.KeyboardReader;
import edu.csc413.tankgame.view.RunGameView;
import edu.csc413.tankgame.GameDriver;

import java.util.UUID;


public class Player extends Tank{


    public Player(String id, double x, double y, double angle){
        super(id,x,y,angle);
    }

    @Override
    public void move(GameWorld gameWorld) {
        KeyboardReader keyboard = KeyboardReader.instance();


        if (getHP()<=0){
            return;
        }
        if (fireCooldown > 0){
            fireCooldown-=1;
        }
        if (keyboard.upPressed()){
            moveForward(Constants.TANK_MOVEMENT_SPEED);
        }
        if (keyboard.downPressed()){
            moveBackward(Constants.TANK_MOVEMENT_SPEED);
        }
        if(keyboard.leftPressed()){
            turnLeft(Constants.TANK_TURN_SPEED);
        }
        if (keyboard.rightPressed()){
            turnRight(Constants.TANK_TURN_SPEED);
        }
        if (keyboard.spacePressed()){
            if (fireCooldown == 0) {
                fireShell(gameWorld);
                gameWorld.playWav(RunGameView.Shell_fire);
                fireCooldown = 150;
            }
        }
        if (keyboard.escapePressed()){
            gameWorld.endgame();
        }

    }


    private void fireShell(GameWorld gameWorld){
        Shells shell = new Shells("shellboom",getShellX(), getShellY(), getShellAngle());
        gameWorld.addEntity(shell);
    }
}
