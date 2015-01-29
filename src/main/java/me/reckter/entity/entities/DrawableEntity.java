package me.reckter.entity.entities;

/**
 * Created by hannes on 26.01.15.
 */
public abstract class DrawableEntity<T> extends Entity{


    abstract public void render(T g);
}
