package com.lucas;

import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lucas.net.GameServerBootstrap;
import com.lucas.packet.PacketRegistration;
import com.lucas.rs2.pf.region.ObjectDef;
import com.lucas.rs2.pf.region.Region;

import server.Connection;

import server.event.CycleEventHandler;
import server.event.Task;
import server.event.TaskScheduler;
import server.world.WalkingCheck;
import server.world.StillGraphicsManager;
import server.world.PlayerManager;
import server.model.objects.DoubleDoors;
import server.model.npcs.NPCHandler;
import server.model.players.PlayerHandler;
import server.model.objects.Doors;
import server.model.minigames.*;
import server.world.ItemHandler;
import server.world.ObjectHandler;
import server.world.ObjectManager;
import server.world.ShopHandler;
import server.world.ClanChatHandler;

/**
 * The main class needed to start the server.
 * @author Graham
 * @author Ares_
 * 
 */
public final class Server {

	/**
	 * The {@link Logger} instance for the {@link Server} class
	 */
	private static Logger logger = Logger.getLogger(Server.class.getName());
	
	/**
	 * Calls to manage the players on the server.
	 */
	public static PlayerManager playerManager = null;
	private static StillGraphicsManager stillGraphicsManager = null;

	/**
	 * Sleep mode of the server.
	 */
	public static boolean sleeping;

	/**
	 * Calls the rate in which an event cycles.
	 */
	public static final int cycleRate;

	/**
	 * Server updating.
	 */
	public static boolean UpdateServer = false;

	/**
	 * Calls in which the server was last saved.
	 */
	// lol wat? delete this sheit
	public static long lastMassSave = System.currentTimeMillis();

	/**
	 * Calls the usage of CycledEvents.
	 */
	private static long cycleTime, cycles, totalCycleTime, sleepTime;

	/**
	 * Used for debugging the server.
	 */
	private static DecimalFormat debugPercentFormat;
	// ^ useless
	
	/**
	 * Forced shutdowns.
	 */
	public static boolean shutdownServer = false;
	public static boolean shutdownClientHandler;

	/**
	 * Calls the usage of player items.
	 */
	public static ItemHandler itemHandler = new ItemHandler();

	/**
	 * Handles logged in players.
	 */
	public static PlayerHandler playerHandler = new PlayerHandler();

	/**
	 * Handles global NPCs.
	 */
	public static NPCHandler npcHandler = new NPCHandler();

	/**
	 * Handles global shops.
	 */
	public static ShopHandler shopHandler = new ShopHandler();

	/**
	 * Handles global objects.
	 */
	public static ObjectHandler objectHandler = new ObjectHandler();
	public static ObjectManager objectManager = new ObjectManager();

	/**
	 * Handles the castlewars minigame.
	 */
	public static CastleWars castleWars = new CastleWars();

	/**
	 * Handles the fightpits minigame.
	 */
	public static FightPits fightPits = new FightPits();

	/**
	 * Handles the pestcontrol minigame.
	 */
	public static PestControl pestControl = new PestControl();

	/**
	 * Handles the fightcaves minigames.
	 */
	public static FightCaves fightCaves = new FightCaves();

	/**
	 * Handles the clan chat.
	 */
	public static ClanChatHandler clanChat = new ClanChatHandler();

	/**
	 * Handles the task scheduler.
	 */
	private static final TaskScheduler scheduler = new TaskScheduler();

	/**
	 * Gets the task scheduler.
	 */
	public static TaskScheduler getTaskScheduler() {
		return scheduler;
	}

	static {
		cycleRate = 600;
		shutdownServer = false;
		sleepTime = 0;
		debugPercentFormat = new DecimalFormat("0.0#%");
	}

	/**
	 * Starts the server.
	 */
	public static void main(java.lang.String args[]) {
		logger.info("Starting the server emulator up..");
		ObjectDef.loadConfig();
		Region.load();
		WalkingCheck.load();

		playerManager = PlayerManager.getSingleton();
		playerManager.setupRegionPlayers();
		stillGraphicsManager = new StillGraphicsManager();

		Doors.getSingleton().load();
		DoubleDoors.getSingleton().load();
		Connection.initialize();

		// start the packet listeners here
		try {
			PacketRegistration.PacketLoader.parsePackets();
		} catch (Throwable canThrow) {
			logger.log(Level.SEVERE, "The packet reader could not configure the packet correctly.");
		}
			
		// start the multi threading engine here
		
		// finally start the netty bootstrap
		new GameServerBootstrap().bind();
		
		/**
		 * Main server tick.
		 */
		scheduler.schedule(new Task() {
			@Override
			protected void execute() {
				// this will all be seperate threads
				itemHandler.process();
				playerHandler.process();
				npcHandler.process();
				shopHandler.process();
				CycleEventHandler.getSingleton().process();
				objectManager.process();
				fightPits.process();
				pestControl.process();
			}
		});

	}

	/**
	 * Logging execution.
	 */
	public static boolean playerExecuted = false;

	/**
	 * Gets the sleep mode timer and puts the server into sleep mode.
	 */
	public static long getSleepTimer() {
		return sleepTime;
	}

	/**
	 * Gets the Graphics manager.
	 */
	public static StillGraphicsManager getStillGraphicsManager() {
		return stillGraphicsManager;
	}

	/**
	 * Gets the Player manager.
	 */
	public static PlayerManager getPlayerManager() {
		return playerManager;
	}

	/**
	 * Gets the Object manager.
	 */
	public static ObjectManager getObjectManager() {
		return objectManager;
	}

}