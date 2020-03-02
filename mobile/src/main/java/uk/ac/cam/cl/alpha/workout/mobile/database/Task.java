package uk.ac.cam.cl.alpha.workout.mobile.database;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Task<T> implements Callable<T> {
    private final Supplier<? extends T> supplier;

    Task(Supplier<? extends T> supplier) {
        this.supplier = supplier;
    }

    Task(Runnable runnable) {
        supplier = () -> {
            runnable.run();
            return null;
        };
    }

    @Override
    public T call() {
        return supplier.get();
    }

    public Runnable andThen(Consumer<? super T> consumer) {
        return () -> consumer.accept(supplier.get());
    }
}
