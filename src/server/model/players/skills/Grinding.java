package server.model.players.skills;

import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.players.Client;
import server.model.players.skills.SkillHandler;

public class Grinding extends SkillHandler {
	
	private final static int[][] GRINDABLES = {
		
		{237, 235},
		{1973, 1975},
		{5075, 6693}
		
	};
	
	public static void grindItem(final Client c, final int itemUsed, final int usedWith, final int itemSlot) {
		if (isSkilling[15] == true) {
			return;
		}
		c.startAnimation(364);
		final int itemId = (itemUsed == 233 ? usedWith : itemUsed);
		for (int i = 0; i < GRINDABLES.length; i++) {
			if (itemId == GRINDABLES[i][0]) {
				isSkilling[15] = true;
				final int product = GRINDABLES[i][1];
				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (isSkilling[15] == true) {
							c.getItems().deleteItem(itemId, itemSlot, 1);
							c.getItems().addItem(product, 1);
							c.sendMessage("You grind the "+ c.getItems().getItemName(itemId) +" into "+ c.getItems().getItemName(product) +".");
							container.stop();
						}
					}
					@Override
					public void stop() {
						isSkilling[15] = false;
					}
				}, 3);
			}
		}
	}
}