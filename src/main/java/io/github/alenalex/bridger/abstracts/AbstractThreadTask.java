package io.github.alenalex.bridger.abstracts;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.exceptions.ThreadInitializationError;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class AbstractThreadTask {

    public static final long MIN_THREAD_CALL_PERIOD = 100;

    private final Bridger plugin;
    private final String threadName;
    protected ScheduledExecutorService servicePool;
    private final int poolSize;

    private long threadCallPeriod;
    private boolean running;

    public AbstractThreadTask(@NotNull Bridger plugin, @NotNull String threadName, int poolSize, long threadCallPeriod) {
        this.plugin = plugin;
        this.poolSize = poolSize;
        this.threadCallPeriod = threadCallPeriod;
        this.threadName = threadName;
        this.servicePool = new ScheduledThreadPoolExecutor(poolSize);
        this.running = false;
    }

    public AbstractThreadTask(Bridger plugin, String threadName, int poolSize) {
        this.plugin = plugin;
        this.threadName = threadName;
        this.poolSize = poolSize;
        this.servicePool = new ScheduledThreadPoolExecutor(poolSize);
        this.threadCallPeriod = 1000L;
        this.running = false;
    }

    public Bridger getPlugin() {
        return plugin;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public long getThreadCallPeriod() {
        return threadCallPeriod;
    }

    public void setThreadCallPeriod(long threadCallPeriod) {
        this.threadCallPeriod = threadCallPeriod;
    }

    public String getThreadName() {
        return threadName;
    }

    public boolean startThread(){
        final Runnable runnableTask = callableTask();
        if(threadCallPeriod <= MIN_THREAD_CALL_PERIOD)
            throw new ThreadInitializationError("The period for calling thread should be greater than "+MIN_THREAD_CALL_PERIOD);

        if(runnableTask == null)
            throw new ThreadInitializationError("The provided runnable task for thread execution is empty!");


        this.servicePool.scheduleAtFixedRate(callableTask(), 0L, threadCallPeriod, TimeUnit.MILLISECONDS);
        this.running = true;
        plugin.getLogger().info("Thread spawned for "+threadName+" task!");
        return true;
    }

    public void stopThread(){
        if(!isRunning())
            return;

        plugin.getLogger().info("Stopping thread "+threadName);
        prepareStop();
        this.servicePool.shutdown();
        this.running = false;
        plugin.getLogger().info("The thread "+threadName+" has been dropped successfully");
    }

    public void reloadThread(){
        if(!this.servicePool.isShutdown()) {
            plugin.getLogger().warning("The thread "+threadName+" was forced to abort with "+this.servicePool.shutdownNow().size()+" task left!");
        }

        this.servicePool = null;
        this.servicePool = new ScheduledThreadPoolExecutor(poolSize);
    }

    protected abstract Runnable callableTask();

    protected abstract void prepareStop();

    public boolean isRunning() {
        return running;
    }
}
