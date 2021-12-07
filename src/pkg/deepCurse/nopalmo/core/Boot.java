package pkg.deepCurse.nopalmo.core;

import java.sql.SQLException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import pkg.deepCurse.nopalmo.database.DatabaseTools;
import pkg.deepCurse.nopalmo.global.Reactions;
import pkg.deepCurse.nopalmo.listener.DirectMessageReceivedListener;
import pkg.deepCurse.nopalmo.listener.GuildMessageReceivedListener;
import pkg.deepCurse.nopalmo.manager.DirectCommandManager;
import pkg.deepCurse.nopalmo.manager.GuildCommandManager;
import pkg.deepCurse.nopalmo.utils.Locks;
import pkg.deepCurse.nopalmo.utils.LogHelper;

public class Boot {

	public static JDA bot;
	public static DatabaseTools databaseTools = null;
	public static GuildCommandManager guildCommandManager = new GuildCommandManager(); // move to master manager
	public static DirectCommandManager directCommandManager = new DirectCommandManager(); // move to master manager

	public static boolean isProd = false;
	
	public static long pid = ProcessHandle.current().pid();

	public static void main(String[] args) {
		LogHelper.boot("Booting: <" + pid + ">");

		LogHelper.boot("Testing Lock. . .");

		try {
			if (Locks.dirLock("nopalmo.lock")) {
				LogHelper.boot("Failed to lock. . .\nShutting down. . .");
				System.exit(3);
			} else {
				LogHelper.boot("Nopalmo is locked. . .");
			}
		} catch (Exception e) {
			LogHelper.crash(e);
		}

		long preBootTime = System.currentTimeMillis();

		isProd = args[2].contentEquals("prod");

		LogHelper.boot("Connecting to mariadb:nopalmo");
		try {
			databaseTools = new DatabaseTools(args[1]);
			LogHelper.boot("Connected. . .");
		} catch (SQLException | ClassNotFoundException e1) {
			e1.printStackTrace();
			LogHelper.boot("Failed to connect\nShutting down. . .");
			System.exit(4);
		}

		LogHelper.boot("Init reaction/emote list");
		Reactions.init();
		LogHelper.boot("Initialized reaction/emote list. . .");
		LogHelper.boot("Init guild commands list");
		guildCommandManager.init();
		LogHelper.boot("Initialized guild commands list. . .");
		
		try {
			LogHelper.boot("Sleeping");
			Thread.sleep(90000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		try {
			bot = JDABuilder.createDefault(args[0]).setChunkingFilter(ChunkingFilter.NONE)
					.setMemberCachePolicy(MemberCachePolicy.NONE).enableIntents(GatewayIntent.GUILD_MEMBERS)
					.setActivity(Activity.watching("Loading users...")).setIdle(true)
					.addEventListeners(new GuildMessageReceivedListener())
					.addEventListeners(new DirectMessageReceivedListener()).build().awaitReady();
		} catch (Exception e) {
			LogHelper.crash(e);
		}

		bot.getPresence().setActivity(Activity.listening("Infected Mushroom"));

		long bootTime = System.currentTimeMillis() - preBootTime;

		LogHelper.boot("Taken " + bootTime + "ms to boot");
		
		LogHelper.boot("Starting loop");

	}

}
