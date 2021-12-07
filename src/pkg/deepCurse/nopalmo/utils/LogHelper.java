package pkg.deepCurse.nopalmo.utils;

import pkg.deepCurse.nopalmo.core.Boot;

/**
 * this class exists for the sole reason of im lazy, as far as i know, this is
 * really bad practice and i will replace it at some point, or at least upgrade
 * it, who knows really
 * 
 * @author deepCurse
 */
public class LogHelper {

	public static int loggerLevel = 0;

	public static boolean bootEnabled = true;
	public static boolean guildCommandManagerEnabled = true;

	public static void boot(String text) {
		boot(text, 0);
	}

	public static void boot(String text, int level) {
		if (bootEnabled && level <= loggerLevel) {
			System.out.println(Boot.class + ": " + text);
		}
	}

	public static void crash(Exception e) {
		e.printStackTrace();
		System.exit(8);
	}

	public static void guildCommandManager(String text) {
		guildCommandManager(text);
	}

	public static void guildCommandManager(String text, int level) {
		if (guildCommandManagerEnabled && level <= loggerLevel) {
			System.out.println(Boot.class + ": " + text);
		}
	}
}
