package server.model.players.skills;

import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.players.Client;

public class PotionMaking extends HerbData {

	public static void createUnfinishedPotion(final Client c, final int itemUsed, final int usedWith) {
		if (isSkilling[15] == true) {
			return;
		}
		for (int i = 0; i < unfinishedPotions.length; i++) {
			final int itemId = (itemUsed == 227 ? usedWith : itemUsed);
			if (itemId == unfinishedPotions[i][1]) {
				if (c.playerLevel[15] < unfinishedPotions[i][2]) {
					c.sendMessage("You need an herblore level of "+ unfinishedPotions[i][2] +" to make this potion.");					return;
				}
				final int product = unfinishedPotions[i][0];
				handlePotionMaking(c, itemId, 227, product, 0);
			}
		}
	}	
	
	public static boolean isPotion(final Client c, final int itemUsed, final int usedWith) {
		for (int i = 0; i < finishedPotions.length; i++) {
			if (itemUsed == finishedPotions[i][1] || usedWith == finishedPotions[i][1]) {
				System.out.println("True "+ itemUsed +" - "+ usedWith);
				createPotion(c, itemUsed, usedWith);
				return true;
			}
			if (usedWith == finishedPotions[i][2] || usedWith == finishedPotions[i][2]) {
				System.out.println("True "+ itemUsed +" - "+ usedWith);
				createPotion(c, itemUsed, usedWith);
				return true;
			}
		}
		System.out.println("False "+ itemUsed +" - "+ usedWith);
		return false;
	}

	public static void createPotion(final Client c, final int itemUsed, final int usedWith) {
		if (isSkilling[15] == true) {
			return;
		}
		for (int i = 0; i < finishedPotions.length; i++) {
			System.out.println("Debug 1");
			final int primary = (usedWith == finishedPotions[i][1] ? usedWith : itemUsed);
			if (primary == finishedPotions[i][1]) {
				System.out.println("Debug 2");
				if (c.playerLevel[15] < finishedPotions[i][3]) {
					c.sendMessage("You need an herblore level of "+ finishedPotions[i][3] +" to make this potion.");
					return;
				}
				final int product = finishedPotions[i][0];
				final int secondary = finishedPotions[i][2];
				handlePotionMaking(c, primary, secondary, product, finishedPotions[i][4]);
			}
		}
	}
	
	private static void handlePotionMaking(final Client c, final int primary, final int secondary, final int product, final int xp) {
		isSkilling[15] = true;
		c.startAnimation(363);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (isSkilling[15] == true) {
					if (c.getItems().playerHasItem(primary) && c.getItems().playerHasItem(secondary)) {
						c.getItems().deleteItem(primary, 1);
						c.getItems().deleteItem(secondary, 1);
						c.getItems().addItem(product, 1);
						c.getPA().addSkillXP(xp, 15);
						c.startAnimation(363);
						c.sendMessage("You make a "+ c.getItems().getItemName(product));
					} else {
						container.stop();
					}
				} else {
					container.stop();
				}
			}
			@Override
			public void stop() {
				isSkilling[15] = false;
			}
		}, 4);
	}
}