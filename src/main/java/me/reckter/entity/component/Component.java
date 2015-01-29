package me.reckter.entity.component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by hannes on 21.01.15.
 */
public abstract class Component {

    public Set<Class<? extends Component>> requirements() {
        return new HashSet<>();
    }
}
