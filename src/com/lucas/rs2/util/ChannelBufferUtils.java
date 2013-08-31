package com.lucas.rs2.util;

import org.jboss.netty.buffer.ChannelBuffer;

public final class ChannelBufferUtils {

	/**
	 * Parses the name of a player.
	 * 
	 * @param parser
	 *            The string to be parsed.
	 * 
	 * @return The result of the operation.
	 */
	public static String parsePlayerName(String parser) {
		return adjust(parser.replace(" ", "_"));
	}

	/**
	 * Reads a string from the dynamic buffer.
	 * 
	 * @param payload
	 *            The dynamic buffer.
	 * 
	 * @return The result.
	 */
	public static String readRS2String(ChannelBuffer payload) {
		StringBuilder bldr = new StringBuilder();
		byte b;
		while (payload.readable() && (b = payload.readByte()) != 10) {
			bldr.append((char) b);
		}
		return bldr.toString();
	}

	/**
	 * Write a string to the buffer.
	 * 
	 * @param string
	 *            The string to be written.
	 * 
	 * @param buffer
	 *            The dynamic buffer.
	 */
	public static void writeRS2String(String string, ChannelBuffer buffer) {
		buffer.writeBytes(string.getBytes());
		buffer.writeByte(10);
	}

	/**
	 * Adjusts a RuneScape player's name to standards.
	 * 
	 * @param parser
	 *            The string to be parsed.
	 * 
	 * @return The result of the operation.
	 */
	private static String adjust(final String parser) {
		if (parser.length() > 0) {
			final char characters[] = parser.toCharArray();
			for (int j = 0; j < characters.length; j++)
				if (characters[j] == '_') {
					characters[j] = ' ';
					if ((j + 1 < characters.length)
							&& (characters[j + 1] >= 'a')
							&& (characters[j + 1] <= 'z')) {
						characters[j + 1] = (char) ((characters[j + 1] + 65) - 97);
					}
				}
			if ((characters[0] >= 'a') && (characters[0] <= 'z')) {
				characters[0] = (char) ((characters[0] + 65) - 97);
			}
			return new String(characters);
		} else {
			return parser;
		}
	}

	/**
	 * Converts the username to a long value.
	 * 
	 * @param s
	 *            The username.
	 * 
	 * @return the long value
	 */
	public static long nameToLong(String s) {
		long l = 0L;
		for (int i = 0; i < s.length() && i < 12; i++) {
			char c = s.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z')
				l += (1 + c) - 65;
			else if (c >= 'a' && c <= 'z')
				l += (1 + c) - 97;
			else if (c >= '0' && c <= '9')
				l += (27 + c) - 48;
		}
		while (l % 37L == 0L && l != 0L)
			l /= 37L;
		return l;
	}

}