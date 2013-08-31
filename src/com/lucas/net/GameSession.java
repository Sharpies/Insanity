package com.lucas.net;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author Sean
 * @author Ares_
 *
 */
public abstract class GameSession {

	/**
	 * The channel.
	 */
	protected final Channel channel;
	
	/**
	 * The channel handler context.
	 */
	protected final ChannelHandlerContext context;
	
	/**
	 * Creates a new {@link GameSession}
	 * @param context The channel handler context.
	 */
	public GameSession(ChannelHandlerContext context) {
		this.channel = context.getChannel();
		this.context = context;
	}
	
	/**
	 * Handles the disconnection of a certain channel.
	 */
	public abstract void disconnected();
	
	/**
	 * Gets the channel.
	 * @return The channel.
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * Gets the channel handler context.
	 * @return The channel handler context.
	 */
	public ChannelHandlerContext getContext() {
		return context;
	}

	/**
	 * Receives the message fired from the frame decoder.
	 * @param obj The object fired.
	 */
	public abstract void message(Object obj);
	
}
