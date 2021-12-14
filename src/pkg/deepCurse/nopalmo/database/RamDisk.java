package pkg.deepCurse.nopalmo.database;

import java.util.HashMap;

public class RamDisk {

	public static HashMap<String, Boolean> bools = new HashMap<String, Boolean>();
	// public static

	public static void init() {
		bools.put("shouldRun", true);
	}

}
