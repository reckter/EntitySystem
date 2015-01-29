package me.reckter.entity.events;

import me.reckter.entity.entities.Entity;

/**
 * Created by hannes on 29.01.15.
 */
public class CollisionEvent extends EntityEvent {
    public CollisionEvent(Entity entityB, Entity entityA) {
        super(entityA, entityB);
    }
}
