package pkg.deepCurse.simpleLoggingGarbage.core;

import javax.security.auth.login.LoginException;

/**
 * this class exists for the sole reason of im lazy, as far as i know, this is
 * really bad practice and i will replace it at some point, or at least upgrade
 * it, who knows really
 * 
 * @author deepCurse
 */
public class Log {

	public static int loggerLevel = 0;

	//@formatter:off
	public static boolean bootEnabled = true;
	public static void boot(String text) {
		boot(text,0);
	}
	public static void boot(String text, int level) {
		if (bootEnabled && level <= loggerLevel) {
			System.out.println(text);
		}
	}
	//@formatter:on
	public static void crash(Exception e) {
	}
}
