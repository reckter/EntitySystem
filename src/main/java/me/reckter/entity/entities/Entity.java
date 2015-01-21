package me.reckter.entity.entities;

import me.reckter.entity.component.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hannes on 20/01/15.
 */
public class Entity {


    List<Component> components;

    public Entity() {
        components = new ArrayList<>();
    }


    public <T extends Component> T getComponent(Class<T> componentClass) throws ComponentNotFoundException {
        for(Component component: components) {
            if(component.getClass().isInstance(componentClass)) {
                //noinspection unchecked
                return (T) component;
            }
        }
        throw new ComponentNotFoundException("couldn't find the component '" + componentClass.getName() + "' in '" + components + "'");
    }

    public List<Component> getComponents() {
        return components;
    }
}
