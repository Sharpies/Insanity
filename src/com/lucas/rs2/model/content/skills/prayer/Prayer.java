package com.lucas.rs2.model.content.skills.prayer;

import java.util.HashMap;

import server.model.players.Client;

/**
 * Handles the prayer burying and altar actions
 * @author Ares_
 * 
 */
public final class Prayer {

	/**
	 * The timer for in between bone burying
	 */
	private static final long BONE_BURY_TIME = 1500;

	/**
	 * An enumeration of the different bones a player can use
	 * @author Ares_
	 * 
	 */
	private enum Bones {
		REGULAR_BONE(526, 1);

		/**
		 * The bone the player can use
		 */
		private final short item;

		/**
		 * The experience you can gain from a bone
		 */
		private final short experience;

		/**
		 * A {@link HashMap} of all our bone data
		 */
		private static HashMap<Short, Bones> bone = new HashMap<Short, Bones>();

		/**
		 * Constructs a new {@link Bones}
		 * @param item The id of the bone we are using
		 * @param experience The experience gained from it
		 */
		Bones(int item, int experience) {
			this.item = (short) item;
			this.experience = (short) experience;
		}

		static {
			for (final Bones bonez : bone.values())
				Bones.bone.put(bonez.getExperience(), bonez);
		}

		/**
		 * Gets the item or bone in this case
		 * @return The bone we are using
		 */
		public short getItem() {
			return item;
		}

		/**
		 * Gets the experience from a bone
		 * @return The experience gained
		 */
		public short getExperience() {
			return experience;
		}

	}

	/**
	 * Handles the player burying the bone
	 * @param player The player burying
	 * @param bone The bone they are burying
	 */
	public static void buryBone(Client player, int bone) {
		if (System.currentTimeMillis() - player.buryDelay > BONE_BURY_TIME) {
			for (final Bones bones : Bones.values()) {
				if (bone == bones.getItem()) {
					player.getItems().deleteItem(bone, 1);
					player.sendMessage("You bury some "
							+ player.getItems().getItemName(bones.item) + ".");
					player.getPA().addSkillXP(bones.getExperience() * 1, 5);
					player.buryDelay = System.currentTimeMillis();
					player.startAnimation(827);
				}
			}
		}
	}

}