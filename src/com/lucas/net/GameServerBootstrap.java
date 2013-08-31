package com.lucas.net;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;

import com.lucas.rs2.GameConstants;


/**
 * Designed to setup the netty networking only
 * @author Ares_
 *
 */
public final class GameServerBootstrap {

	/**
	 * The {@link Logger} instance for the {@link GameServerBootstrap}
	 */
	private static Logger logger = Logger.getLogger(GameServerBootstrap.class.getName());
	
	/**
	 * The {@link ServerBootstrap}
	 */
	private ServerBootstrap bootstrap = new ServerBootstrap();

	/**
	 * The {@link ExecutorService} and {@link Executors}
	 */
	private ExecutorService executor = Executors.newCachedThreadPool();
	
	/**
	 * Constructs a new {@link GameServerBootstrap}
	 */
	public GameServerBootstrap() {
		// TODO add jaggrab and shit here
	}
	
	/**
	 * We set up our netty networking on this void
	 */
	public void bind() {
		logger.info("Starting the networking strap...");
		
		// start setting up the netty networking
		bootstrap.setFactory(new NioServerSocketChannelFactory(executor, executor));
		bootstrap.setPipelineFactory(new GameServerPipelineFactory(new HashedWheelTimer()));
		
		// The address the server will be listening too
		SocketAddress game = new InetSocketAddress(GameConstants.PORT);
		
		logger.info("The game server is now listening to " +game+ ".");
		// we now are listening on this port
		bootstrap.bind(game);
	}
	
}