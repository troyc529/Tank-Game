package edu.csc413.tankgame;

import edu.csc413.tankgame.model.*;
import edu.csc413.tankgame.view.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class GameDriver {
    private final MainView mainView;
    private final RunGameView runGameView;
    private final GameWorld world;
    static int count;
    static int iteration;

    public GameDriver() {
        mainView = new MainView(this::startMenuActionPerformed);
        runGameView = mainView.getRunGameView();
        world = new GameWorld();
    }

    public void start() {
        mainView.setScreen(MainView.Screen.START_GAME_SCREEN);
    }

    private void startMenuActionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {
            case StartMenuView.START_BUTTON_ACTION_COMMAND -> runGame();
            case StartMenuView.EXIT_BUTTON_ACTION_COMMAND -> mainView.closeGame();
            default -> throw new RuntimeException("Unexpected action command: " + actionEvent.getActionCommand());
        }
    }

    private void runGame() {
        mainView.setScreen(MainView.Screen.RUN_GAME_SCREEN);
        Runnable gameRunner = () -> {
            setUpGame();
            while (updateGame()) {
                runGameView.repaint();
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException exception) {
                    throw new RuntimeException(exception);
                }
            }

            mainView.setScreen(MainView.Screen.END_MENU_SCREEN);
            resetGame();
        };
        new Thread(gameRunner).start();
    }

    /**
     * setUpGame is called once at the beginning when the game is started. Entities that are present from the start
     * should be initialized here, with their corresponding sprites added to the RunGameView.
     */
    private void setUpGame() {
        // TODO: Implement.
        world.startgame();
        runGameView.add(runGameView.getLabel());
        runGameView.getLabel().setText("SCORE: " + runGameView.getScore());
        count = 35;
        iteration = 0;
        Tank playerTank = new Player(Constants.PLAYER_TANK_ID,
                Constants.PLAYER_TANK_INITIAL_X,
                Constants.PLAYER_TANK_INITIAL_Y,
                Constants.PLAYER_TANK_INITIAL_ANGLE);
        Tank aiTank = new AI(Constants.AI_TANK_1_ID,
                Constants.AI_TANK_1_INITIAL_X,
                Constants.AI_TANK_1_INITIAL_Y,
                0);
        world.addEntity(aiTank);
        world.addEntity(playerTank);
        runGameView.addSprite(playerTank.getId(),
                RunGameView.PLAYER_TANK_IMAGE_FILE,
                playerTank.getX(),
                playerTank.getY(),
                playerTank.getAngle());
        runGameView.addSprite(aiTank.getId(),
                RunGameView.AI_TANK_IMAGE_FILE,
                aiTank.getX(),
                aiTank.getY(),
                aiTank.getAngle());

        runGameView.addSprite("HP_8", RunGameView.HP_8, 380, 20, 0);

        List<WallInformation> wallInfo = WallInformation.readWalls();
        for (WallInformation wall : wallInfo) {
            Wall wallpart = new Wall(wall);
            world.addEntity(wallpart);
            runGameView.addSprite(wallpart.getId(), wall.getImageFile(), wallpart.getX(), wallpart.getY(), wallpart.getAngle());
        }

    }

    /**
     * updateGame is repeatedly called in the gameplay loop. The code in this method should run a single frame of the
     * game. As long as it returns true, the game will continue running. If the game should stop for whatever reason
     * (e.g. the player tank being destroyed, escape being pressed), it should return false.
     */
    private void hang(int mili) {
        try {
            Thread.sleep(mili);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }


    private void makeAI(String id, double x, double y) {
        Tank aiTank2 = new AI(id,
                x,
                y,
                Math.toRadians(90));
        world.addEntity(aiTank2);
        runGameView.addSprite(aiTank2.getId(),
                RunGameView.AI_TANK_IMAGE_FILE,
                aiTank2.getX(),
                aiTank2.getY(),
                aiTank2.getAngle());
    }

    private boolean updateGame() {
        iteration += 1;
        if (iteration == 500) {
            if (world.getEntity("hp") == null) {
                HP hp = new HP("hp", 75, 75, 0);
                runGameView.addSprite("hp", RunGameView.HP, hp.getX(), hp.getY(), 0);
                world.addEntity(hp);
            }
            makeAI(Constants.AI_TANK_2_ID, Constants.AI_TANK_1_INITIAL_Y, Constants.AI_TANK_1_INITIAL_X);
        }
        if (iteration == 1500) {
            makeAI(Constants.AI_TANK_1_ID, Constants.AI_TANK_1_INITIAL_X, Constants.PLAYER_TANK_INITIAL_Y);
        }
        if (iteration == 2500) {
            if (world.getEntity("hp") == null) {
                HP hp = new HP("hp", 850, 650, 0);
                runGameView.addSprite("hp", RunGameView.HP, hp.getX(), hp.getY(), 0);
                world.addEntity(hp);
            }

            makeAI(Constants.AI_TANK_2_ID, Constants.PLAYER_TANK_INITIAL_X, Constants.PLAYER_TANK_INITIAL_Y);
            iteration = 0;
        }
        List<Entity> temp = new ArrayList<>(world.getEntities());

        for (Entity entity: temp) {
            if (entity.getId().contains("player") && entity.getHP() <= 0) {
                count -= 1;
                if (count == 0) {
                    runGameView.addSprite("HP_0", RunGameView.HP_0, 410, 260, 0);
                } else if (count < -2) {
                    runGameView.getLabel().setText("FINAL SCORE: " + runGameView.getScore());
                    Dimension size = runGameView.getLabel().getPreferredSize();
                    runGameView.getLabel().setBounds(420, 235, size.width, size.height);
                    hang(5000);

                    return false;
                }

            }

            if (entity.getId().contains("shell")) {
                entity.changeID();
                runGameView.addSprite(entity.getId(),
                        RunGameView.SHELL_IMAGE_FILE,
                        entity.getX(),
                        entity.getY(),
                        entity.getAngle());
            }
            entity.move(world);

            runGameView.setSpriteLocationAndAngle(entity.getId(), entity.getX(), entity.getY(), entity.getAngle());
            if (entity.getId().contains("boom")) {
                if (entity.getX() < Constants.SHELL_X_LOWER_BOUND ||
                        entity.getX() > Constants.SHELL_X_UPPER_BOUND ||
                        entity.getY() < Constants.SHELL_Y_LOWER_BOUND ||
                        entity.getY() > Constants.SHELL_Y_UPPER_BOUND) {
                    world.removeEntity(entity.getId());
                    runGameView.removeSprite(entity.getId());
                }
            }

        }


        for (Entity entity : temp) {
            for (Entity entity1 : temp) {
                if (entity.equals(entity1))
                    continue;
                else {
                    if (world.collisionDet(entity, entity1)) {
                        world.collisionHandle(runGameView, entity, entity1);
                    }
                }

            }

        }
        return world.getendgame();
    }

    /**
     * resetGame is called at the end of the game once the gameplay loop exits. This should clear any existing data from
     * the game so that if the game is restarted, there aren't any things leftover from the previous run.
     */
    private void resetGame() {
        // TODO: Implement.
        runGameView.reset();
        world.resetEntities();
        updateGame();
    }

    public static void main(String[] args) {
        GameDriver gameDriver = new GameDriver();
        gameDriver.start();
    }
}
