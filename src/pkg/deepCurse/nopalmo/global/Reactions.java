package pkg.deepCurse.nopalmo.global;

import java.util.HashMap;

public class Reactions {

	private static final HashMap<String, Long> reactionMap = new HashMap<String, Long>();
	private static final HashMap<String, String> internalReactionMap = new HashMap<String, String>();

	public static void init() {
		insert("galaxyThumb", 801657838358495232L);
		insert("kirbo_wadafuq", 799633705068003338L);

		insertInternal("flushed", "U+1F633");
		insertInternal("eggplant", "U+1F346");
	}

	private static void insertInternal(String name, String emote) {
		internalReactionMap.put(name, emote);
	}

	public static void insert(String input, long id) {
		reactionMap.put(input, id);
	}

	public static String getReaction(String id) {
		return id.startsWith(":") ? internalReactionMap.get(id.substring(1)) : ":" + id + ":" + reactionMap.get(id);
	}

	public static String getEmote(String id) {
		return id.startsWith(":") ? internalReactionMap.get(id.substring(1))
				: "<:" + id + ":" + reactionMap.get(id) + ">";
	}

}
