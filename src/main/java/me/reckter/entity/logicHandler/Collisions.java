package me.reckter.entity.logicHandler;

import me.reckter.entity.component.Collision;
import me.reckter.entity.entities.Entity;
import me.reckter.entity.events.CollisionEvent;
import me.reckter.entity.events.EndTickEvent;
import me.reckter.entity.events.EntityAddEvent;
import me.reckter.entity.events.EntityDelEvent;
import me.reckter.entity.logic.Entities;
import me.reckter.entity.logic.Logic;
import me.reckter.entity.logic.LogicHandler;
import me.reckter.entity.logic.OnEvent;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by hannes on 28.01.15.
 */
@Logic
public class Collisions {


    private LogicHandler logicHandler;

    public static Collisions INSTANCE = new Collisions();

    public static void newInstance() {
        INSTANCE = new Collisions();
    }

    private Map<String, List<Entity>> collisionGroups;

    private Map<String, Set<String>> collisionRules;

    private Collisions() {
        collisionGroups = new HashMap<>();
        collisionRules = new HashMap<>();
    }


    public void addCollisionGroup(String group) {
        collisionGroups.put(group, new ArrayList<>());
        collisionRules.put(group, new HashSet<>());
    }

    public void delCollisionGroup(String group) {
        collisionGroups.remove(group);
    }


    public void addCollisionRule(String collider, String with) {
        collisionRules.get(collider).add(with);
    }

    public void addCollisionBothWays(String a, String b) {
        addCollisionRule(a, b);
        addCollisionRule(b, a);
    }

    public void removeCollisionRule(String collider, String with) {
        collisionRules.get(collider).remove(with);
    }

    public void removeCollisionBothWays(String a, String b) {
        removeCollisionRule(a, b);
        removeCollisionRule(b, a);
    }


    @OnEvent
    @Entities(Collision.class)
    public void addEntity(EntityAddEvent event) {
        Collision collision = event.target.getComponent(Collision.class);
        collisionGroups.get(collision.group).add(event.target);

    }


    @Entities(Collision.class)
    public void delEntitiy(EntityDelEvent event) {
        Collision collision = event.target.getComponent(Collision.class);
        collisionGroups.get(collision.group).remove(event.target);

    }


    @OnEvent(-1000)
    public void checkCollisions(EndTickEvent event) {
        collisionRules.keySet().parallelStream().forEach(groupA -> {
                    Set<Entity> collisionPartners = collisionRules.get(groupA).parallelStream()
                            .map(collisionGroups::get)
                            .collect(HashSet::new, AbstractCollection::addAll, AbstractCollection::addAll);
                    collisionGroups.get(groupA).parallelStream().forEach(entityA -> {
                        collisionPartners.parallelStream()
                                .filter(entityB -> entityA.getComponent(Collision.class).collidesWith(entityB))
                                .forEach(entityB -> {
                                    logicHandler.fire(new CollisionEvent(entityB, entityA));
                                });
                    });
                }
        );
    }

}
