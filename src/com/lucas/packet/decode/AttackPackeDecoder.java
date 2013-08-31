package com.lucas.packet.decode;

import server.model.players.Client;

import com.lucas.packet.IPacketMessageDecoder;
import com.lucas.packet.Packet;

/**
 * The attack packet handler implementing {@link IPacketMessageDecoder}
 * @author Ares_
 *
 */
public final class AttackPackeDecoder implements IPacketMessageDecoder {

	/**
	 * The attack player & attack mob operation codes.
	 */
	//private static final byte ATTACK_PLAYER = 73, ATTACK_MOB = 72;
	
	/*
	 * (non-Javadoc)
	 * @see com.lucas.packet.IPacketMessageDecoder#decodePacket(server.model.players.Client, com.lucas.packet.Packet)
	 */
	@Override
	public void decodePacket(Client player, Packet packet) {

	}
	
}