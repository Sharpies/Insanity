package com.lucas.engine;

/**
 * Created with NetBeans 7.0.1. Date: 2/08/13 Time: 05:31 AM
 *
 * @author Sebastian <juan.2114@hotmail.com>
 * 
 * @see java.lang.Object 
 */
public interface Task {
    
    /**
     * The execution of the {@link Task}.
     */
    void execute();
    
    /**
     * When the {@link Task} should stop
     */
    boolean shouldStop();
    
    /**
     * The action performed when the {@link Task} dies.
     */
    void stop();
    
    /**
     * If the {@link Task} should be running forever.
     */
    boolean infiniteTask();
}
