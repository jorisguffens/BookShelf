package com.gufli.bookshelf.events;

public @interface Event {

    EventPriority priority() default EventPriority.NORMAL;

}