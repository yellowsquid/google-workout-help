package uk.ac.cam.cl.alpha.workout.mobile.database;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Task<T> implements Callable<T> {
    private final Callable<T> callable;

    public Task(Callable<T> callable) {
        this.callable = callable;
    }

    public static <T> Task<T[]> group(T[] array, Task<T>... tasks) {
        return group(Arrays.stream(tasks))
                .chain(stream -> stream.collect(Collectors.toList()).toArray(array));
    }

    public <V> Task<V> chain(Function<? super T, ? extends V> function) {
        return new Task<>(() -> function.apply(callable.call()));
    }

    public static <T> Task<Stream<T>> group(Stream<? extends Task<? extends T>> tasks) {
        return new Task<>(() -> tasks.map(task -> {
            try {
                return task.call();
            } catch (Exception e) {
                throw new RuntimeException("failed to aggregate tasks", e);
            }
        }));
    }

    @Override
    public T call() throws Exception {
        return callable.call();
    }

    public <V> Task<V> chainTask(Function<? super T, ? extends Task<? extends V>> function) {
        return new Task<>(() -> function.apply(callable.call()).call());
    }
}
