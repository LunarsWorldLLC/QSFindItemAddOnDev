package io.myzticbean.finditemaddon.utils.async;

import io.myzticbean.finditemaddon.utils.log.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class VirtualThreadScheduler {
    // Use a shared executor for all virtual-thread tasks
    private static ExecutorService virtualExecutor;

    private VirtualThreadScheduler() {
        // prevent instantiation
    }

    /**
     * Initialize or reinitialize the virtual thread executor.
     * Call this on plugin enable to support PlugMan reloads.
     */
    public static void init() {
        if (virtualExecutor == null || virtualExecutor.isShutdown()) {
            Logger.logInfo("Initializing virtual thread executor...");
            virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();
        }
    }

    /**
     * Run a task asynchronously on a Java Virtual Thread.
     *
     * @param task The task to run.
     * @return a Future representing the result of the task.
     */
    public static Future<?> runTaskAsync(Runnable task) {
        ensureInitialized();
        return virtualExecutor.submit(task);
    }

    /**
     * Run a task asynchronously on a Java Virtual Thread with result.
     *
     * @param task The task to run.
     * @param <T>  The type of the result.
     * @return a Future with the result.
     */
    public static <T> Future<T> runTaskAsync(java.util.concurrent.Callable<T> task) {
        ensureInitialized();
        return virtualExecutor.submit(task);
    }

    /**
     * Ensure the executor is initialized before use.
     */
    private static void ensureInitialized() {
        if (virtualExecutor == null || virtualExecutor.isShutdown()) {
            init();
        }
    }

    /**
     * Shut down the virtual thread executor gracefully.
     */
    public static void shutdown() {
        if (virtualExecutor != null && !virtualExecutor.isShutdown()) {
            Logger.logInfo("Shutting down virtual thread executor...");
            virtualExecutor.shutdown();
        }
    }
}
