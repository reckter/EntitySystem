package me.reckter.entity.logic;

import me.reckter.entity.entities.Entity;
import me.reckter.entity.events.EntityAddEvent;
import me.reckter.entity.events.EntityDelEvent;
import me.reckter.entity.events.Event;
import me.reckter.entity.matcher.Matcher;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by hannes on 20/01/15.
 *
 */
@Logic
public class LogicHandler {

    @SuppressWarnings("FieldCanBeLocal")
    private static LogicHandler instance;

    Map<Class<? extends Event>, List<PriorityMethod>> eventHandler;
    Map<PriorityMethod, Matcher> matcher;

    Map<PriorityMethod, List<Entity>> entities;


    Queue<Event> eventQueue;


    public LogicHandler() {
        eventHandler = new HashMap<>();
        matcher = new HashMap<>();

        entities = new HashMap<>();

        eventQueue = new ArrayDeque<>(1000);

    }


    public void parseLogic() throws IllegalAccessException {

        LogicHandler.instance = this;

        Reflections reflections = new Reflections("", new SubTypesScanner(), new MethodAnnotationsScanner());

        Set<Class<? extends Event>> events = reflections.getSubTypesOf(Event.class);
        for(Class<? extends Event> event: events) {
            eventHandler.put(event, new ArrayList<>());
        }





        Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(On.class);

        for(Method method : methodsAnnotatedWith) {

            Class clazz = method.getDeclaringClass();

            if (clazz.isAnnotationPresent(Logic.class)) {
                Object instance = null;
                try {
                    try {
                        Field instanceField = clazz.getDeclaredField("instance");
                        instance = instanceField.get(null);
                    } catch (NoSuchFieldException ignored) {
                    }

                    if (instance == null) {
                        instance = clazz.newInstance();
                    }
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }

                On on = method.getAnnotation(On.class);
                Entities entities = method.getAnnotation(Entities.class);

                // who needs privacy anyway? /s   also speed up!
                method.setAccessible(true);

                PriorityMethod priorityMethod = new PriorityMethod(on.priority(), method, instance);

                matcher.put(priorityMethod, new Matcher(entities.value(), entities.exclusion()));

                eventHandler.get(on.value()).add(priorityMethod);
            }
        }

        eventHandler.values().forEach(Collections::sort);


    }

    public void handleEvent(Event event) {
        for(PriorityMethod priorityMethod : eventHandler.get(event.getClass())) {
            try {
                priorityMethod.method.invoke(priorityMethod.instance, event);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public void handleEvents() {
        while (!eventQueue.isEmpty()) {
            Event event = eventQueue.poll();
            handleEvent(event);
        }
    }

    public void fire(Event event) {
        eventQueue.offer(event);
    }



    private class PriorityMethod implements Comparable<PriorityMethod>{
        int priority;
        Method method;
        Object instance;

        public PriorityMethod(int priority, Method method, Object instance) {
            this.priority = priority;
            this.method = method;
            this.instance = instance;
        }

        @Override
        public int compareTo(PriorityMethod o) {
            return Integer.compare(priority, o.priority);
        }

    }


    @On(value = EntityAddEvent.class, priority = -1000)
    @Entities()
    public void onEntityAdd(EntityAddEvent event) {
        System.out.println("add: ");
        for(List<PriorityMethod> priorityMethods: eventHandler.values()) {
            for(PriorityMethod priorityMethod: priorityMethods) {
                System.out.println(priorityMethod.method.getName());
            }
        }
    }


    @On(value = EntityDelEvent.class, priority = -1000)
    @Entities()
    public void onEntityDel(EntityDelEvent event) {
        System.out.println("del: ");
        for(List<PriorityMethod> priorityMethods: eventHandler.values()) {
            for(PriorityMethod priorityMethod: priorityMethods) {
                System.out.println(priorityMethod.method.getName());
            }
        }
    }



}
