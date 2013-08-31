package com.lucas.engine;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with NetBeans 7.0.1. Date: 2/08/13 Time: 05:31 AM
 *
 * @author Sebastian <juan.2114@hotmail.com>
 * 
 * @see java.lang.Object 
 */
public final class ThreadingEngine implements Runnable {
    
    /**
     * The {@link BlockingQueue} of {@link Task}'s.
     */
    private BlockingQueue<Task> tasks = new LinkedBlockingQueue<Task>();
    
    /**
     * The {@link Executor} logic service.
     */
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    
    /**
     * The responsible {@link Thread} for this {@link Executor}.
     */
    private Thread thread;
    
    /**
     * Is the {@link ThreadingEngine} running.
     */
    private boolean isRunning = false;

    /**
     * Starts the new {@link ThreadingEngine}.
     */
    public void startEngine() {
        if (isRunning) {
            throw new IllegalStateException("This engine is already running.");
        }
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Adds a {@link Task} into the {@link Vector}.
     */
    public void addTask(final Task task) {
        tasks.offer(task);
    }

    /**
     * Cancels a {@link Task} before is being executed by the {@link Executors}.
     */
    public void cancelTask(final Task task) {
        if (tasks.contains(task)) {
            tasks.remove(task);
        }
    }
    
    /**
     * Constructs a new default {@link ThreadingEngine} .
     */
    public ThreadingEngine() { 
          super(); // Looks prettier
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                try {
                    final Task task = tasks.take();
                    if (!task.infiniteTask()) {
                        executor.submit(new Runnable() {

                            @Override
                            public void run() {
                            	if (task.shouldStop()) task.stop();
                                task.execute();
                            }
                        });
                    } else {
                        executor.scheduleAtFixedRate(new Runnable() {
                            @Override
                            public void run() {
                            	if (task.shouldStop()) task.stop();
                                task.execute();
                            }
                        }, 0, 600, TimeUnit.MILLISECONDS);
                    }
                } catch (InterruptedException t) {
                    continue;
                }
            }
        } finally {
            executor.shutdownNow();
        }
    }
}