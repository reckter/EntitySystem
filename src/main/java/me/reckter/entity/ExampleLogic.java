package me.reckter.entity;

import me.reckter.entity.component.Component;
import me.reckter.entity.component.Movement;
import me.reckter.entity.events.AllEntityTickEvent;
import me.reckter.entity.events.Event;
import me.reckter.entity.events.TickEvent;
import me.reckter.entity.logic.Entities;
import me.reckter.entity.logic.Logic;
import me.reckter.entity.logic.On;


/**
 * Created by hannes on 09/01/15.
 */
@Logic
public final class ExampleLogic {


    @On(value = TickEvent.class, priority = 10)
    @Entities(Movement.class)
    public void move(TickEvent event) {
        long time = System.nanoTime() - TestMain.startTime;
        TestMain.timeMove.add(time);
    }


    @On(TickEvent.class)
    @Entities()
    public void freez(TickEvent event) {
        long time = System.nanoTime() - TestMain.startTime;
        TestMain.timeFreez.add(time);

    }

    @On(value = TickEvent.class, priority = -10)
    @Entities()
    public void collisionChecking(TickEvent event) {
        long time = System.nanoTime() - TestMain.startTime;
        TestMain.timeCollision.add(time);

    }
}
