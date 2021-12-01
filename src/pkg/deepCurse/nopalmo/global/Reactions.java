package pkg.deepCurse.nopalmo.global;

import java.util.HashMap;

public class Reactions {
	
	private static HashMap<String, Long> reactionMap = new HashMap<String, Long>();
	
	public static void init() {
		reactionMap.put("galaxyThumb", 801657838358495232L);
	}
	
	public static String getReaction(String id) {
		return ":"+id+":"+reactionMap.get(id);
	}
	
	public static String getEmote(String id) {
		return "<:"+id+":"+reactionMap.get(id)+">";
	}
	
}
