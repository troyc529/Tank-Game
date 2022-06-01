package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;

/**
 * A general concept for an entity in the Tank Game. This includes everything that can move or be interacted with, such
 * as tanks, shells, walls, power ups, etc.
 */
public abstract class Entity {

    private String id;
    private double x;
    private double y;
    private double angle;
    private int hp;


    public Entity(String id, double x, double y, double angle) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.hp = 8;

    }

    public int getHP() {
        return hp;
    }
    public void removeHP(int i){
        hp-=i;
    }
    public void changeHP(int i){
        hp = i;
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public void updateX(double newX){
        this.x = newX;
    }

    public double getXBound(String id) {
        if (id.contains("tank"))
            return x + Constants.TANK_WIDTH;
        else if (id.contains("boom"))
            return x + Constants.SHELL_WIDTH;
        else if (id.contains("wall")||id.contains("hp"))
            return x + Constants.WALL_WIDTH;

        return 0;
    }

    public double getY() {
        return y;
    }

    public void updateY(double newY){
       this.y = newY;
    }

    public double getYBound(String id) {
        if (id.contains("tank"))
            return y + Constants.TANK_HEIGHT;
        else if (id.contains("boom"))
            return y + Constants.SHELL_HEIGHT;
        else if (id.contains("wall")||id.contains("hp"))
            return y + Constants.WALL_HEIGHT;

        return 0;
    }

    public double getAngle() {
        return angle;
    }

    protected void updateID(String uID) {
        this.id = id + uID;
    }

    public void changeID() {
        this.id = this.id.substring(5, id.length());
    }


    protected void moveForward(double movementSpeed) {
        x += movementSpeed * Math.cos(angle);
        y += movementSpeed * Math.sin(angle);
    }

    protected void moveBackward(double movementSpeed) {
        x -= movementSpeed * Math.cos(angle);
        y -= movementSpeed * Math.sin(angle);
    }

    protected void turnLeft(double turnSpeed) {
        angle -= turnSpeed;
    }

    protected void turnRight(double turnSpeed) {
        angle += turnSpeed;
    }

    /**
     * All entities can move, even if the details of their move logic may vary based on the specific type of Entity.
     */
    public abstract void move(GameWorld gameWorld);
}
