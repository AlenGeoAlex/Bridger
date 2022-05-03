package io.github.alenalex.bridger.handler;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.interfaces.IHandler;
import io.github.alenalex.bridger.workload.core.WorkloadDistributor;
import io.github.alenalex.bridger.workload.core.WorkloadThread;
import org.bukkit.scheduler.BukkitTask;

public class WorkloadHandler implements IHandler {

    private final Bridger plugin;

    private final WorkloadDistributor workloadDistributor;

    private WorkloadThread syncThread;

    private BukkitTask syncTask;

    public WorkloadHandler(Bridger plugin) {
        this.plugin = plugin;
        this.workloadDistributor = new WorkloadDistributor();
    }

    @Override
    public boolean initHandler(){
        this.syncThread = workloadDistributor.createThread(220000L);
        return syncThread != null;
    }

    @Override
    public void prepareHandler() {
        this.syncTask = plugin.getServer().getScheduler().runTaskTimer(plugin, syncThread, 1L, 1L);
    }

    @Override
    public Bridger plugin() {
        return this.plugin;
    }

    @Override
    public void disableHandler() {
        if(syncTask != null)
            syncTask.cancel();

    }

    public WorkloadThread getSyncThread() {
        return syncThread;
    }
}
