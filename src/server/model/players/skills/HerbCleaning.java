package server.model.players.skills;

import server.model.players.Client;

public class HerbCleaning extends HerbData {
	
	public static void handleHerbCleaning(final Client c, final int itemId, final int itemSlot) {
		for (int i = 0; i < grimyHerbs.length; i++) {
			if (itemId == grimyHerbs[i][0]) {
				if (c.playerLevel[15] < grimyHerbs[i][2]) {
					c.sendMessage("You need an herblore level of "+ grimyHerbs[i][2] +" to clean this herb.");
					return;
				}
				c.getItems().deleteItem(itemId, itemSlot, 1);
				c.getItems().addItem(grimyHerbs[i][1], 1);
				c.getPA().addSkillXP((int) grimyHerbs[i][3], 15);
			}
		}
	}
}