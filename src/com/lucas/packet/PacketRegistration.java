package com.lucas.packet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import server.model.players.Client;

/**
 * Handles the loading and parsing of incoming packets.
 * @author Iz3 Legend
 * @author Sebastian <juan.2114@hotmail.com>
 * 
 */
public final class PacketRegistration {

	/**
	 * The logger instance for the registration of {@link Packet}'s.
	 */
	private static final Logger logger = Logger.getLogger(PacketRegistration.class.getName());

	/**
	 * Holds all of the statically built packets.
	 */
	private static IPacketMessageDecoder[] packets = new IPacketMessageDecoder[256];

	/**
	 * Handles the incoming packet transmission.
	 * 
	 * @param player
	 *            The player association.
	 * 
	 * @param packet
	 *            The packet of interest.
	 */
	public static void readPacket(Client player, Packet packet) {
		if (packets[packet.getOpcode()] != null) {
			packets[packet.getOpcode()].decodePacket(player, packet);
		}
	}

	/**
	 * Retrieves the array holding the loaded packets.
	 * 
	 * @return The retrieved array.
	 */
	public IPacketMessageDecoder[] getPackets() {
		return packets;
	}

	/**
	 * Modifies the array holding the loaded packets.
	 * 
	 * @param packets
	 *            The new modification.
	 */
	public void setPackets(IPacketMessageDecoder[] packets) {
		PacketRegistration.packets = packets;
	}

	/**
	 * @author Sebastian <juan.2114@hotmail.com>
	 * 
	 * @see java.lang.Object
	 */
	public static final class PacketLoader {

		/**
		 * Starts the loading of the {@link Packet}'s from outside.
		 */
		@SuppressWarnings("rawtypes")
		public static Map<Integer, Class> loadPackets()
				throws FileNotFoundException, IOException,
				ClassNotFoundException {
			BufferedReader fileReader = new BufferedReader(new FileReader(
					"./data/packets.txt"));
			Map<Integer, Class> complexMap = new HashMap<Integer, Class>();
			try {
				String line = fileReader.readLine();
				while (line != null) {
					String[] values = line.split("#");
					int indexOf = values[0].indexOf("(") + 1;
					String packetId = values[0].substring(indexOf);
					packetId = packetId.replaceAll(" ", "");
					int classIndex = values[1].indexOf("(") + 1;
					String className = values[1].substring(classIndex);
					className = className.replace(" ", "");
					Class c = Class
							.forName("com.lucas.packet.decode."
									+ className);
					complexMap.put(Integer.valueOf(packetId), c);
					line = fileReader.readLine();
				}
			} finally {
				fileReader.close();
			}
			return complexMap;
		}

		/**
		 * Loads the {@link Packet}'s and populates them from the complex
		 * {@link Map}.
		 */
		@SuppressWarnings("rawtypes")
		public static void parsePackets() throws FileNotFoundException,
				IOException, ClassNotFoundException, InstantiationException,
				IllegalAccessException {
			Map<Integer, Class> map = loadPackets();
			for (Map.Entry<Integer, Class> e : map.entrySet()) {
				if (e != null && e.getKey() != null && e.getValue() != null) {
					PacketRegistration.packets[e.getKey()] = ((IPacketMessageDecoder) e
							.getValue().newInstance());
					logger.info("Sucesfully loaded Packet: [ " + e.getKey()
							+ " - " + e.getValue().getName() + "].");
				}
			}
		}
	}
	
}
