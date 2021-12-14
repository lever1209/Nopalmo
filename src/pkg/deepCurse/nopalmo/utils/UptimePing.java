package pkg.deepCurse.nopalmo.utils;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Date;

public class UptimePing {

	public static long sendPing(String IP) throws Exception {

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

		if (socketAddress.getAddress() == null) {
			return -1;
		} else {
			return timeToRespond;
		}
	}
}
