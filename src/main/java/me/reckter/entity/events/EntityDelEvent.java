package me.reckter.entity.events;

import me.reckter.entity.entities.Entity;

/**
 * Created by hannes on 21.01.15.
 */
public class EntityDelEvent extends Event {
    public EntityDelEvent(Entity offender, Entity victim) {
        super(offender, victim);
    }
}
