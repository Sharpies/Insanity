package server.model.players.skills;

import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.players.Client;

public class JewelryMaking extends CraftingData {

	public static void jewelryMaking(final Client c, final String type, final int itemId, final int amount) {
		switch (type) {
		case "RING":
			for (int i = 0; i < jewelryData.RINGS.item.length; i++) {
				if (itemId == jewelryData.RINGS.item[i][1]) {
					mouldJewelry(c, jewelryData.RINGS.item[i][0], itemId, amount, jewelryData.RINGS.item[i][2], jewelryData.RINGS.item[i][3]);
				}
			}
			break;
		case "NECKLACE":
			for (int i = 0; i < jewelryData.NECKLACE.item.length; i++) {
				if (itemId == jewelryData.NECKLACE.item[i][1]) {
					mouldJewelry(c, jewelryData.NECKLACE.item[i][0], itemId, amount, jewelryData.NECKLACE.item[i][2], jewelryData.NECKLACE.item[i][3]);
				}
			}
			break;
		case "AMULET":
			for (int i = 0; i < jewelryData.AMULETS.item.length; i++) {
				if (itemId == jewelryData.AMULETS.item[i][1]) {
					mouldJewelry(c, jewelryData.AMULETS.item[i][0], itemId, amount, jewelryData.AMULETS.item[i][2], jewelryData.AMULETS.item[i][3]);
				}
			}
			break;
		}
	}
	
	private static int time;
	
	private static void mouldJewelry(final Client c, final int required, final int itemId, final int amount, final int level, final int xp) {
		if (c.playerSkilling[12] == true) {
			return;
		}
		if (c.playerLevel[12] < level) {
			c.sendMessage("You need a crafting level of "+ level +" to mould this item.");
			return;
		}
		if (!c.getItems().playerHasItem(2357)) {
			c.sendMessage("You need a gold bar to mould this item.");
			return;
		}
		final String itemRequired = c.getItems().getItemName(required);		
		if (!c.getItems().playerHasItem(required)) {
			c.sendMessage("You need "+ ((itemRequired.startsWith("A") || itemRequired.startsWith("E") || itemRequired.startsWith("O")) ? "an" : "a") +" "+ itemRequired.toLowerCase() +" to mould this item.");
			return;
		}
		time = amount;
		c.getPA().removeAllWindows();
		final String itemName = c.getItems().getItemName(itemId);
		c.startAnimation(899);
		c.playerSkilling[12] = true;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (c.playerSkilling[12] == true) {
					if (time == 0) {
						container.stop();
					}
					if (c.getItems().playerHasItem(2357) && c.getItems().playerHasItem(required)) {
						c.getItems().deleteItem(2357, 1);
						c.getItems().deleteItem(required, 1);
						c.getItems().addItem(itemId, 1);
						c.startAnimation(899);
						c.getPA().addSkillXP(xp, 12);
						time--;
						c.sendMessage("You make "+ ((itemName.startsWith("A") || itemName.startsWith("E") || itemName.startsWith("O")) ? "an" : "a") +" "+ itemName.toLowerCase());
					} else {
						container.stop();
					}
				} else {
					container.stop();
				}
			}
			@Override
			public void stop() {
				
			}
		}, 4);
	}
	
	public static void stringAmulet(final Client c, final int itemUsed, final int usedWith) {
		final int amuletId = (itemUsed == 1759 ? usedWith : itemUsed);
		for (final amuletData a : amuletData.values()) {
			if (amuletId == a.getAmuletId()) {
				c.getItems().deleteItem(1759, 1);
				c.getItems().deleteItem(amuletId, 1);
				c.getItems().addItem(a.getProduct(), 1);
				c.getPA().addSkillXP(4, 12);
			}
		}
	}
	
	public static void jewelryInterface(final Client c) {
		c.getPA().showInterface(4161);
		for (final jewelryData i : jewelryData.values()) {
			if (c.getItems().playerHasItem(1592)) {
				for (int j = 0; j < i.item.length; j++) {
					if (c.getItems().playerHasItem(jewelryData.RINGS.item[j][0])) {
						c.getPA().sendFrame34(jewelryData.RINGS.item[j][1], j, 4233, 1);
					} else {
						c.getPA().sendFrame34(-1, j, 4233, 1);
					}
					c.getPA().sendFrame126("", 4230);
					c.getPA().sendFrame246(4229, 0, -1);
				}
			} else {
				c.getPA().sendFrame246(4229, 120, 1592);
				for (int j = 0; j < i.item.length; j++) {
					c.getPA().sendFrame34(-1, j, 4233, 1);
				}
				c.getPA().sendFrame126("You need a ring mould to craft rings.", 4230);
			}
			if (c.getItems().playerHasItem(1597)) {
				for (int j = 0; j < i.item.length; j++) {
					if (c.getItems().playerHasItem(jewelryData.NECKLACE.item[j][0])) {
						c.getPA().sendFrame34(jewelryData.NECKLACE.item[j][1], j, 4239, 1);
					} else {
						c.getPA().sendFrame34(-1, j, 4239, 1);
					}
					c.getPA().sendFrame126("", 4236);
					c.getPA().sendFrame246(4235, 0, -1);
				}
			} else {
				c.getPA().sendFrame246(4235, 120, 1597);
				for (int j = 0; j < i.item.length; j++) {
					c.getPA().sendFrame34(-1, j, 4239, 1);
				}
				c.getPA().sendFrame126("You need a necklace mould to craft necklaces.", 4236);
			}
			if (c.getItems().playerHasItem(1595)) {
				for (int j = 0; j < i.item.length; j++) {
					if (c.getItems().playerHasItem(jewelryData.AMULETS.item[j][0])) {
						c.getPA().sendFrame34(jewelryData.AMULETS.item[j][1], j, 4245, 1);
					} else {
						c.getPA().sendFrame34(-1, j, 4245, 1);
					}
					c.getPA().sendFrame126("", 4242);
					c.getPA().sendFrame246(4241, 0, -1);
				}
			} else {
				c.getPA().sendFrame246(4235, 120, 1597);
				for (int j = 0; j < i.item.length; j++) {
					c.getPA().sendFrame34(-1, j, 4245, 1);
				}
				c.getPA().sendFrame126("You need an amulet mould to craft amulets.", 4242);
			}
		}
	}
}