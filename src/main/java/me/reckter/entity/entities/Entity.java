package me.reckter.entity.entities;


import me.reckter.entity.component.Component;

import java.util.*;

/**
 * Created by hannes on 20/01/15.
 */
public class Entity {

    Map<Class<? extends Component>, Component> components;


    public Entity() {

        components = new HashMap<>();
    }


    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> componentClass) throws ComponentNotFoundException {
        if(components.containsKey(componentClass)) {
            return (T) components.get(componentClass);
        }
        throw new ComponentNotFoundException("couldn't find the component '" + componentClass.getName() + "' in '" + components + "'");
    }

    public Collection<Component> getComponents() {
        return components.values();
    }

    public Collection<Class<? extends Component>> getComponentsClass() {
        return components.keySet();
    }

    public void addComponent(Component component) {
        for(Class<? extends Component> requirement: component.requirements()) {
            if(!components.containsKey(requirement)) {
                try {
                    components.put(requirement, requirement.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        components.put(component.getClass(), component);
    }

    public Set<Component> removeComponent(Component component) {
        Set<Component> ret = new HashSet<>();

        Queue<Component> toCheck = new ArrayDeque<>();
        toCheck.offer(component);
        while(!toCheck.isEmpty()) {
            Component checking = toCheck.poll();
            Set<Component> isRequiredBy = componentIsRequiredBy(component);
            isRequiredBy.removeAll(ret);

            isRequiredBy.forEach(toCheck::offer);

            ret.add(checking);

        }

        ret.forEach(components::remove);

        return ret;
    }

    private Set<Component> componentIsRequiredBy(Component component) {
        Set<Component> ret = new HashSet<>();
        for(Component componentTmp: components.values()) {
            Set<Class<? extends Component>> requirements = componentTmp.requirements();
            if(requirements.contains(component)) {
                ret.add(componentTmp);
            }
        }
        return ret;
    }

    public boolean hasComponent(Class<? extends Component> clazz) {
        return components.containsKey(clazz);
    }

    public boolean hasComponents(List<Class<? extends Component>> clazzes) {
        for(Class<? extends Component> clazz: clazzes) {
            if(!hasComponent(clazz)) {
                return false;
            }
        }
        return true;
    }

    @SafeVarargs
    public final boolean hasComponents(Class<? extends Component>... clazzes) {
        return hasComponents(Arrays.asList(clazzes));
    }

}
