package com.lucas.packet;

import org.jboss.netty.buffer.ChannelBuffer;

import com.lucas.rs2.util.ChannelBufferUtils;

/**
 * Represents a single packet used in the in-game protocol.
 * @author Ares_
 *
 */
public final class Packet {

	/**
	 * The operation code for the packet
	 */
	private final short opcode;
	
	/**
	 * The {@link ChannelBuffer} payload
	 */
	private final ChannelBuffer payload;

	/**
	 * The {@link PacketType}
	 */
	private PacketType type;
	
	/**
	 * Constructs a new {@link Packet}
	 * @param opcode The operation code
	 * @param payload The packet payload
	 * @param type The packet type
	 */
	public Packet(int opcode, ChannelBuffer payload, PacketType type) {
		this.opcode = (short)opcode;
		this.payload = payload;
		this.type = type;
	}
	
	/**
	 * Reads a single byte from the payload or buffer.
	 * @return The byte that is/was read.
	 */
	public byte get() {
		return payload.readByte();
	}
	
	/**
     * Reads a single short from the buffer.
     * @return The short that was read.
     */
	public short getShort() {
        return payload.readShort();
    }
	
	/**
     * Reads a single integer from the buffer.
     * @return The integer that was read.
     */
    public int getInt() {
        return payload.readInt();
    }

    /**
     * Reads a single long from the buffer.
     * @return The long that was read.
     */
    public long getLong() {
        return payload.readLong();
    }

    /**
     * Reads a series of bytes from the buffer.
     * @param data The bytes to be read.
     * @param off The off-set.
     * @param len The length of the chain.
     */
    public void get(byte[] data, int off, int len) {
        payload.readBytes(data, off, len);
    }

    /**
     * Checks the amount of remaining data.
     * @return The result of the check.
     */
    public int remaining() {
        return payload.readableBytes();
    }

    /**
     * Checks if the packet is considered raw.
     * @return The result of the check.
     */
    public boolean isRaw() {
        return opcode == -1;
    }
    
    /**
     * The length of a {@link Packet}.
     *  @return The length.
     */
    public short getLength() {
        return (short) payload.capacity();
    }
    
	/**
	 * Gets the {@link PacketType}
	 * @return The packet type
	 */
	public PacketType getType() {
		return type;
	}
	
	/**
	 * Gets the {@link ChannelBuffer} payload
	 * @return The payload
	 */
	public ChannelBuffer getPayload() {
		return payload;
	}
	
	/**
	 * Gets the packet operation code
	 * @return The operation code
	 */
	public short getOpcode() {
		return opcode;
	}
	
	/**
     * Reads a RuneScape style string.
     * @return The string that was read.
     */
    public String getRuneScapeString() {
        return ChannelBufferUtils.readRS2String(getPayload());
    }
    
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
    public String toString() {
        return super.toString() + " [" + opcode + "," + payload.capacity() + "]";
    }
	
}