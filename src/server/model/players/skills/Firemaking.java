package server.model.players.skills;

import com.lucas.Server;
import com.lucas.rs2.Config;
import com.lucas.rs2.pf.region.*;

import server.model.objects.*;
import server.model.players.*;
import server.*;
import server.util.Misc;
import server.world.ObjectHandler;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;


/**
 * Firemaking.java
 * 
 * @author Sanity
 * 
 **/
public class Firemaking {

    private Client c;

    private int[] logs = { 1511, 1521, 1519, 1517, 1515, 1513 };
    private int[] exp = {30,60,80,100,120,150};
    private int[] level = { 1, 15, 30, 45, 60, 75 };
    public long lastLight;
	private int fireId = 2732;
    private int DELAY = 1250;
    public boolean resetAnim = false;

    public Firemaking(Client c) {
	this.c = c;
    }

    public void checkLogType(int logType, int otherItem) {
	for (int j = 0; j < logs.length; j++) {
	    if (logs[j] == logType || logs[j] == otherItem) {
		lightFire(j);
		return;
	    }
	}
    }

    public void lightFire(int slot) {
	if(c.absX == Objects.getObjectX() && c.absY == Objects.getObjectY() && Objects.getObjectId() > 0) {
		c.sendMessage("You can't light a fire here!");
		return;
	}
	if (c.duelStatus >= 5) {
	    c.sendMessage("Why am I trying to light a fire in the duel arena?");
	    return;
	}
	final int x = c.getX();
	final int y = c.getY();
	
	if (Region.getClipping(c.getX() - 1, c.getY(), c.heightLevel, -1, 0)) {
			c.getPA().walkTo(-1, 0);
		} else if (Region.getClipping(c.getX() + 1, c.getY(), c.heightLevel, 1, 0)) {
			c.getPA().walkTo(1, 0);
		} else if (Region.getClipping(c.getX(), c.getY() - 1, c.heightLevel, 0, -1)) {
			c.getPA().walkTo(0, -1);
		} else if (Region.getClipping(c.getX(), c.getY() + 1, c.heightLevel, 0, 1)) {
			c.getPA().walkTo(0, 1);
		}
	//}
	
	if (c.playerLevel[c.playerFiremaking] >= level[slot]) {
			if (c.getItems().playerHasItem(590) && c.getItems().playerHasItem(logs[slot])) {
				if (System.currentTimeMillis() - lastLight > DELAY) {
					c.startAnimation(733,0);
					c.getItems().deleteItem(logs[slot],
							c.getItems().getItemSlot(logs[slot]), 1);
					c.getPA().addSkillXP(
							exp[slot] * Config.FIREMAKING_EXPERIENCE,
							c.playerFiremaking);
					c.sendMessage("You light the fire.");
					//c.getPA().walkTo(0,0);
					//c.getPA().checkObjectSpawn(fireId, x, y, 1, 10);
					Server.objectHandler.createAnObject(c, fireId, x, y);
					c.turnPlayerTo(c.getX() + 1, c.getY());
					
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			int timer = 1;
			@Override
			public void execute(CycleEventContainer container) {
			
			
			if (timer == 0) {
			container.stop();
			}
			timer--;
			
			
			}
			@Override
			public void stop() {
				c.getPA().checkObjectSpawn(-1, x, y, 1, 10);
				//c.sendMessage("Your fire has been extinguished.");
				Server.itemHandler.createGroundItem(c, 592, x, y, 1, c.playerId);
			}
		}, 45);					
					this.lastLight = System.currentTimeMillis();
					//c.getPA().frame1();
					//resetAnim = true;
					c.startAnimation(65535);
				}
			}
		}	
	}
	
}