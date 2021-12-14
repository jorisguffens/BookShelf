package com.gufli.bookshelf.api.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {

    String[] commands();

    boolean playerOnly() default false;

    String[] permissions() default {};

    int minArguments() default 0;

    String argumentsHint() default "";

}
