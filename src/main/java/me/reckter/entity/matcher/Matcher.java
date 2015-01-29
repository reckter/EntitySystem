package me.reckter.entity.matcher;


import me.reckter.entity.component.Component;
import me.reckter.entity.entities.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hannes on 20/01/15.
 */
public class Matcher {

    List<Class<? extends Component>> requierements;
    List<Class<? extends Component>> exclusions;

    public Matcher(List<Class<? extends Component>> requierements, List<Class<? extends Component>> exclusions) {
        this.requierements = requierements;
        this.exclusions = exclusions;
    }


    public Matcher(Class<? extends Component>[] requierements, Class<? extends Component>[] exclusions) {
        this.requierements = new ArrayList<>();
        this.exclusions = new ArrayList<>();

        Collections.addAll(this.requierements, requierements);
        Collections.addAll(this.exclusions, exclusions);

    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean matches(Entity entity) {

        Collection<Class<? extends Component>> components = entity.getComponentsClass();
        if(!components.containsAll(requierements)) {
            return false;
        }

        for(Class<? extends Component> exclusion: exclusions) {
            if(components.contains(exclusion)) {
                return false;
            }
        }

        return true;

    }

    public List<Entity> getMatching(List<Entity> entities) {
        return entities.stream().filter(this::matches).collect(Collectors.toList());
    }
}
