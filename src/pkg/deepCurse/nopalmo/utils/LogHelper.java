package pkg.deepCurse.nopalmo.utils;

/**
 * this class exists for the sole reason of im lazy, as far as i know, this is
 * really bad practice and i will replace it at some point, or at least upgrade
 * it, who knows really
 * 
 * @author deepCurse
 */
public class LogHelper {

//	public static void log(String text, Class<?> clazz) {
//		log(text, 0, clazz);
//	}
//
//	public static void log(String text, int level, Class<?> clazz) {
//		if (bootEnabled && level <= loggerLevel) {
//			System.out.println(clazz + ": " + text);
//		}
//	}

	public static void crash(Exception e) {
		e.printStackTrace();
		System.exit(8);
	}
}
