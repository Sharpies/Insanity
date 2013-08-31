package com.lucas.engine.tasks;

import com.lucas.engine.Task;

/**
 * Created with NetBeans 7.0.1. Date: 2/08/13 Time: 05:31 AM
 *
 * An infinite {@link Task}.
 * 
 * @author Sebastian <juan.2114@hotmail.com>
 * 
 * @see java.lang.Object 
 */
public abstract class InfiniteTask implements Task {
    
    @Override
    public boolean infiniteTask() {
        return true;
    }   
}
