package pkg.deepCurse.nopalmo.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import pkg.deepCurse.nopalmo.core.Boot;
import pkg.deepCurse.nopalmo.database.DatabaseTools.Tools.Global;

public class StatusManager {

	private static List<Activity> activityList = new ArrayList<Activity>();
	private static int selection = 0;

	public static void init() {
		activityList.add(Activity.watching("my lead developer eat a watermelon whole"));
		activityList.add(Activity.watching(
				Boot.bot.getUserCache().asList().size() + " users in " + Boot.bot.getGuilds().size() + " servers"));
		activityList.add(Activity.watching("for " + Global.prefix + "help"));
		activityList.add(Activity.competing("your mothers love"));
//		activityList.add(EntityBuilder.createActivity("owo", null, ActivityType.CUSTOM_STATUS));
	}

	public static void shuffle(JDA bot) {

		int rand = new Random().nextInt(activityList.size());

		bot.getPresence().setActivity(activityList.get(rand));
		selection = rand;

	}

	public static void set(JDA bot, int interger) {

		bot.getPresence().setActivity(activityList.get(interger));
		selection = interger;

	}

	public static void increment(JDA bot) {
		bot.getPresence().setActivity(activityList.get(selection > activityList.size() ? selection = 0 : ++selection));
	}

}
