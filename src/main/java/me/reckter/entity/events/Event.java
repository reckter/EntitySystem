package me.reckter.entity.events;

import me.reckter.entity.entities.Entity;

/**
 * Created by hannes on 09/01/15.
 */
public abstract class Event {

    public Entity Offender;
    public Entity victim;

    public Event(Entity offender, Entity victim) {
        Offender = offender;
        this.victim = victim;
    }
}
