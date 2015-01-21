package me.reckter.entity.events;

import me.reckter.entity.entities.Entity;

import java.util.List;

/**
 * Created by hannes on 21.01.15.
 */
abstract public class AllEntityEvent extends Event{

    List<Entity> entities;

    public AllEntityEvent() {
        super(null, null);
    }
}
