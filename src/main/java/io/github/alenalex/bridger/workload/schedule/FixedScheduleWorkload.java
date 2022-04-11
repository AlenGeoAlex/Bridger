package io.github.alenalex.bridger.workload.schedule;

import java.util.concurrent.atomic.AtomicLong;

/*
 * {@author Abhigaya Krishna}
 * https://github.com/AbhigyaKrishna/CoreAPI/tree/main/src/main/java/me/Abhigya/core/util/tasks
 */

/**
 * An abstract implementation for {@link ConditionalScheduleWorkload} and, computes the workload the
 * given number times.
 */
public abstract class FixedScheduleWorkload extends ConditionalScheduleWorkload<AtomicLong> {

    /**
     * Constructs the class.
     *
     * <p>
     *
     * @param numberOfExecutions Number of executions
     */
    protected FixedScheduleWorkload(final AtomicLong numberOfExecutions) {
        super(numberOfExecutions);
    }

    /**
     * Constructs the class.
     *
     * <p>
     *
     * @param numberOfExecutions Number of executions
     */
    protected FixedScheduleWorkload(final long numberOfExecutions) {
        this(new AtomicLong(numberOfExecutions));
    }

    @Override
    public final boolean test(final AtomicLong atomicInteger) {
        return atomicInteger.decrementAndGet() > 0L;
    }
}
