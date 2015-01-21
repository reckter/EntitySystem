package me.reckter.entity;

import me.reckter.entity.entities.Entity;
import me.reckter.entity.events.EntityAddEvent;
import me.reckter.entity.events.EntityDelEvent;
import me.reckter.entity.events.Event;
import me.reckter.entity.events.TickEvent;
import me.reckter.entity.logic.LogicHandler;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hannes on 20/01/15.
 */
public class TestMain {



    public static void main(String[] args) throws IllegalAccessException {
        LogicHandler logicHandler = new LogicHandler();
        logicHandler.parseLogic();

        logicHandler.handleEvent(new EntityAddEvent(new Entity(), new Entity()));
        logicHandler.handleEvent(new EntityDelEvent(new Entity(), new Entity()));
    }



    public static long startTime;
    public static int tests = 1_000;


    public static List<Long> timeCollision = new ArrayList<>(tests);
    public static List<Long> timeFreez = new ArrayList<>(tests);
    public static List<Long> timeMove = new ArrayList<>(tests);
    public static List<Long> timeAfter = new ArrayList<>(tests);


    /*

    public static void main(String[] args) throws IllegalAccessException {
        LogicHandler logicHandler = new LogicHandler();
        logicHandler.parseLogic();


        for (int k = 0; k < 1000; k++) {
            timeCollision.clear();
            timeFreez.clear();
            timeMove.clear();
            timeAfter.clear();

            System.out.println("direct: ");


            ExampleLogic exampleLogic = new ExampleLogic();
            long startTimeAll = System.nanoTime();
            for (int i = 0; i < tests; i++) {
                TickEvent event = new TickEvent();
                startTime = System.nanoTime();
                long time;
                exampleLogic.collisionChecking(event);
                exampleLogic.freez(event);
                exampleLogic.move(event);
                time = System.nanoTime() - TestMain.startTime;
                timeAfter.add(time);
            }
            long allTime = System.nanoTime() - startTimeAll;

            System.out.println("avrg collision: " + timeCollision.stream().collect(Collectors.averagingLong(s -> (long) s)) + "(" + timeCollision.get(timeCollision.size() - 1) + ")");
            System.out.println("avrg freez: " + timeFreez.stream().collect(Collectors.averagingLong(s -> (long) s)) + "(" + timeFreez.get(timeFreez.size() - 1) + ")");
            System.out.println("avrg move: " + timeMove.stream().collect(Collectors.averagingLong(s -> (long) s)) + "(" + timeMove.get(timeMove.size() - 1) + ")");
            System.out.println("avrg after: " + timeAfter.stream().collect(Collectors.averagingLong(s -> (long) s)) + "(" + timeAfter.get(timeAfter.size() - 1) + ")");
            System.out.println("all: " + allTime);

            timeCollision.clear();
            timeFreez.clear();
            timeMove.clear();
            timeAfter.clear();

            System.out.println("System: ");

            startTimeAll = System.nanoTime();
            for (int i = 0; i < tests; i++) {
                Event event = new TickEvent();
                startTime = System.nanoTime();
                long time;
                logicHandler.handleEvent(event);
                time = System.nanoTime() - TestMain.startTime;
                timeAfter.add(time);
            }

            allTime = System.nanoTime() - startTimeAll;

            System.out.println("avrg collision: " + timeCollision.stream().collect(Collectors.averagingLong(s -> (long) s)) + "(" + timeCollision.get(timeCollision.size() - 1) + ")");
            System.out.println("avrg freez: " + timeFreez.stream().collect(Collectors.averagingLong(s -> (long) s)) + "(" + timeFreez.get(timeFreez.size() - 1) + ")");
            System.out.println("avrg move: " + timeMove.stream().collect(Collectors.averagingLong(s -> (long) s)) + "(" + timeMove.get(timeMove.size() - 1) + ")");
            System.out.println("avrg after: " + timeAfter.stream().collect(Collectors.averagingLong(s -> (long) s)) + "(" + timeAfter.get(timeAfter.size() - 1) + ")");
            System.out.println("all: " + allTime);
        }
    }

    */
}
