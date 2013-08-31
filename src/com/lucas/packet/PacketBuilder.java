package com.lucas.packet;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import com.lucas.rs2.util.ChannelBufferUtils;

import server.util.ISAACCipher;

/**
 * A utility class for building packets.
 * 
 * @author Ares_
 * 
 */
public final class PacketBuilder {

	/**
	 * The operation code of the packet
	 */
	private final short opcode;

	/**
	 * The {@link ChannelBuffer} payload
	 */
	private final ChannelBuffer payload;

	/**
	 * The {@link PacketType}
	 */
	private final PacketType type;

	/**
	 * The current bit-position.
	 */
	private int bitPosition;

	/**
	 * The bit mask array.
	 */
	public static final int[] BIT_MASK_OUT = new int[32];

	/**
	 * Creates the bit mask array.
	 */
	static {
		for (int i = 0; i < BIT_MASK_OUT.length; i++) {
			BIT_MASK_OUT[i] = (1 << i) - 1;
		}
	}

	/**
	 * Constructs a new {@link PacketBuilder}.
	 */
	public PacketBuilder() {
		this(-1, ChannelBuffers.buffer(256), PacketType.FIXED);
	}

	/**
	 * Constructs a new {@link PacketBuilder}.
	 * @param opcode The opcode has been given.
	 */
	public PacketBuilder(int opcode) {
		this(opcode, ChannelBuffers.buffer(256), PacketType.FIXED);
	}

	/**
	 * Constructs a new {@link PacketBuilder}
	 * @param opcode The opcode given.
	 */
	public PacketBuilder(int opcode, ChannelBuffer buffer) {
		this(opcode, buffer, PacketType.FIXED);
	}

	/**
	 * Constructs a new {@link PacketBuilder}.
	 * @param opcode The opcode given.
	 * @param type The type of the packet.
	 */
	public PacketBuilder(int opcode, PacketType type) {
		this(opcode, ChannelBuffers.buffer(2048), type);
	}

	/**
	 * Constructs a new {@link PacketBuilder}
	 * @param opcode The opcode of packet.
	 * @param payload The dynamic buffer.
	 * @param type The packet type.
	 */
	public PacketBuilder(int opcode, ChannelBuffer payload, PacketType type) {
		this.opcode = (short) opcode;
		this.payload = payload;
		this.type = type;
	}

	/**
	 * Writes a packet header.
	 * @param cipher The encryption code.
	 * @param value The packet opcode.
	 * @return The super type for chaining.
	 */
	public PacketBuilder writeHeader(ISAACCipher cipher, int value) {
		put((byte) (value + cipher.getNextValue()));
		return this;
	}

	/**
	 * Start the bit access and sets the bit-position.
	 * @return The super type for chaining.
	 */
	public PacketBuilder startBitAccess() {
		bitPosition = payload.writerIndex() * 8;
		return this;
	}

	/**
	 * Finishes the bit access and sets the bit-position.
	 * @return The super type for chaining.
	 */
	public PacketBuilder finishBitAccess() {
		payload.writerIndex((bitPosition + 7) / 8);
		return this;
	}

	/**
	 * Writes as a boolean
	 * @param numBits The number of bits to be written. 
	 * @param value The generic value.
	 * @return The super type for chaining.
	 */
	public PacketBuilder putBits(int numBits, boolean value) {
		putBits(numBits, (value ? 1 : 0));
		return this;
	}

	/**
	 * Write a specific amount of bits to the buffer
	 * @param numBits The number of bits to be written.
	 * @param value The generic value.
	 * @return The super type for chaining.
	 */
	public PacketBuilder putBits(int numBits, int value) {
		int bytes = (int) Math.ceil((double) numBits / 8D) + 1;
		int position = (bitPosition + 7) / 8;
		payload.ensureWritableBytes(position + bytes);
		payload.writerIndex(position);
		int bytePos = bitPosition >> 3;
		int bitOffset = 8 - (bitPosition & 7);
		bitPosition += numBits;
		for (; numBits > bitOffset; bitOffset = 8) {
			byte b = payload.getByte(bytePos);
			payload.setByte(bytePos, (byte) (b & ~BIT_MASK_OUT[bitOffset]));
			payload.setByte(bytePos,
					(byte) (b | (value >> (numBits - bitOffset))
							& BIT_MASK_OUT[bitOffset]));
			bytePos++;
			numBits -= bitOffset;
		}
		byte b = payload.getByte(bytePos);
		if (numBits == bitOffset) {
			payload.setByte(bytePos, (byte) (b & ~BIT_MASK_OUT[bitOffset]));
			payload.setByte(bytePos,
					(byte) (b | value & BIT_MASK_OUT[bitOffset]));
		} else {
			payload.setByte(
					bytePos,
					(byte) (b & ~(BIT_MASK_OUT[numBits] << (bitOffset - numBits))));
			payload.setByte(
					bytePos,
					(byte) (b | (value & BIT_MASK_OUT[numBits]) << (bitOffset - numBits)));
		}
		return this;
	}

	/**
	 * Writes the contents of a message
	 * @throws IllegalArgumentException The exception thrown.
	 * @param message The message to be written.
	 * @return The super type for chaining.
	 */
	public PacketBuilder put(Packet message) {
		if (!message.isRaw()) {
			throw new IllegalArgumentException("message is not raw");
		}
		payload.writeBytes(message.getPayload());
		return this;
	}

	/**
	 * Writes a rip-code of bytes.
	 * @param bytes The bytes to be written.
	 * @return The super type for chaining.
	 */
	public PacketBuilder putBytes(byte[] bytes) {
		payload.writeBytes(bytes);
		return this;
	}

	/**
	 * Writes a byte array derived from an {@link ChannelBuffer}.
	 * @param buffer The dynamic buffer.
	 * @return The super type for chaining.
	 */
	public PacketBuilder putBytes(ChannelBuffer buffer) {
		byte[] bytes = new byte[buffer.readableBytes()];
		buffer.markReaderIndex();
		try {
			buffer.readBytes(bytes);
		} finally {
			buffer.resetReaderIndex();
		}
		putBytes(bytes);
		return this;
	}

	/**
	 * Writes a single byte to the buffer.
	 * @param b The byte to be written.
	 * @return The super type for chaining.
	 */
	public PacketBuilder put(int b) {
		payload.writeByte(b);
		return this;
	}

	/**
	 * Write a series of bytes to the buffer.
	 * @param b The bytes to be written.
	 * @return The super type for chaining.
	 */
	public PacketBuilder put(byte[] b) {
		payload.writeBytes(b);
		return this;
	}

	/**
	 * Write a single short to the buffer.
	 * @param s The short to be written.
	 * @return The super type for chaining.
	 */
	public PacketBuilder putShort(int s) {
		payload.writeShort(s);
		return this;
	}

	/**
	 * Write a single integer to the buffer.
	 * @param i The integer to be written.
	 * @return The super type for chaining.
	 */
	public PacketBuilder putInt(int i) {
		payload.writeInt(i);
		return this;
	}

	/**
	 * Writes a byte value on the packet.
	 * @param value The byte value to write on the packet.
	 * @return The PacketBuilder instance.
	 */
	public PacketBuilder writeByte(int value) {
		payload.writeByte((byte) value);
		return this;
	}

	/**
	 * Write a single long to the buffer.
	 * @param l The long to be written.
	 * @return The super type for chaining.
	 */
	public PacketBuilder putLong(long l) {
		payload.writeLong(l);
		return this;
	}

	/**
	 * Writes a single string broken down into bytes.
	 * @param string The string to be broken down.
	 * @return The super type for chaining.
	 */
	public PacketBuilder putString(String string) {
		ChannelBufferUtils.writeRS2String(string, payload);
		return this;
	}

	/**
	 * Checks if the buffer has enough read-able data.
	 * @return The result of the operation.
	 */
	public boolean isEmpty() {
		return payload.readableBytes() == 0;
	}

	/**
	 * Writes a single smart in the buffer.
	 * @param i The smart to be written.
	 */
	public void putSmart(int i) {
		if (i >= Byte.MAX_VALUE - 3) {
			putShort((short) i);
		} else {
			put((byte) i);
		}
	}

	/**
	 * Converts this PacketBuilder to a packet.
	 * @return The Packet object.
	 */
	public Packet toPacket() {
		return new Packet(((int) opcode), getPayload().copy(), type);
	}

	/**
	 * Gets the {@link Packet} opcode.
	 * @return The opcode.
	 */
	public short getOpcode() {
		return opcode;
	}

	/**
	 * Gets the {@link PacketType}.
	 * @return The packet-type.
	 */
	public PacketType getType() {
		return type;
	}

	/**
	 * Gets our {@link ChannelBuffer}.
	 * @return The payload.
	 */
	public ChannelBuffer getPayload() {
		return payload;
	}

	/**
	 * Writes a short with a standard addition.
	 * @param value The value to be written.
	 * @return The super type for chaining.
	 */
	public PacketBuilder writeAdditionalShort(int value) {
		payload.writeByte((byte) (value >> 8));
		payload.writeByte((byte) (value + 128));
		return this;
	}

	/**
	 * Writes a type-C byte.
	 * @param val The value to write.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putByteC(int val) {
		put((byte) (-val));
		return this;
	}

	/**
	 * Writes a little-endian short.
	 * @param val The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putLEShort(int val) {
		put((byte) (val));
		put((byte) (val >> 8));
		return this;
	}

}