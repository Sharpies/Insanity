package com.lucas.packet;

import server.model.players.Client;

/**
 * Decodes incoming {@link Packet} data coming from our client.
 * @author Ares_
 * 
 */
public interface IPacketMessageDecoder {

	/**
     * Decodes an incoming {@link Packet} from the client
     * @param player The player who is using the packet.
     * @param packet The packet that is incoming.
     */
    void decodePacket(Client player, Packet packet);
    
}