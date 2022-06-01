package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;
import edu.csc413.tankgame.view.RunGameView;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.*;


/**
 * GameWorld holds all of the model objects present in the game. GameWorld tracks all moving entities like tanks and
 * shells, and provides access to this information for any code that needs it (such as GameDriver or entity classes).
 */
public class GameWorld {
    // TODO: Implement. There's a lot of information the GameState will need to store to provide contextual information.
    //       Add whatever instance variables, constructors, and methods are needed.
    private boolean endgame;
    private List<Entity> entities;

    public GameWorld() {
        // TODO: Implement.
        entities = new ArrayList<>();
    }

    /**
     * Returns a list of all entities in the game.
     */
    public List<Entity> getEntities() {
        // TODO: Implement.
        return entities;
    }

    public void endgame() {
        endgame = false;
    }

    public void startgame() {
        endgame = true;
    }

    public boolean getendgame() {
        return endgame;
    }

    /**
     * Adds a new entity to the game.
     */
    public void addEntity(Entity entity) {
        // TODO: Implement.
        entities.add(entity);
    }

    /**
     * Returns the Entity with the specified ID.
     */
    public Entity getEntity(String id) {
        // TODO: Implement.
        for (Entity entity : entities) {
            if (entity.getId().equals(id)) {
                return entity;
            }
        }

        return null;
    }

    public static void playWav(String sound) {
        try {
            InputStream in = new FileInputStream(sound);
            InputStream bufferedIn = new BufferedInputStream(in);
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(bufferedIn);
            clip.open(inputStream);
            clip.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public boolean collisionDet(Entity entity, Entity entity1) {

        if (entity.getId().contains("tank")) {
            if (entity.getX() < Constants.TANK_X_LOWER_BOUND) {

                entity.updateX(entity.getX() - (entity.getXBound(entity.getId()) - Constants.TANK_X_UPPER_BOUND));
            }
            if (entity.getX() > Constants.TANK_X_UPPER_BOUND) {
                entity.updateX(entity.getXBound(entity.getId()) - Constants.TANK_X_UPPER_BOUND);
            }
            if (entity.getY() < Constants.TANK_Y_LOWER_BOUND) {
                entity.updateY(entity.getY() - (entity.getYBound(entity.getId()) - Constants.TANK_Y_UPPER_BOUND));
            }
            if (entity.getY() > Constants.TANK_Y_UPPER_BOUND) {

                entity.updateY(entity.getYBound(entity.getId()) - Constants.TANK_Y_UPPER_BOUND);
            }
        }
        if (entity.getId().equals(entity1.getId())) {
            return false;
        }
        if (entity.getX() < entity1.getXBound(entity1.getId()) &&
                entity.getXBound(entity.getId()) > entity1.getX() &&
                entity.getY() < entity1.getYBound(entity1.getId()) &&
                entity.getYBound(entity.getId()) > entity1.getY()) {
            return true;
        }

        return false;

    }

    private List<Double> math(Entity entity, Entity entity1) {
        List<Double> dList = new ArrayList<>();
        double a = entity.getXBound(entity.getId()) - entity1.getX();
        double b = entity1.getXBound(entity1.getId()) - entity.getX();
        double c = entity.getYBound(entity.getId()) - entity1.getY();
        double d = entity1.getYBound(entity1.getId()) - entity.getY();

        dList.add(a);
        dList.add(b);
        dList.add(c);
        dList.add(d);

        return dList;

    }


    public void collisionHandle(RunGameView run, Entity entity, Entity entity1) {
        List<Double> dList;
        if (entity instanceof Tank && entity1 instanceof Tank) {
            dList = math(entity, entity1);
            if (Collections.min(dList).equals(dList.get(0))) {
                entity.updateX(entity.getX() - (dList.get(0) / 2));
                entity1.updateX(entity1.getX() + (dList.get(0) / 2));
            } else if (Collections.min(dList).equals(dList.get(1))) {
                entity.updateX(entity.getX() + (dList.get(1) / 2));
                entity1.updateX(entity1.getX() - (dList.get(1) / 2));
            } else if (Collections.min(dList).equals(dList.get(2))) {
                entity.updateY(entity.getY() - (dList.get(2) / 2));
                entity1.updateY(entity1.getY() + (dList.get(2) / 2));
            } else if (Collections.min(dList).equals(dList.get(3))) {
                entity.updateY(entity.getY() + (dList.get(3) / 2));
                entity1.updateY(entity1.getY() - (dList.get(3) / 2));
            }

        } else if (entity instanceof Tank && entity1 instanceof Shells) {
            if (entity.getId().contains("player")) {
                run.removeSprite("HP_" + entity.getHP());
                entity.removeHP(1);
                int temp = entity.getHP();
                switch (temp) {
                    case 7:
                        run.addSprite("HP_" + entity.getHP(), RunGameView.HP_7, 380, 20, 0);
                        break;
                    case 6:
                        run.addSprite("HP_" + entity.getHP(), RunGameView.HP_6, 380, 20, 0);
                        break;
                    case 5:
                        run.addSprite("HP_" + entity.getHP(), RunGameView.HP_5, 380, 20, 0);
                        break;
                    case 4:
                        run.addSprite("HP_" + entity.getHP(), RunGameView.HP_4, 380, 20, 0);
                        break;
                    case 3:
                        run.addSprite("HP_" + entity.getHP(), RunGameView.HP_3, 380, 20, 0);
                        break;
                    case 2:
                        run.addSprite("HP_" + entity.getHP(), RunGameView.HP_2, 380, 20, 0);
                        break;
                    case 1:
                        run.addSprite("HP_" + entity.getHP(), RunGameView.HP_1, 380, 20, 0);
                        break;
                    case 0:
                        playWav(RunGameView.Player_boom);
                        run.addAnimation(RunGameView.BIG_EXPLOSION_ANIMATION, RunGameView.BIG_EXPLOSION_FRAME_DELAY,
                                entity.getX(), entity.getY());
                        run.addSprite("fire", RunGameView.Fire, entity.getX(), entity.getY(), 0);
                        break;
                    default:
                        break;
                }

            }
            if (entity.getId().contains("ai")) {
                entity.removeHP(3);
                run.updateScore(25);
                if (entity.getHP() < 0) {
                    run.addAnimation(RunGameView.BIG_EXPLOSION_ANIMATION, RunGameView.BIG_EXPLOSION_FRAME_DELAY,
                            entity.getX(), entity.getY());
                    playWav(RunGameView.AI_boom);
                    run.removeSprite(entity.getId());
                    removeEntity(entity.getId());
                    run.updateScore(100);
                }
            }
            run.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION, RunGameView.SHELL_EXPLOSION_FRAME_DELAY,
                    entity1.getX(), entity1.getY());
            playWav(RunGameView.Shell_hit);
            run.removeSprite(entity1.getId());
            removeEntity(entity1.getId());
        } else if (entity instanceof Tank && entity1 instanceof Wall) {

            dList = math(entity, entity1);
            if (Collections.min(dList).equals(dList.get(0))) {
                entity.updateX(entity.getX() - dList.get(0));
            } else if (Collections.min(dList).equals(dList.get(1))) {
                entity.updateX(entity.getX() + dList.get(1));
            } else if (Collections.min(dList).equals(dList.get(2))) {
                entity.updateY(entity.getY() - dList.get(2));
            } else if (Collections.min(dList).equals(dList.get(3))) {
                entity.updateY(entity.getY() + dList.get(3));
            }

        } else if (entity instanceof Shells && entity1 instanceof Shells) {

            run.removeSprite(entity.getId());
            removeEntity(entity.getId());
            run.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION, RunGameView.SHELL_EXPLOSION_FRAME_DELAY,
                    entity.getX(), entity.getY());

            run.removeSprite(entity1.getId());
            removeEntity(entity1.getId());
            run.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION, RunGameView.SHELL_EXPLOSION_FRAME_DELAY,
                    entity1.getX(), entity1.getY());
            playWav(RunGameView.Shell_hit);

        } else if (entity instanceof Shells && entity1 instanceof Wall) {

            run.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION, RunGameView.SHELL_EXPLOSION_FRAME_DELAY,
                    entity.getX(), entity.getY());
            run.removeSprite(entity.getId());
            removeEntity(entity.getId());
            entity1.removeHP(4);
            playWav(RunGameView.Shell_hit);
            if (entity1.getHP() == 0) {
                run.removeSprite(entity1.getId());
                removeEntity(entity1.getId());
            }
        } else if (entity instanceof Tank && entity1 instanceof HP) {
            if (entity.getId().contains("player")) {
                removeEntity(entity1.getId());
                run.removeSprite("HP_" + entity.getHP());
                entity.changeHP(8);
                run.addSprite("HP_" + entity.getHP(), RunGameView.HP_8, 380, 20, 0);
                run.removeSprite(entity1.getId());
            }
        }
    }

    /**
     * Removes the entity with the specified ID from the game.
     */
    public void removeEntity(String id) {
        // TODO: Implement.
        entities.removeIf(entity -> entity.getId().equals(id));
    }

    public void resetEntities() {
        entities.clear();
    }
}
