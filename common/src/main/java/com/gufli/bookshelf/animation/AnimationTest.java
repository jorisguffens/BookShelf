package com.gufli.bookshelf.animation;

import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;

public class AnimationTest {

    public static void test() {
        AnimationBuilder.get()
                .repeatUntil(new Supplier<Boolean>() {
                    int counter = 0;
                    @Override
                    public Boolean get() {
                        System.out.println(counter + "");
                        counter++;
                        return counter >= 10;
                    }
                }, 1, ChronoUnit.SECONDS)
                .execute(() -> System.out.println("oh hello there"))
                .build().start();
    }

}
