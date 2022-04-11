package io.github.alenalex.bridger.workload.core;
/*
The entire package has been forked from the original project: CoreAPI
https://github.com/AbhigyaKrishna/CoreAPI/tree/main/src/main/java/me/Abhigya/core/util/tasks
 */

import java.util.Collection;
import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class WorkloadThread implements Runnable {

    /** The work deque. */
    private final Queue<Workload> deque = new ConcurrentLinkedQueue<>();

    /** The maximum nano per tick. */
    private final long maxNanosPerTick;

    /** The work thread id. */
    private final long workThreadId;


    public WorkloadThread(final long workThreadId, final long maxNanosPerTick) {
        this.workThreadId = workThreadId;
        this.maxNanosPerTick = maxNanosPerTick;
    }


    public void add(Workload workload) {
        deque.add(workload);
    }


    public Collection<Workload> getDeque() {
        return Collections.unmodifiableCollection(deque);
    }


    public long getMaxNanosPerTick() {
        return maxNanosPerTick;
    }


    public long getWorkThreadId() {
        return workThreadId;
    }

    @Override
    public void run() {
        final long stopTime = System.nanoTime() + this.maxNanosPerTick;
        final Workload first = this.deque.poll();
        if (first == null) {
            return;
        }
        this.computeWorkload(first);
        Workload workload;
        while (System.nanoTime() <= stopTime && (workload = this.deque.poll()) != null) {
            this.computeWorkload(workload);
            if (!first.reSchedule() && first.equals(workload)) {
                break;
            }
        }
    }


    private void computeWorkload(final Workload workload) {
        if (workload.shouldExecute()) {
            workload.compute();
        }
        if (workload.reSchedule()) {
            this.deque.add(workload);
        }
    }
}