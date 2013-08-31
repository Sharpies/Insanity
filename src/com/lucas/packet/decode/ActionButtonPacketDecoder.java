package com.lucas.packet.decode;

import server.model.players.Client;

import com.lucas.packet.IPacketMessageDecoder;
import com.lucas.packet.Packet;

/**
 * The action button packet decoder using the implementation of {@link IPacketMessageDecoder}
 * @author Ares_
 *
 */
public final class ActionButtonPacketDecoder implements IPacketMessageDecoder {

	/*
	 * (non-Javadoc)
	 * @see com.lucas.packet.IPacketMessageDecoder#decodePacket(server.model.players.Client, com.lucas.packet.Packet)
	 */
	@Override
	public void decodePacket(Client player, Packet packet) {
		short actionButton = packet.getShort();
		switch(actionButton) {
		
		/*
		 * The logout button handler
		 */
		case 2458:
			
			break;
		}
	}

}