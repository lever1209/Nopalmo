package pkg.deepCurse.nopalmo.server.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Socks {
	
	public static void sendStringSock(String address, int port, String input) {
		try {
			Socket cSocket = new Socket(address, port);
			DataOutputStream dOut = new DataOutputStream(cSocket.getOutputStream());
			dOut.writeUTF(input);
			dOut.flush();
			dOut.close();
			cSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
