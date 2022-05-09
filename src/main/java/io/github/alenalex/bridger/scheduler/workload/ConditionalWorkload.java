package io.github.alenalex.bridger.scheduler.workload;

import io.github.alenalex.bridger.scheduler.core.Workload;

import java.util.function.Predicate;

/*
 * {@author Abhigaya Krishna}
 * https://github.com/AbhigyaKrishna/CoreAPI/tree/main/src/main/java/me/Abhigya/core/util/tasks
 */

public abstract class ConditionalWorkload<T> implements Predicate<T>, Workload {

    private final T element;

    public ConditionalWorkload(T element) {
        this.element = element;
    }

    @Override
    public boolean shouldExecute() {
        return this.test(element);
    }

    public T getElement() {
        return element;
    }
}