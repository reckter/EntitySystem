package me.reckter.entity.events;

import me.reckter.entity.entities.Entity;

/**
 * Created by hannes on 20/01/15.
 */
public class TickEvent extends Event {


    public TickEvent(Entity victim) {
        super(victim, victim);
    }
}
