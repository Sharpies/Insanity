package com.lucas.packet.encode;

import server.event.CycleEventHandler;
import server.model.players.Client;

import com.lucas.packet.IPacketMessageEncoder;
import com.lucas.packet.PacketBuilder;

/**
 * The logging out packet with an implementation of {@link IPacketMessageEncoder}
 * @author Ares_
 *
 */
public final class LogOutPacketEncoder implements IPacketMessageEncoder {

	/*
	 * (non-Javadoc)
	 * @see com.lucas.packet.IPacketMessageEncoder#encodePacket(server.model.players.Player)
	 */
	@Override
	public void encodePacket(Client player) {
		//PacketBuilder builder = new PacketBuilder();
		// checks if the player is in combat
		if (player.underAttackBy > 0 || player.underAttackBy2 > 0)
			return;
		if (System.currentTimeMillis() - player.logoutDelay > 10000) {
			//builder.writeHeader(, 109);
			player.outStream.createFrame(109);
			CycleEventHandler.getSingleton().stopEvents(this);
			player.properLogout = true;
		} else {
			player.sendMessage("You must wait a few seconds from being out of combat to logout.");
		}
	}

}