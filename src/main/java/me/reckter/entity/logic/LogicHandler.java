package me.reckter.entity.logic;




import me.reckter.entity.component.Component;
import me.reckter.entity.entities.DrawableEntity;
import me.reckter.entity.entities.Entity;
import me.reckter.entity.events.*;
import me.reckter.entity.matcher.Matcher;
import me.reckter.entity.util.UpdateList;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by hannes on 20/01/15.
 *
 */
@Logic
public class LogicHandler<T>  {

    @SuppressWarnings("FieldCanBeLocal")
    private static LogicHandler instance;


    /**
     * the event handler's must be sorted by priority at all Times
     */
    Map<Class<? extends Event>, List<EventHandler>> eventHandlers;

    Map<EventHandler, List<Entity>> methodEntities;

    Queue<Event> eventQueue;

    UpdateList<Entity> entites;
    UpdateList<DrawableEntity<T>> drawableEntities;

    public LogicHandler() {
        eventHandlers = new HashMap<>();

        methodEntities = new HashMap<>();

        eventQueue = new ArrayDeque<>(1000);

        entites = new UpdateList<>();
        drawableEntities = new UpdateList<>();
    }


    /**
     * parses all Logics found in the classpath and initializes all EventHandlers
     * @throws IllegalAccessException
     */
    public void parseLogic() {

        LogicHandler.instance = this;

        Reflections reflections = new Reflections("", new SubTypesScanner(), new MethodAnnotationsScanner());

        Set<Class<? extends Event>> events = reflections.getSubTypesOf(Event.class);
        for(Class<? extends Event> event: events) {
            eventHandlers.put(event, new ArrayList<>());
        }


        Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(OnEvent.class);

        for(Method method : methodsAnnotatedWith) {

            Class clazz = method.getDeclaringClass();

            if (clazz.isAnnotationPresent(Logic.class)) {

                Object instance = null;
                try {
                    try {
                        Field instanceField = clazz.getDeclaredField("instance");
                        instance = instanceField.get(null);
                    } catch (NoSuchFieldException ignored) {
                    } catch (IllegalAccessException e) {
                        throw new LogicReflectionException(e);
                    }

                    if (instance == null) {
                        try {
                            instance = clazz.newInstance();
                        } catch (IllegalAccessException e) {
                            throw new LogicReflectionException(e);
                        }
                    }
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }


                // inject ourselfs into the logic if needed

                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (field.getType().isAssignableFrom(this.getClass())) {
                        field.setAccessible(true);
                        try {
                            field.set(instance, this);
                        } catch (IllegalAccessException e) {
                            throw new LogicReflectionException(e);
                        }
                    }
                }

                OnEvent onEvent = method.getAnnotation(OnEvent.class);

                // who needs privacy anyway? /s   also speed up!
                method.setAccessible(true);

                Class<?>[] params = method.getParameterTypes();
                if(params.length == 0 || !Event.class.isAssignableFrom(params[0])) {
                    throw new LogicReflectionException("The first parameter of the method '" + method.getName() + "' in '" + method.getDeclaringClass().getName() + "', must be an Event!");
                }
                if(params.length > 1) {
                    throw new LogicReflectionException("The method '" + method.getName() + "' in '" + method.getDeclaringClass().getName() + "', must have exactly one Parameter!");
                }

                //noinspection unchecked
                Class<? extends Event> event = (Class<? extends Event>) params[0];

                EventHandler eventHandler;

                if(method.isAnnotationPresent(Entities.class)) {
                    Entities entities = method.getAnnotation(Entities.class);
                    if(!EntityEvent.class.isAssignableFrom(event)) {
                        throw new LogicReflectionException("The method '" + method.getName() + "' in '" + method.getDeclaringClass().getName() + "', has a Entity annotation, but take a non Entity Event!");
                    }
                    eventHandler = new EntityEventHandler(onEvent.value(), method, instance, new Matcher(entities.value(), entities.exclusion()));
                } else {
                    eventHandler = new EventHandler(onEvent.value(), method, instance);
                }


                eventHandlers.get(event).add(eventHandler);
            }
        }

        eventHandlers.values().forEach(Collections::sort);
    }


    /**
     * handles an Event.
     * Only calls the EventHandler, that want to listen to that event
     * @param event the event to handle
     */
    public void handleEvent(Event event) {
        for(EventHandler eventHandler : this.eventHandlers.get(event.getClass())) {
            if(eventHandler.takes(event)) {
                try {
                    eventHandler.method.invoke(eventHandler.instance, event);
                } catch (IllegalAccessException throwable) {
                    throwable.printStackTrace();
                } catch (IllegalArgumentException throwable) {
                    throwable.printStackTrace();
                } catch (InvocationTargetException throwable) {
                    throwable.printStackTrace();

                    throw new RuntimeException(throwable);
                }
            }
        }
    }

    /**
     * handles all events, that are in the queue right now
     */
    public void handleEvents() {
        while (!eventQueue.isEmpty()) {

            Event event = eventQueue.poll();
            handleEvent(event);
        }
    }

    /**
     * makes one logic tick
     * fires a BeginTickEvent at the start
     * and at the end a EndTickEvent
     *
     */
    public void tick() {

        handleEvent(new BeginTickEvent());
        handleEvents();
        handleEvent(new EndTickEvent());
        handleEvents();

        entites.update();
        drawableEntities.update();
    }



    public void render(T graphics) {
        for (DrawableEntity<T> drawableEntity : drawableEntities) {
            drawableEntity.render(graphics);
        }
    }


    public List<Entity> getEntities(List<Class<? extends Component>> components) {
        return entites.stream().filter(s -> s.hasComponents(components)).collect(Collectors.toList());
    }



    @SafeVarargs
    public final List<Entity> getEntities(Class<? extends Component>... components) {
        return getEntities(Arrays.asList(components));
    }

    public List<DrawableEntity<T>> getDrawableEntities() {
        return drawableEntities;
    }


    /**
     * adds an Event to the event queue
     * @param event the event to fire
     */
    public void fire(Event event) {
        eventQueue.offer(event);
    }

    /**
     * fires an EntitiyAddEVent
     * @param entity the entity to add
     */
    public void add(Entity entity) {
        fire(new EntityAddEvent(entity, entity));
    }

    /**
     * fires an EntityDelEvent
     * @param entity the entity to remove
     */
    public void remove(Entity entity) {
        fire(new EntityDelEvent(entity, entity));
    }

    @OnEvent(-1000)
    @Entities()
    public void onEntityAdd(EntityAddEvent event) {
        entites.add(event.target);
        if(event.target instanceof DrawableEntity) {
            //noinspection unchecked
            drawableEntities.add((DrawableEntity<T>) event.target);
        }
    }


    @OnEvent(-1000)
    @Entities()
    public void onEntityDel(EntityDelEvent event) {
        entites.remove(event.target);
        drawableEntities.remove(event.target);
    }


    /**
     * an EventHandler, that needs Entites
     */
    private class EntityEventHandler extends EventHandler {
        Matcher matcher;
        public EntityEventHandler(int priority, Method method, Object instance, Matcher matcher) {
            super(priority, method, instance);
            this.matcher = matcher;
        }

        public boolean takes(Event event) {
            return event instanceof EntityEvent && matcher.matches(((EntityEvent) event).target);
        }
    }

    /**
     * an EventHandler, that just listens to events
     */
    private class EventHandler implements Comparable<EventHandler>{

        int priority;
        Method method;
        Object instance;

        public EventHandler(int priority, Method method, Object instance) {
            this.priority = priority;
            this.method = method;
            this.instance = instance;
        }

        @Override
        public int compareTo(EventHandler o) {
            return Integer.compare(o.priority, priority);
        }


        public boolean takes(Event event) {
            return true;
        }

    }



}
