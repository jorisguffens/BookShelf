package com.gufli.bookshelf.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {

    String[] commands() default {};

    boolean playerOnly() default false;

    String[] permissions() default {};

    int argumentsLength() default -1;

    String argumentsHint() default "";

}
