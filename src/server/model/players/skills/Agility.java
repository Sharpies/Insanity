package server.model.players.skills;

import com.lucas.rs2.Config;

import server.model.players.Client;
import server.event.*;

public class Agility {
	
	/**
	 ** @author L A
	 **
	 **/
	
	private boolean logBalance, obstacleNetUp, treeBranchUp, balanceRope, treeBranchDown, obstacleNetOver;
	public  boolean doingAgility;	
	
	private void agilityWalk(final Client c, final int walkAnimation, final int x, final int y) {
		c.isRunning2 = false;
		c.getPA().sendFrame36(173, 0);
		c.playerWalkIndex = walkAnimation;
		c.getPA().requestUpdates();
		c.getPA().walkTo(x ,y);
	}
	
	private void resetAgilityWalk(final Client c) {
		c.isRunning2 = true;
		c.getPA().sendFrame36(173, 1);
		c.playerWalkIndex = 0x333;
		c.getPA().requestUpdates();
	}
	
	private int[] agilityObject = {
		2295,
		2285, 2286,
		2313,
		2312,
		2314, 2315,
		154, 4058		
	};
	
	public boolean agilityObstacle(final Client c, final int objectType) {
		for (int i = 0; i < agilityObject.length; i++) {
			if (objectType == agilityObject[i]) {
				return true;
			}
		}
		return false;		
	}
	
	public void agilityCourse(final Client c, final int objectType) {
		switch (objectType) {
		case 2295:
			doingAgility = true;
			while (c.absX != 2474 && c.absY != 3436) {
				c.getPA().walkTo(2474 - c.absX, 3436 - c.absY);
			}
			agilityWalk(c, 762, 0, -7);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (c.absX == 2474 && c.absY == 3429) {
						container.stop();
					}
				}
				@Override
				public void stop() {
					resetAgilityWalk(c);
					c.getPA().addSkillXP((int) 7.5 * Config.AGILITY_EXPERIENCE, c.playerAgility);
					logBalance = true;
					doingAgility = false;
				}
			}, 1);
			break;
		case 2285:
			c.startAnimation(828);
			c.getPA().movePlayer(c.absX, 3424, 1);
			c.getPA().addSkillXP((int) 7.5 * Config.AGILITY_EXPERIENCE, c.playerAgility);
			obstacleNetUp = true;
			break;
		case 2313:
			c.startAnimation(828);
			c.getPA().movePlayer(2473, 3420, 2);
			c.getPA().addSkillXP((int) 5 * Config.AGILITY_EXPERIENCE, c.playerAgility);
			treeBranchUp = true;
			break;
		case 2312:
			doingAgility = true;
			while (c.absX != 2477 && c.absY != 3420) {
				c.getPA().walkTo(2477 - c.absX, 3420 - c.absY);
			}
			agilityWalk(c, 762, 6, 0);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (c.absX == 2483 && c.absY == 3420) {
						container.stop();
					}
				}
				@Override
				public void stop() {
					resetAgilityWalk(c);
					c.getPA().addSkillXP((int) 7 * Config.AGILITY_EXPERIENCE, c.playerAgility);
					balanceRope = true;
					doingAgility = false;
				}
			}, 1);
			break;
		case 2314:
		case 2315:
			c.startAnimation(828);
			c.getPA().movePlayer(c.absX, c.absY, 0);
			c.getPA().addSkillXP((int) 5 * Config.AGILITY_EXPERIENCE, c.playerAgility);
			treeBranchDown = true;
			break;
		case 2286:
			doingAgility = true;
			c.startAnimation(828);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					c.getPA().movePlayer(c.absX, 3427, 0);
					container.stop();
				}
				@Override
				public void stop() {
					c.turnPlayerTo(c.absX, 3426);
					c.getPA().addSkillXP((int) 8 * Config.AGILITY_EXPERIENCE, c.playerAgility);
					obstacleNetOver = true;
					doingAgility = false;
				}
			}, 1);
			break;
		case 154:
		case 4058:
			doingAgility = true;
			while (c.absX != 2484 && c.absY != c.objectY - 1) {
				c.getPA().walkTo(2484 - c.absX, (c.objectY - 1) - c.absY);
			}
			c.startAnimation(749);	
			agilityWalk(c, 844, 0, 7);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (c.absY == 3437) {
						container.stop();
					}
				}
				@Override
				public void stop() {
					c.startAnimation(748);
					resetAgilityWalk(c);
					if (logBalance && obstacleNetUp && treeBranchUp && balanceRope && treeBranchDown && obstacleNetOver) {
						c.getPA().addSkillXP((int) 47 * Config.AGILITY_EXPERIENCE, c.playerAgility);
						c.sendMessage("You have completed the full gnome agility course.");
					} else {
						c.getPA().addSkillXP((int) 7 * Config.AGILITY_EXPERIENCE, c.playerAgility);
					}
					logBalance = false;
					obstacleNetUp = false;
					treeBranchUp = false;
					balanceRope = false;
					treeBranchDown = false;
					obstacleNetOver = false;
					doingAgility = false;
				}
			}, 1);
			break;
		}
	}
}