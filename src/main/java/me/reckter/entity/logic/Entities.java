package me.reckter.entity.logic;


import me.reckter.entity.component.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by hannes on 09/01/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Entities {
    public Class<? extends Component>[] value() default {};
    public Class<? extends Component>[] exclusion() default {};
}
