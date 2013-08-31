package com.lucas.net;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.lucas.packet.Packet;

import server.model.players.Client;

/**
 * The game channel handler using an implementation of {@link SimpleChannelHandler}
 * @author Ares_
 *
 */
public class GameChannelHandler extends SimpleChannelHandler {
	
	/**
	 * The {@link Logger} instance for the {@link GameChannelHandler}
	 */
	private static Logger logger = Logger.getLogger(GameChannelHandler.class.getName());
	
	private Session session;
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#channelDisconnected(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent evt) {
		Channel channel = evt.getChannel();
		/*Object attachment = ctx.getAttachment();
		if(attachment != null) {
			((GameSession) ctx.getAttachment()).disconnected();
		}*/
		if (session != null) {
			Client client = session.getClient();
			if (client != null) {
				client.disconnected = true;
			}
			session = null;
		}
		logger.info("Channel disconnected from game: " + channel.getRemoteAddress() + ".");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#exceptionCaught(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ExceptionEvent)
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent evt) {
		Channel channel = evt.getChannel();
		logger.log(Level.WARNING, "Exception caught, closing in game", evt.getCause());
		if(channel.isConnected())
			channel.close();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		if (e.getMessage() instanceof Client) {
			session.setClient((Client) e.getMessage());
		} else if (e.getMessage() instanceof Packet) {
			if (session.getClient() != null) {
				session.getClient().queueMessage((Packet) e.getMessage());
			}
		}
		// TODO re write this chunk of shit
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#channelConnected(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		// TODO re do this also since I don't like it but it will do for now.
		if (session == null)
			session = new Session(ctx.getChannel());
	}
	
}