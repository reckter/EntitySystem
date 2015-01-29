package me.reckter.entity;

import me.reckter.entity.entities.Entity;
import me.reckter.entity.logic.LogicHandler;

/**
 * Created by hannes on 27.01.15.
 */
public class TestMain {

    public static void main(String[] args) {
        LogicHandler logicHandler = new LogicHandler();
        logicHandler.parseLogic();

        logicHandler.tick();
    }
}
