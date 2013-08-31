package server.model.players.skills;

import server.model.players.Client;

public class Tanning extends CraftingData {

	public static void sendTanningInterface(final Client c) {
		c.getPA().showInterface(14670);
		for (final tanningData t : tanningData.values()) {
			c.getPA().itemOnInterface(t.getItemFrame(), 250, t.getLeatherId());
			c.getPA().sendFrame126(t.getName(), t.getNameFrame());
			if (c.getItems().playerHasItem(995, t.getPrice())) {
				c.getPA().sendFrame126("@gre@Price: "+ t.getPrice(), t.getCostFrame());
			} else {
				c.getPA().sendFrame126("@red@Price: "+ t.getPrice(), t.getCostFrame());
			}
		}
	}

	public static void tanHide(final Client c, final int buttonId) {
		for (final tanningData t : tanningData.values()) {
			if (buttonId == t.getButtonId(buttonId)) {
				int amount = c.getItems().getItemCount(t.getHideId());
				if (amount > t.getAmount(buttonId)) {
					amount = t.getAmount(buttonId);
				}
				int price = (amount * t.getPrice());
				int coins = c.getItems().getItemCount(995);
				if (price > coins) {
					price = (coins - (coins % t.getPrice()));
				}
				if (price == 0) {
					c.sendMessage("You do not have enough coins to tan this hide.");
					return;
				}
				amount = (price / t.getPrice());
				final int hide = t.getHideId();
				final int leather = t.getLeatherId();
				if (c.getItems().playerHasItem(995, price)) {
					if (c.getItems().playerHasItem(hide)) {
						c.getItems().deleteItem(hide, amount);
						c.getItems().deleteItem(995, c.getItems().getItemSlot(995), 20);
						c.getItems().addItem(leather, amount);
						c.sendMessage("The tanner tans the hides for you.");
					} else {
						c.sendMessage("You do not have any hides to tan.");
						return;
					}
				} else {
					c.sendMessage("You do not have enough coins to tan this hide.");
					return;
				}
			}
		}
	}
}