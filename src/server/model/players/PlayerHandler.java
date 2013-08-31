package server.model.players;

import com.lucas.Server;
import com.lucas.rs2.Config;

import server.model.npcs.NPCHandler;
import server.model.players.Client;
import server.model.players.Player;
import server.model.players.PlayerSave;
import server.util.Misc;
import server.util.Stream;

public class PlayerHandler {

	public static Object lock = new Object();
	public static Player players[] = new Player[Config.MAX_PLAYERS];
	public static String messageToAll = "";

	public static boolean updateAnnounced;
	public static boolean updateRunning;
	public static int updateSeconds;
	public static long updateStartTime;

	/**
	 * Checks if the current player is online
	 */
	public static String playersCurrentlyOn[] = new String[Config.MAX_PLAYERS];

	private boolean kickAllPlayers = false;

	static {
		for (int i = 0; i < Config.MAX_PLAYERS; i++)
			players[i] = null;
	}

	public boolean newPlayerClient(Client client1) {
		int slot = -1;
		for (int i = 1; i < Config.MAX_PLAYERS; i++) {
			if ((players[i] == null) || players[i].disconnected) {
				slot = i;
				break;
			}
		}
		if (slot == -1)
			return false;
		client1.handler = this;
		client1.playerId = slot;
		players[slot] = client1;
		players[slot].isActive = true;
		if (Config.SERVER_DEBUG)
			Misc.println("Player Slot " + slot + " slot 0 " + players[0]
					+ " Player Hit " + players[slot]);
		return true;
	}

	public void destruct() {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] == null)
				continue;
			players[i].destruct();
			players[i] = null;
		}
	}

	public static int getPlayerCount() {
		int count = 0;
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] != null) {
				count++;
			}
		}
		return count;
	}

	/**
	 * We check if the player is online or not
	 * 
	 * @param playerName
	 *            The player who is online
	 * @return Always return false to avoid problems
	 */
	public static boolean isPlayerOn(String playerName) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (playersCurrentlyOn[i] != null) {
				if (playersCurrentlyOn[i].equalsIgnoreCase(playerName)) {
					return true;
				}
			}
		}
		return false;
	}

	public void process() {
		if (kickAllPlayers) {
			for (int i = 1; i < Config.MAX_PLAYERS; i++) {
				if (players[i] != null) {
					players[i].disconnected = true;
				}
			}
		}
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] == null || !players[i].isActive
					|| !players[i].initialized)
				continue;
			try {
				if (players[i].disconnected
						&& (System.currentTimeMillis() - players[i].logoutDelay > 10000
								|| players[i].properLogout || kickAllPlayers)) {
					if (players[i].inTrade) {
						Client o = (Client) PlayerHandler.players[players[i].tradeWith];
						if (o != null) {
							o.getTradeAndDuel().declineTrade();
						}
					}
					if (players[i].duelStatus == 5) {
						Client o = (Client) PlayerHandler.players[players[i].duelingWith];
						if (o != null) {
							o.getTradeAndDuel().duelVictory();
						}
					} else if (players[i].duelStatus <= 4
							&& players[i].duelStatus >= 1) {
						Client o = (Client) PlayerHandler.players[players[i].duelingWith];
						if (o != null) {
							o.getTradeAndDuel().declineDuel();
						}
					}
					Client o = (Client) PlayerHandler.players[i];
					if (PlayerSave.saveGame(o)) {
						System.out.println("Game saved for player "
								+ players[i].playerName);
					} else {
						System.out.println("Could not save for "
								+ players[i].playerName);
					}
					removePlayer(i);
					players[i] = null;
					continue;
				}
				players[i].preProcessing();
				players[i].processQueuedPackets();
				players[i].process();
				players[i].postProcessing();
				players[i].getNextPlayerMovement();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] == null || !players[i].isActive
					|| !players[i].initialized)
				continue;
			try {
				players[i].update();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] == null || !players[i].isActive
					|| !players[i].initialized)
				continue;
			try {
				players[i].clearUpdateFlags();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (updateRunning && !updateAnnounced) {
			updateAnnounced = true;
			Server.UpdateServer = true;
		}
		if (updateRunning
				&& (System.currentTimeMillis() - updateStartTime > (updateSeconds * 1000))) {
			kickAllPlayers = true;
		}
	}

	public void updateNPC(Player plr, Stream str) {
		// synchronized(plr) {
		updateBlock.currentOffset = 0;

		str.createFrameVarSizeWord(65);
		str.initBitAccess();

		str.writeBits(8, plr.npcListSize);
		int size = plr.npcListSize;
		plr.npcListSize = 0;
		for (int i = 0; i < size; i++) {
			if (plr.RebuildNPCList == false
					&& plr.withinDistance(plr.npcList[i]) == true) {
				plr.npcList[i].updateNPCMovement(str);
				plr.npcList[i].appendNPCUpdateBlock(updateBlock);
				plr.npcList[plr.npcListSize++] = plr.npcList[i];
			} else {
				int id = plr.npcList[i].npcId;
				plr.npcInListBitmap[id >> 3] &= ~(1 << (id & 7));
				str.writeBits(1, 1);
				str.writeBits(2, 3);
			}
		}

		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] != null) {
				int id = NPCHandler.npcs[i].npcId;
				if (plr.RebuildNPCList == false
						&& (plr.npcInListBitmap[id >> 3] & (1 << (id & 7))) != 0) {

				} else if (plr.withinDistance(NPCHandler.npcs[i]) == false) {

				} else {
					plr.addNewNPC(NPCHandler.npcs[i], str, updateBlock);
				}
			}
		}

		plr.RebuildNPCList = false;

		if (updateBlock.currentOffset > 0) {
			str.writeBits(14, 16383);
			str.finishBitAccess();
			str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
		} else {
			str.finishBitAccess();
		}
		str.endFrameVarSizeWord();
		// }
	}

	private Stream updateBlock = new Stream(new byte[Config.BUFFER_SIZE]);

	public void updatePlayer(Player plr, Stream str) {
		// synchronized(plr) {
		updateBlock.currentOffset = 0;
		if (updateRunning && !updateAnnounced) {
			str.createFrame(114);
			str.writeWordBigEndian(updateSeconds * 50 / 30);
		}
		plr.updateThisPlayerMovement(str);
		boolean saveChatTextUpdate = plr.isChatTextUpdateRequired();
		plr.setChatTextUpdateRequired(false);
		plr.appendPlayerUpdateBlock(updateBlock);
		plr.setChatTextUpdateRequired(saveChatTextUpdate);
		str.writeBits(8, plr.playerListSize);
		int size = plr.playerListSize;
		if (size >= 79)
			size = 79;
		plr.playerListSize = 0;
		for (int i = 0; i < size; i++) {
			if (!plr.didTeleport && !plr.playerList[i].didTeleport
					&& plr.withinDistance(plr.playerList[i])) {
				plr.playerList[i].updatePlayerMovement(str);
				plr.playerList[i].appendPlayerUpdateBlock(updateBlock);
				plr.playerList[plr.playerListSize++] = plr.playerList[i];
			} else {
				int id = plr.playerList[i].playerId;
				plr.playerInListBitmap[id >> 3] &= ~(1 << (id & 7));
				str.writeBits(1, 1);
				str.writeBits(2, 3);
			}
		}

		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] == null || !players[i].isActive || players[i] == plr)
				continue;
			int id = players[i].playerId;
			if ((plr.playerInListBitmap[id >> 3] & (1 << (id & 7))) != 0)
				continue;
			if (!plr.withinDistance(players[i]))
				continue;
			plr.addNewPlayer(players[i], str, updateBlock);
		}

		if (updateBlock.currentOffset > 0) {
			str.writeBits(11, 2047);
			str.finishBitAccess();
			str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
		} else
			str.finishBitAccess();

		str.endFrameVarSizeWord();
		// }
	}

	private void removePlayer(int i) {
		if (players[i].privateChat != 2) {
			for (int index = 1; index < Config.MAX_PLAYERS; index++) {
				if (players[index] == null || players[index].isActive == false)
					continue;
				Client o = (Client) PlayerHandler.players[index];
				if (o != null) {
					o.getPA().updatePM(players[i].playerId, 0);
				}
			}
		}
		players[i].destruct();
		players[i] = null;
	}

}