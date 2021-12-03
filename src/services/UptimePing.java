package services;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.Date;

public class UptimePing {

	public static long sendPing(String IP) throws UnknownHostException, IOException {
		/*
		 * StopWatch stopWatch = new StopWatch(); stopWatch.start(); InetAddress sender
		 * = InetAddress.getByName(IP); System.out.println("Pinging: " + IP);
		 * stopWatch.stop(); long timeP = stopWatch.getTime(TimeUnit.MICROSECONDS);
		 * System.out.println(timeP); if
		 * (sender.isReachable(5000)){//.isReachable(5000)) {
		 * System.out.println("Successfully pinged: " + IP); return timeP; } else {
		 * System.out.println("Failed to ping: " + IP); return -1; }
		 */
		try {
			int port = 80;
			long timeToRespond = 0;

			InetAddress inetAddress = InetAddress.getByName(IP);
			InetSocketAddress socketAddress = new InetSocketAddress(inetAddress, port);

			SocketChannel sc = SocketChannel.open();
			sc.configureBlocking(true);

			Date start = new Date();
			if (sc.connect(socketAddress)) {
				Date stop = new Date();
				timeToRespond = (stop.getTime() - start.getTime());

			}

			// System.out.println("fix me"); // not sure why this is here

			if (socketAddress.getAddress() == null) {
				return -1;
			} else {
				return timeToRespond;
			}
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
			return -1;
		}
	}
}
