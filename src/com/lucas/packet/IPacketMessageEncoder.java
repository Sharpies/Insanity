package com.lucas.packet;

import server.model.players.Client;

/**
 * Encodes an outgoing {@link Packet} going from the server to client.
 * @author Ares_
 * 
 */
public interface IPacketMessageEncoder {

    /**
     * Encodes an outgoing {@link Packet}.
     * @param player The {@link Player} of interest.
     */
    void encodePacket(Client player);
    
}