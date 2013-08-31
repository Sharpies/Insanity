package com.lucas.engine.impl;

import com.lucas.engine.tasks.InfiniteTask;

/**
 * An implementation of the {@link InfiniteTask} for the {@link PlayerUpdating} procedure
 * @author Ares_
 *
 */
public final class PlayerUpdatingTask extends InfiniteTask {

	/*
	 * (non-Javadoc)
	 * @see com.lucas.engine.Task#execute()
	 */
	@Override
	public void execute() {
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.lucas.engine.Task#stop()
	 */
	@Override
	public void stop() {

	}

	/*
	 * (non-Javadoc)
	 * @see com.lucas.engine.Task#shouldStop()
	 */
	@Override
	public boolean shouldStop() {
		return false; // we dont want the player updating to stop
	}
	
}