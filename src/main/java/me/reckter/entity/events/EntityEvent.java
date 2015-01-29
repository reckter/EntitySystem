package me.reckter.entity.events;

import me.reckter.entity.entities.Entity;

/**
 * Created by hannes on 26.01.15.
 */
public class EntityEvent extends Event {

    public Entity source;
    public Entity target;

    public EntityEvent(Entity offender, Entity victim) {
        source = offender;
        this.target = victim;
    }
}
