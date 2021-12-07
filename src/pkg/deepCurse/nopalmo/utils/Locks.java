package pkg.deepCurse.nopalmo.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Scanner;

import pkg.deepCurse.nopalmo.core.Boot;

public class Locks {

	/**
	 * 
	 * @param lockName
	 * @return true on exists, false on vacant
	 * @throws Exception
	 */
	public static boolean dirLock(String lockName) throws Exception {

		long pid = 0L;
		Scanner pidScanner = new Scanner(new File(System.getProperty("user.dir") + "/" + lockName));
		StringBuilder pidBuilder = new StringBuilder();
		while (pidScanner.hasNext()) {
			pidBuilder.append(pidScanner.next());
		}
		pidScanner.close();
		pid = Long.parseLong(pidBuilder.toString().replaceAll("[^0-9]", ""));
		
		
		
		Process proc = new ProcessBuilder().command("readlink", "/proc/" + pid + "/cwd").start();
		Scanner readlinkScanner = new Scanner(new InputStreamReader(proc.getInputStream()));
		StringBuilder readlinkBuilder = new StringBuilder();
		while (readlinkScanner.hasNext()) {
			readlinkBuilder.append(readlinkScanner.next());
		}
		readlinkScanner.close();
		
		
		
		if (readlinkBuilder.toString().contentEquals(new File(System.getProperty("user.dir")).getPath())) {
			return true;
		} else {
			FileWriter writer = new FileWriter(new File(System.getProperty("user.dir") + "/" + lockName));

			writer.write(String.valueOf(Boot.pid));
			writer.close();
			return false;
		}
	}

}
