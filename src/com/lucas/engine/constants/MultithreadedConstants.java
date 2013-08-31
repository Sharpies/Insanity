package com.lucas.engine.constants;

import com.lucas.engine.ThreadingEngine;

/**
 * Created with NetBeans 7.0.1. 1050 Date: 2/08/13 Time: 05:31 AM
 *
 * Holds all the different {@link ThreadingEngine} threads
 * 
 * @author Sebastian <juan.2114@hotmail.com>
 * 
 * @see java.lang.Object 
 */
public final class MultithreadedConstants {
    
    /**
     * Holds the {@link Task} System {@link Executors}.
     */
    private static ThreadingEngine actorsUpdating = new ThreadingEngine();
    
    /**
     * Start all the {@link Executors}.
     */
    public static void startThreading() {
    	actorsUpdating.startEngine();
    }
    
    /**
     * Get the {@link Task} {@link Executors} {@link Thread}.
     */
    public static ThreadingEngine getActorsUpdating() {
        return actorsUpdating;
    }    
}
