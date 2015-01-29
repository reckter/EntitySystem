package me.reckter.entity.component;

import me.reckter.entity.entities.Entity;

import java.util.Set;

/**
 * Created by hannes on 29.01.15.
 */
public class Collision extends Component {

    public String group;

    public Collision(String group) {
        this.group = group;
    }

    @Override
    public Set<Class<? extends Component>> requirements() {
        Set<Class<? extends Component>> ret = super.requirements();
        ret.add(Position.class);
        return ret;
    }

    public boolean collidesWith(Entity entityB) {
        //TODO
        return false;
    }
}
