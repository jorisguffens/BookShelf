package com.gufli.bookshelf.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {

    String[] commands();

    boolean playerOnly() default false;

    String[] permissions();

    int argumentsLength() default -1;

    String argumentsHint() default "";

}
