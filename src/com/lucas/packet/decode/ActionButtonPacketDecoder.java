package com.lucas.packet.decode;

import java.util.logging.Logger;

import server.model.players.Client;

import com.lucas.packet.IPacketMessageDecoder;
import com.lucas.packet.Packet;
import com.lucas.packet.encode.LogOutPacketEncoder;

/**
 * The action button packet decoder using the implementation of {@link IPacketMessageDecoder}
 * @author Ares_
 *
 */
public final class ActionButtonPacketDecoder implements IPacketMessageDecoder {

	/**
	 * The {@link Logger} instance for the {@link ActionButtonPacketDecoder} class.
	 */
	private static Logger logger = Logger.getLogger(ActionButtonPacketDecoder.class.getName());
	
	/*
	 * (non-Javadoc)
	 * @see com.lucas.packet.IPacketMessageDecoder#decodePacket(server.model.players.Client, com.lucas.packet.Packet)
	 */
	@Override
	public void decodePacket(Client player, Packet packet) {
		final int button = packet.getShort();
		switch(button) {
		
		/*
		 * The logout button handler
		 */
		case 2458:
			player.writePacketMessage(new LogOutPacketEncoder());
			break;
		
		default:
			player.sendMessage("Un supported button id:" +button);
			logger.info("Unhandled action button : " + button);
			break;
		}
	}

}