package server.model.players;

import com.lucas.Server;
import com.lucas.rs2.pf.region.Region;

import server.model.players.Client;
import server.event.CycleEventHandler;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.util.Misc;
import server.*;

	/**
	 * 	Class MithrilSeeds
	 * Handles	Mithril Seeds
	 * @author	I'mAGeek
	 * START:	8:01 AM 3/22/2012
	 * FINISH:	8:20 AM 3/22/2012
	 */

public class MithrilSeeds {

	public static int rFlower[] = {2460,2462,2464,2466,2468,2470,2472,2474,2476};
	public static int flower() {
		return rFlower[(int)(Math.random()*rFlower.length)];
	}

	public static void plantMithrilSeed(Client c) {
		final int[] coords = new int[2];
		coords[0] = c.absX;
		coords[1] = c.absY;
		
			if (c.getItems().playerHasItem(299,1)) {
				c.getItems().deleteItem2(299, 1);
				c.getPA().addSkillXP(2500, 19);
				c.startAnimation(827);
				c.canWalk = false;
				Server.objectHandler.createAnObject(c, c.randomFlower(), coords[0], coords[1]);
				c.getDH().sendDialogues(22, -1);
			}
	}
	public static void processFlower(Client c) {
		final int[] coords = new int[2];
		coords[0] = c.absX;
		coords[1] = c.absY;
		
		Server.objectHandler.createAnObject(c, -1, coords[0], coords[1]);
		Server.objectHandler.createAnObject(c, c.randomFlower(), coords[0], coords[1]);
		c.canWalk = true;
		if (Region.getClipping(c.getX() - 1, c.getY(), c.heightLevel, -1, 0)) {
			c.getPA().walkTo(-1, 0);
     		} else if (Region.getClipping(c.getX() + 1, c.getY(), c.heightLevel, 1, 0)) {
			c.getPA().walkTo(1, 0);
     		} else if (Region.getClipping(c.getX(), c.getY() - 1, c.heightLevel, 0, -1)) {
			c.getPA().walkTo(0, -1);
       		} else if (Region.getClipping(c.getX(), c.getY() + 1, c.heightLevel, 0, 1)) {
			c.getPA().walkTo(0, 1);
       		}
		c.turnPlayerTo(coords[0] + 1, coords[1]);
		c.sendMessage("You plant a flower!");

		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				for (int j = 0; j < Server.playerHandler.players.length; j++) {
				if (Server.playerHandler.players[j] != null) {
				Client c = (Client)Server.playerHandler.players[j];
					Server.objectHandler.createAnObject(c, -1, coords[0], coords[1]);
					container.stop();
				}
				}
			}
			@Override
			public void stop() {

			}
		}, 100);
	}
	
	public static void pickupFlowers(Client c) {
		final int[] coords = new int[2];
		coords[0] = c.absX;
		coords[1] = c.absY;
		
		c.canWalk = true;
		Server.objectHandler.createAnObject(c, -1, coords[0], coords[1]);
		c.getItems().addItem(flower(), 1);		
	}
}